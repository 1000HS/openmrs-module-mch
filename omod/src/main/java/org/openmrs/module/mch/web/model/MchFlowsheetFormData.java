package org.openmrs.module.mch.web.model;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.module.heightweighttracker.impl.pih.WHOMapping;
import org.openmrs.module.mch.impl.pih.ConceptDictionary;
import org.openmrs.module.mch.impl.pih.ProphylaxisMapping;
import org.openmrs.module.mch.regimen.RegimenDrugHelper;
import org.openmrs.module.mch.utils.Utils;
import org.openmrs.module.mch.web.UIHelper;

public class MchFlowsheetFormData extends FlowsheetFormData {
	
	protected final Log log = LogFactory.getLog(getClass());

	private WHOMapping whoMapping = null;
	
	public MchFlowsheetFormData(Patient p) {
		super(p);
	}

	public String getGender()
	{
		return getPatient().getGender();
	}
	public int getAge()
	{
		return getPatient().getAge();
	}


	public PatientProgram getPediHIVProgram() {
		Program hivProgram = Context.getProgramWorkflowService().getProgram(ConceptDictionary.ProgramId_PediHIVProgram);
		if (hivProgram == null)
			throw new RuntimeException("Please configure the mapping in the global property for the hiv program.  The current value resolves to " + ConceptDictionary.ProgramId_PediHIVProgram);
		PatientProgram ret = null;
		if (hivProgram != null){
			List<PatientProgram> ppList = Context.getProgramWorkflowService().getPatientPrograms(this.getPatient(), hivProgram, null, null, null, null, false);
			if (ppList != null && ppList.size() > 0){
				//sorts descending
				Collections.sort(ppList,new Comparator<PatientProgram>() {
			        public int compare(PatientProgram left, PatientProgram right) {
			     	    Date now = new Date();
			            long leftWeight = (left.getDateEnrolled() == null) ? now.getTime() : left.getDateEnrolled().getTime();
			            long rightWeight = (right.getDateEnrolled() == null) ? now.getTime() : right.getDateEnrolled().getTime();
			            if (leftWeight < rightWeight) 
			           	    return 1;
			            return -1;
			        }    
			     });
				return ppList.get(0);
			}
		}	
		return ret;
	}
	
	private int getPatientInformedState(Date date){
		PatientProgram pp = getPediHIVProgram();
		if (pp != null){
			for (PatientState ps : pp.getStates()){
				if (ps.getState().getProgramWorkflow().getConcept().getConceptId().equals(ConceptDictionary.INFORMED_STATUS))
				{
					if(ps.getStartDate() != null && date != null && (ps.getStartDate().equals(date) || ps.getStartDate().before(date)))
					{
						if(ps.getEndDate() == null || ps.getEndDate().after(date))
						{
							return ps.getState().getConcept().getId();
						}
						
					}
				}
			}
		}
		return 0;
	}
	
	public boolean isPatientInformed(Date date)
	{
		int id = getPatientInformedState(date);
		if(id == ConceptDictionary.PATIENT_INFORMED)
		{
			return true;
		}
		return false;
	}
	
	public boolean isPatientNotInformed(Date date)
	{
		int id = getPatientInformedState(date);
		if(id == ConceptDictionary.PATIENT_NOT_INFORMED)
		{
			return true;
		}
		return false;
	}
	
	private int arvRowNum = 0;
	public String getArvRegimenHtmlTable() {
    	StringBuilder builder = new StringBuilder(4096);
    	builder.append("<table class=\"section-table\">\n");
    	builder.append("<tr>\n");
    	builder.append("<th scope=\"col\" id=\"col-arv-emr-1\">EMR</th>\n");
    	builder.append("<th scope=\"col\" id=\"col-arv-info\">TRAITEMENT ANTIRETRVIRAL</th>\n");
    	builder.append("<th scope=\"col\" id=\"col-arv-emr-2\">EMR</th>\n");
    	builder.append("<th scope=\"col\" id=\"col-arv-stop\"></th>\n");
    	builder.append("</tr>\n");

    	Map<Date,Set<DrugOrder>> drugOrdersGroupedByStartAndEndDate = getRegimenChanges(this.getDrugOrders(ConceptDictionary.ANTIRETROVIRAL_DRUGS));
		int index = 0;
		Map.Entry<Date, Set<DrugOrder>> prevMapEntry = null;
		List<DrugOrder> prevOrder = null;
	
		List<DrugOrder> currentOrder = null;
		for (Map.Entry<Date, Set<DrugOrder>> e : drugOrdersGroupedByStartAndEndDate.entrySet()){
			
			
			currentOrder = new ArrayList<DrugOrder>(e.getValue());
			
			//for pedi we only care about when the drug changes not if only the dosage changes.
			if(changeRegimen(prevOrder, currentOrder) || index == drugOrdersGroupedByStartAndEndDate.size() -1)
			{
				if (prevMapEntry != null && prevMapEntry.getValue().size() > 0)
				{
					Date changeDate = null;
					if(index < drugOrdersGroupedByStartAndEndDate.size() -1)
					{
						for(DrugOrder dro: prevOrder)
						{
							if(dro.isDiscontinued(e.getKey()))
							{
								changeDate = dro.getEffectiveStopDate();
							}
						}
					}
					addARVRegimenTableRow(builder,  prevOrder, prevMapEntry.getKey(), changeDate);
					
				}
				
				Date changeDate = null;
				for(DrugOrder dro: currentOrder)
				{
					if(dro.isDiscontinuedRightNow())
					{
						changeDate = dro.getEffectiveStopDate();
					}
				}
				if ((index == drugOrdersGroupedByStartAndEndDate.size() -1 && e.getValue().size() > 0 && changeRegimen(prevOrder, currentOrder)) || (drugOrdersGroupedByStartAndEndDate.size() == 1 && e.getValue().size() > 0 ))
					addARVRegimenTableRow(builder,  new ArrayList<DrugOrder>(e.getValue()), e.getKey(), changeDate);
				
				prevMapEntry = e;		
			}
			
			if(prevMapEntry == null)
			{
				prevMapEntry = e;
			}
			
			if(changeRegimen(prevOrder, currentOrder) || prevOrder == null)
			{
				prevOrder = currentOrder;
			}
			index ++;
			
			//add to usedDrugOrderIds, for easy exclusion later on in the 'other medications' category'
			if (e.getValue() != null){
				for (DrugOrder dorTmp : e.getValue()){
					if (dorTmp.getOrderId() != null)
						usedDrugOrderIds.add(dorTmp.getOrderId());
				}
			}
		}

		// Make sure there is always a blank row, or 3.
		addARVRegimenTableRow(builder, null, null, null);
		addARVRegimenTableRow(builder, null, null, null);
		addARVRegimenTableRow(builder, null, null, null);
		addARVRegimenTableRow(builder, null, null, null);
		builder.append("</table>\n");
		
		return builder.toString();
    }
    
	private boolean changeRegimen(List<DrugOrder> prevOrder, List<DrugOrder> currentOrder) {
		
		if(prevOrder == null)
		{
			return false;
		}
		
		if(UIHelper.formatRegimenDisplaySummaryPedi(prevOrder).equals(UIHelper.formatRegimenDisplaySummaryPedi(currentOrder)))
		{
			return false;
		}
		
		return true;
	}

	private void addARVRegimenTableRow(StringBuilder builder, List<DrugOrder> drugOrders, Date changeDate, Date nextChangeDate) {
    	builder.append("<tr>\n");
    	boolean drugRegimenInEmr = !(drugOrders == null || drugOrders.size() == 0);
    	// Add a check if it's from the EMR
    	builder.append("<td class=\"section-emr\">");
    	if(drugRegimenInEmr) {
    		builder.append("&#x2713;");    		
    	}
    	builder.append("</td>\n");    		
    	
    	RegimenDrugHelper drugHelper = new RegimenDrugHelper();
    	boolean isABC_3TC_NVP = drugHelper.isABC_3TC_NVP(drugOrders);
    	boolean isABC_3TC_EFV = drugHelper.isABC_3TC_EFV(drugOrders);
    	boolean isAZT_3TC_NVP = drugHelper.isAZT_3TC_NVP(drugOrders);
    	boolean isAZT_3TC_EFV = drugHelper.isAZT_3TC_EFV(drugOrders);
    	boolean isD4T_3TC_NVP = drugHelper.isD4T_3TC_NVP(drugOrders);
		boolean isD4T_3TC_EFV = drugHelper.isD4T_3TC_EFV(drugOrders);
		boolean isABC_3TC_LPVr = drugHelper.isABC_3TC_LPVr(drugOrders);
		boolean isAZT_3TC_LPVr = drugHelper.isAZT_3TC_LPVr(drugOrders);
		boolean isD4T_3TC_LPVr = drugHelper.isD4T_3TC_LPVr(drugOrders);
		
		boolean isOther = drugRegimenInEmr && !(isABC_3TC_NVP || isABC_3TC_EFV || isAZT_3TC_NVP ||
				isAZT_3TC_EFV || isD4T_3TC_NVP || isD4T_3TC_EFV || isABC_3TC_LPVr || isAZT_3TC_LPVr || isD4T_3TC_LPVr);
		

		builder.append("<td>\n");
    	if(arvRowNum == 0) {
    		builder.append("<span class=\"value-label\">TRAITEMENT INITIAL: </span>\n");
		if (drugRegimenInEmr)
    			builder.append(" <span class=\"value-data\">" + formatDate(FormatDate_General,changeDate) +  "</span>");
    	    builder.append("<br/>\n");
    		builder.append(UIHelper.getCheckBoxWidget(isABC_3TC_NVP, "ABC+3TC+NVP")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isABC_3TC_EFV, "ABC+3TC+EFV")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isAZT_3TC_NVP, "AZT+3TC+NVP")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isAZT_3TC_EFV, "AZT+3TC+EFV")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isD4T_3TC_NVP, "D4T+3TC+NVP")).append("\n");
    		builder.append("<br/>\n");
    		builder.append(UIHelper.getCheckBoxWidget(isD4T_3TC_EFV, "D4T+3TC+EFV")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isABC_3TC_LPVr, "ABC+3TC+LPV/r")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isAZT_3TC_LPVr, "AZT+3TC+LPV/r")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isD4T_3TC_LPVr, "D4T+3TC+LPV/r")).append("\n");
    		builder.append("<br/>\n");
    		builder.append(UIHelper.getCheckBoxWidget(isOther, "Autre (a specifier et inclure la dose): ")).append("\n");
    		if(drugRegimenInEmr) {
	    		builder.append("<span class=\"value-data\">").append(UIHelper.formatRegimenDisplaySummaryPedi(drugOrders)).append("</span>");
    		}
    	} else {
    		builder.append("<span class=\"value-label\">");
    		builder.append("CHANGEMENT DU TRAITEMENT (").append(arvRowNum).append(") &nbsp;&nbsp;&nbsp;Date ");
    		builder.append("</span>\n<span class=\"value-date\">");
        	if(drugOrders == null || drugOrders.size() == 0) {
        		builder.append(DateTextPlaceHolder);    		
        	} else {
        		builder.append(formatDate(FormatDate_General, changeDate));
        	}
    		builder.append("</span><br/>\n");
    		builder.append(UIHelper.getCheckBoxWidget(isABC_3TC_NVP, "ABC+3TC+NVP")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isABC_3TC_EFV, "ABC+3TC+EFV")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isAZT_3TC_NVP, "AZT+3TC+NVP")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isAZT_3TC_EFV, "AZT+3TC+EFV")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isD4T_3TC_NVP, "D4T+3TC+NVP")).append("\n");
    		builder.append("<br/>\n");
    		builder.append(UIHelper.getCheckBoxWidget(isD4T_3TC_EFV, "D4T+3TC+EFV")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isABC_3TC_LPVr, "ABC+3TC+LPV/r")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isAZT_3TC_LPVr, "AZT+3TC+LPV/r")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isD4T_3TC_LPVr, "D4T+3TC+LPV/r")).append("\n");
    		builder.append("<br/>\n");
    		builder.append(UIHelper.getCheckBoxWidget(isOther, "Autre (a specifier): ")).append("\n");
    		if(drugRegimenInEmr) {
	    		builder.append("<span class=\"value-data\">").append(UIHelper.formatRegimenDisplaySummaryPedi(drugOrders)).append("</span>");
    		}
    	}    		
    	builder.append("</td>\n");

    	// Is the drug stopped info in the EMR?
    	boolean drugStoppedInEmr = (nextChangeDate != null) ? true : false;

    	builder.append("<td class=\"section-emr\">");
    	if(drugStoppedInEmr) {
    		builder.append("&#x2713;");
    	}
    	builder.append("</td>\n");    		
    	
		builder.append("<td>\n");
		builder.append("<span class=\"value-label\">Date d'arret </span>");
		builder.append("\n<span class=\"value-date\">");
    	if(drugStoppedInEmr) {
    		builder.append(formatDate(FormatDate_General, nextChangeDate));
    	} else {
    		builder.append(DateTextPlaceHolder);    		
    	}
    	
    	boolean isRegimenFailure = drugHelper.isRegimenFailure(drugOrders);
    	boolean isINTERACTION_WITH_TUBERCULOSIS_DRUG = drugHelper.isINTERACTION_WITH_TUBERCULOSIS_DRUG(drugOrders);
    	boolean isToxicity = drugHelper.isToxicity(drugOrders);
    	boolean isPATIENT_PREGNANT = drugHelper.isPATIENT_PREGNANT(drugOrders);
    	boolean isPATIENT_DEFAULTED = drugHelper.isPATIENT_DEFAULTED(drugOrders);
    	boolean isDRUG_UNAVAILABLE = drugHelper.isDRUG_UNAVAILABLE(drugOrders);
    	boolean isPATIENT_DIED = drugHelper.isPATIENT_DIED(drugOrders);
		boolean isOtherStopReason = drugStoppedInEmr && !(isRegimenFailure ||
				isINTERACTION_WITH_TUBERCULOSIS_DRUG || isToxicity || isPATIENT_PREGNANT ||
				isPATIENT_DEFAULTED || isDRUG_UNAVAILABLE || isPATIENT_DIED);
    	
		builder.append("</span><span class=\"value-label\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; RAISON: </span><br/>\n");
		builder.append(UIHelper.getCheckBoxWidget(isRegimenFailure, "Echec")).append("\n");
		builder.append(UIHelper.getCheckBoxWidget(isINTERACTION_WITH_TUBERCULOSIS_DRUG, "Interaction avec les anti_TB")).append("\n");
		builder.append(UIHelper.getCheckBoxWidget(isToxicity, "Toxicite")).append("\n");
		builder.append(UIHelper.getCheckBoxWidget(isPATIENT_PREGNANT, "Grossesse")).append("\n");
		builder.append("<br/>\n");
		builder.append(UIHelper.getCheckBoxWidget(isPATIENT_DEFAULTED, "Abandonne")).append("\n");
		builder.append(UIHelper.getCheckBoxWidget(isDRUG_UNAVAILABLE, "Rupture de stock")).append("\n");
		builder.append(UIHelper.getCheckBoxWidget(isPATIENT_DIED, "deces")).append("\n");
		builder.append("<br/>\n");
		builder.append(UIHelper.getCheckBoxWidget(isOtherStopReason, "Autre: ")).append("\n");
    	if(drugStoppedInEmr) {
		builder.append("<span class=\"value-data\">");
    		int i = 0;
    		//SUPER HACK: old regimens have to run out, so we show discontinue reason for every drug with a stop date before nextChangeDate + 7 days
    		Calendar cal = Calendar.getInstance();
    		cal.setTime(nextChangeDate);
    		cal.add(Calendar.DAY_OF_MONTH, 7);
    		Date maxEndDate = cal.getTime();
    		
    		Set<String> reasons = new HashSet<String>();
    		for (DrugOrder dor :drugOrders){
    			if (Utils.getDiscontinuedReason(dor) != null 
    					 && (((dor.getEffectiveStopDate() != null && dor.getEffectiveStopDate().before(maxEndDate)) 
    							 ||   (dor.getAutoExpireDate() != null && dor.getAutoExpireDate().before(maxEndDate))))){
    				   
    				   reasons.add(Utils.getDiscontinuedReason(dor).getDisplayString());
    				   
    			}    				
    		}
    		for (String reason:reasons){
    			if (i > 0)
					   builder.append(", ");
				   builder.append(reason);
				   i++;
    		}
    		builder.append("</span>");    	
	}
    	builder.append("</td>\n");
    	
    	builder.append("</tr>\n");
    	arvRowNum++;
    }

	public List<ProphylaxisMapping> getProphylaxisEpisodes(){
    	Set<Concept> cSet = new HashSet<Concept>();
    	cSet.add(Context.getConceptService().getConcept(ConceptDictionary.DRUG_COTRIMOXAZOLE));
    	cSet.add(Context.getConceptService().getConcept(ConceptDictionary.DRUG_FLUCONAZOLE));
    	cSet.add(Context.getConceptService().getConcept(ConceptDictionary.DRUG_DAPSONE));
    	cSet.add(Context.getConceptService().getConcept(ConceptDictionary.DRUG_ISONIAZID));
    	List<DrugOrder> tmp = this.getAllPatientDrugOrdersByConceptList(cSet);
    	
    	List<ProphylaxisMapping> ret = new ArrayList<ProphylaxisMapping>();
    	for (DrugOrder dor : tmp){
    		ret.add(new ProphylaxisMapping(dor));
    		
    		//to easily exclude these drugOrders in the 'other drug orders' category
    		usedDrugOrderIds.add(dor.getOrderId());
    	}
    	//now add 3 blanks
    	ret.add(new ProphylaxisMapping());
    	ret.add(new ProphylaxisMapping());
    	ret.add(new ProphylaxisMapping());
    	return ret;
    }
    
    public Date getPositiveDiagnosisDate()
	{
		try {
			return (Date) getFirstObs(ConceptDictionary.HIV_DIAGNOSIS_DATE);
		} catch (Exception e) {
			log.error("Failed to retrieve positive diagnosis date", e);
		}
		return null;
	}
    
    public WHOMapping getWhoMapping()
    {
    	if(whoMapping == null)
    	{
    		whoMapping = new WHOMapping();
    	}
    	
    	return whoMapping;
    }
    
    public int getAllergyFormId()
    {
    	return ConceptDictionary.PEDI_ALLERGY_FORM;
    }
    
    public int getHospitalisationFormId()
    {
    	return ConceptDictionary.PEDI_HOSPITALISATION_FORM;
    }
    
   /* public int getOppInfectionFormId()
    {
    	return ConceptDictionary.PEDI_OI_FORM;
    }*/
    
    public int getProblemFormId()
    {
    	return ConceptDictionary.PEDI_PROBLEM_FORM;
    }
    
    public int getVisitFormId()
    {
    	return ConceptDictionary.PEDI_VISIT_FORM;
    }
    
    public int getLabFormId()
    {
    	return ConceptDictionary.PEDI_LAB_FORM;
    }
    
    public int getImageFormId()
    {
    	return ConceptDictionary.PEDI_IMAGE_FORM;
    }


	public boolean getIsPedi(){
    	return true;
    }
    public boolean getIsAdult(){
    	return false;
    }

	public int getPncFormId()
	{

		return ConceptDictionary.PNC_FORM;
	}
	public int getAncFormId()
	{

		return ConceptDictionary.ANC_FORM;
	}


	public List<Concept> getAllMedicalHistory()
	{
		try {
			List<List<Object>> conceptsObjectList=getObsWithValues(Context.getConceptService().getConcept(ConceptDictionary.MEDICAL_HISTORY),null);
			List<Concept> concepts=new ArrayList<Concept>();

			for(List<Object> o:conceptsObjectList){
				concepts.add((Concept) o.get(0));
			}
			if (concepts.size()>0)
				return concepts;
		} catch (Exception e) {
			log.error("Failed to retrieve MEDICAL_HISTORY", e);
		}
		return null;
	}

	public Date getLastMenstrualPeriodDate()
	{
		try {
			return (Date) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.LMP));
		} catch (Exception e) {
			log.error("Failed to retrieve LAST MENSTRUAL PERIOD date", e);
		}
		return null;
	}

	public Double getGravidity()
	{
		try {
			return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.GRAVIDITY));
		} catch (Exception e) {
			log.error("Failed to retrieve GRAVIDITY", e);
		}
		return null;
	}

	public  Concept getLastBloodGroup()
	{
		try {
			return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.BLOOD_GROUP));
		} catch (Exception e) {
			log.error("Failed to retrieve BLOOD_GROUP", e);
		}
		return null;
	}
	public  Concept getLastResultofHIVTest()
	{
		try {
			return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.HIV_RESULT));
		} catch (Exception e) {
			log.error("Failed to retrieve HIV_RESULT", e);
		}
		return null;
	}
	public  Concept getLastResultofHIVTestOfPartener()
	{
		try {
			return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.PARTNER_RESULT_OF_HIV_TEST));
		} catch (Exception e) {
			log.error("Failed to retrieve PARTNER_RESULT_OF_HIV_TEST", e);
		}
		return null;
	}

	public Date getExpectedDueDate()
	{
		try {
			return (Date) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.EXPECTED_DUE_DATE));
		} catch (Exception e) {
			log.error("Failed to retrieve EXPECTED_DUE_DATE date", e);
		}
		return null;
	}

	public Double getDurationOfPregnancy()
	{
		try {
			return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.DURATION_OF_PREGNANCY));
		} catch (Exception e) {
			log.error("Failed to retrieve DURATION_OF_PREGNANCY", e);
		}
		return null;
	}

	public Concept getPatientArrivedWithPartner()
	{
		try {
			return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.PATIENT_ARRIVED_WITH_PARTNER));
		} catch (Exception e) {
			log.error("Failed to retrieve DURATION_OF_PREGNANCY", e);
		}
		return null;
	}

	public Double getParity()
	{
		try {
			return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.PARITY));
		} catch (Exception e) {
			log.error("Failed to retrieve PARITY", e);
		}
		return null;
	}
	public Double getNumberOfAbortions()
	{
		try {
			return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.NUMBER_OF_ABORTIONS));
		} catch (Exception e) {
			log.error("Failed to retrieve NUMBER_OF_ABORTIONS", e);
		}
		return null;
	}
	public Double getNumberOfCesareanSection()
	{
		try {
			return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.NUMBER_OF_CESAREAN_SECTIONS));
		} catch (Exception e) {
			log.error("Failed to retrieve NUMBER_OF_CESAREAN_SECTIONS", e);
		}
		return null;
	}




	public List<Concept> getAllObsetricHistory()
	{
		try {
			List<List<Object>> conceptsObjectList=getObsWithValues(Context.getConceptService().getConcept(ConceptDictionary.OBSTETRIC_HISTORY),null);
			List<Concept> concepts=new ArrayList<Concept>();

			for(List<Object> o:conceptsObjectList){
				concepts.add((Concept) o.get(0));
			}
			if (concepts.size()>0)
				return concepts;
		} catch (Exception e) {
			log.error("Failed to retrieve OBSTETRIC_HISTORY", e);
		}
		return null;
	}


	public List<Concept> getAllLastCurrentProblems()
	{
		try {
			List<Form> forms=new ArrayList<Form>();
			forms.add(Context.getFormService().getForm(Integer.parseInt(Context.getAdministrationService().getGlobalProperty("mch.Form_ANC_Enrolment"))));
			List<Concept> concepts=new ArrayList<Concept>();
			List<Encounter> encounters=Context.getEncounterService().getEncounters(this.getPatient(),null,null,null,forms,null, null,null,null,false);
			if (encounters != null && encounters.size() > 0){
				//sorts descending
				Collections.sort(encounters,new Comparator<Encounter>() {
					public int compare(Encounter left, Encounter right) {
						Date now = new Date();
						long leftWeight = (left.getEncounterDatetime() == null) ? now.getTime() : left.getEncounterDatetime().getTime();
						long rightWeight = (right.getEncounterDatetime() == null) ? now.getTime() : right.getEncounterDatetime().getTime();
						if (leftWeight < rightWeight)
							return 1;
						return -1;
					}
				});

				for(Obs o:encounters.get(0).getAllObs()){
					if(o.getConcept()==Context.getConceptService().getConcept(ConceptDictionary.CURRENT_PROBLEMS)){
						concepts.add(o.getValueCoded());
					}
					System.out.println("Testtttttttttttttttttt: "+o.getValueCoded());
				}
			}
			return concepts;
		} catch (Exception e) {
			log.error("Failed to retrieve CURRENT_PROBLEMS", e);
		}
		return null;
	}


	public List<Concept> getAllLastDangerSignValues(){
    	return getAllLastValues(Context.getFormService().getForm(Integer.parseInt(Context.getAdministrationService().getGlobalProperty("mch.Form_ANC_Enrolment"))),ConceptDictionary.DANGER_SIGNS);
	}

	public Double getLastTemperature()
	{
		try {
			return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.TEMPERATURE));
		} catch (Exception e) {
			log.error("Failed to retrieve TEMPERATURE", e);
		}
		return null;
	}
	public Double getLastSBP()
	{
		try {
			return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.SYSTOLIC_BLOOD_PRESSURE));
		} catch (Exception e) {
			log.error("Failed to retrieve SYSTOLIC_BLOOD_PRESSURE", e);
		}
		return null;
	}
	public Double getLastHB()
	{
		try {
			return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.HEMOGLOBIN));
		} catch (Exception e) {
			log.error("Failed to retrieve HEMOGLOBIN", e);
		}
		return null;
	}
	public Double getLastDBP()
	{
		try {
			return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.DIASTOLIC_BLOOD_PRESSURE));
		} catch (Exception e) {
			log.error("Failed to retrieve DIASTOLIC_BLOOD_PRESSURE", e);
		}
		return null;
	}
	public Double getLastPulse()
	{
		try {
			return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.PULSE));
		} catch (Exception e) {
			log.error("Failed to retrieve PULSE", e);
		}
		return null;
	}

    public Concept getLastGenitalExamination()
    {
        try {

            return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.GENITAL_EXAMINATION));

        } catch (Exception e) {
            log.error("Failed to retrieve ABNORMAL GENITAL_EXAMINATION", e);
        }
        return null;
    }

    public Concept getLastIsBabyMoving()
    {
        try {
            return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.IS_BABY_MOVING));

        } catch (Exception e) {
            log.error("Failed to retrieve IS_BABY_MOVING", e);
        }
        return null;
    }


    public Concept getLastFundalHeightMatchGA()
    {
        try {
            return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.DOES_FUNDAL_HEIGHT_MATCH_GESTATIONAL_AGE));

        } catch (Exception e) {
            log.error("Failed to retrieve DOES_FUNDAL_HEIGHT_MATCH_GESTATIONAL_AGE", e);
        }
        return null;
    }


    public Concept getLastFetalPosition()
    {
        try {
            return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.FETAL_POSITION));

        } catch (Exception e) {
            log.error("Failed to retrieve FETAL_POSITION", e);
        }
        return null;
    }
    public Concept getLastFetalHeart()
    {
        try {
            return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.FETAL_HEART));

        } catch (Exception e) {
            log.error("Failed to retrieve FETAL_HEART", e);
        }
        return null;
    }

    public Concept getLastRespirationMovements()
    {
        try {
            return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.RESPIRATION_MOVEMENTS));

        } catch (Exception e) {
            log.error("Failed to retrieve RESPIRATION_MOVEMENTS", e);
        }
        return null;
    }

    public Concept getLastFetalTone()
    {
        try {
            return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.FETAL_TONE));

        } catch (Exception e) {
            log.error("Failed to retrieve FETAL_TONE", e);
        }
        return null;
    }

    public Concept getLastTetanusGiven()
    {
        try {
            return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.WHICH_TETANUS_WAS_GIVEN_AT_THIS_VISIT));

        } catch (Exception e) {
            log.error("Failed to retrieve WHICH_TETANUS_WAS_GIVEN_AT_THIS_VISIT", e);
        }
        return null;
    }
    public Concept getLastLungs()
    {
        try {
            return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.LUNGS));

        } catch (Exception e) {
            log.error("Failed to retrieve LUNGS", e);
        }
        return null;
    }
    public Concept getLastHeart()
    {
        try {
            return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.HEART));

        } catch (Exception e) {
            log.error("Failed to retrieve HEART", e);
        }
        return null;
    }

    public Concept getLastRPRResult()
    {
        try {
            return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.RAPID_PLASMA_REAGENT));

        } catch (Exception e) {
            log.error("Failed to retrieve RAPID_PLASMA_REAGENT", e);
        }
        return null;
    }

    public Concept getLastHIVResult()
    {
        try {
            return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.RESULT_OF_HIV_TEST));

        } catch (Exception e) {
            log.error("Failed to retrieve RESULT_OF_HIV_TEST", e);
        }
        return null;
    }

    public Concept getPositiveConcept()
    {
        return Context.getConceptService().getConcept(ConceptDictionary.POSITIVE);
    }
    public Concept getNormalConcept()
    {
        return Context.getConceptService().getConcept(ConceptDictionary.NORMAL);
    }

    public Concept getAbnormalConcept()
    {
            return Context.getConceptService().getConcept(ConceptDictionary.ABNORMAL);
    }
    public Concept getNoConcept()
    {
        return Context.getConceptService().getConcept(ConceptDictionary.NO);
    }


    public List<Concept> getAllLastValues(Form form,int conceptId)
	{
		try {
			List<Form> forms=new ArrayList<Form>();
			forms.add(form);
			List<Concept> concepts=new ArrayList<Concept>();
			List<Encounter> encounters=Context.getEncounterService().getEncounters(this.getPatient(),null,null,null,forms,null,null,null,null,false);
			if (encounters != null && encounters.size() > 0){
				//sorts descending
				Collections.sort(encounters,new Comparator<Encounter>() {
					public int compare(Encounter left, Encounter right) {
						Date now = new Date();
						long leftWeight = (left.getEncounterDatetime() == null) ? now.getTime() : left.getEncounterDatetime().getTime();
						long rightWeight = (right.getEncounterDatetime() == null) ? now.getTime() : right.getEncounterDatetime().getTime();
						if (leftWeight < rightWeight)
							return 1;
						return -1;
					}
				});

				for(Obs o:encounters.get(0).getAllObs()){
					if(o.getConcept()==Context.getConceptService().getConcept(conceptId)){
						concepts.add(o.getValueCoded());
					}
				}
			}
			return concepts;
		} catch (Exception e) {
			log.error("Failed to retrieve Value(s) With concept: "+ conceptId, e);
		}
		return null;
	}

    public Double getLastNumericValuesFromFom(String formUuid,int conceptId)
    {
        try {
            List<Form> forms=new ArrayList<Form>();
            forms.add(Context.getFormService().getFormByUuid(formUuid));
            Double lastValue=new Double(0);
            List<Encounter> encounters=Context.getEncounterService().getEncounters(this.getPatient(),null,null,null,forms,null,null,null,null,false);
            if (encounters != null && encounters.size() > 0){
                //sorts descending
                Collections.sort(encounters,new Comparator<Encounter>() {
                    public int compare(Encounter left, Encounter right) {
                        Date now = new Date();
                        long leftWeight = (left.getEncounterDatetime() == null) ? now.getTime() : left.getEncounterDatetime().getTime();
                        long rightWeight = (right.getEncounterDatetime() == null) ? now.getTime() : right.getEncounterDatetime().getTime();
                        if (leftWeight < rightWeight)
                            return 1;
                        return -1;
                    }
                });


                for(Obs o:encounters.get(0).getAllObs()){
                    if(o.getConcept()==Context.getConceptService().getConcept(conceptId)){
                        lastValue=o.getValueNumeric();
                    }
                }
            }
            return lastValue;
        } catch (Exception e) {
            log.error("Failed to retrieve Value(s) With concept: "+ conceptId, e);
        }
        return null;
    }

	public Concept getLastCodedValuesFromForm(String formUuid, int conceptId)
	{
		try {
			List<Form> forms=new ArrayList<Form>();
			forms.add(Context.getFormService().getFormByUuid(formUuid));
			Concept lastCodedValue=new Concept();
			List<Encounter> encounters=Context.getEncounterService().getEncounters(this.getPatient(),null,null,null,forms,null,null,null,null,false);
			if (encounters != null && encounters.size() > 0){
				//sorts descending
				Collections.sort(encounters,new Comparator<Encounter>() {
					public int compare(Encounter left, Encounter right) {
						Date now = new Date();
						long leftWeight = (left.getEncounterDatetime() == null) ? now.getTime() : left.getEncounterDatetime().getTime();
						long rightWeight = (right.getEncounterDatetime() == null) ? now.getTime() : right.getEncounterDatetime().getTime();
						if (leftWeight < rightWeight)
							return 1;
						return -1;
					}
				});


				for(Obs o:encounters.get(0).getAllObs()){
					if(o.getConcept()==Context.getConceptService().getConcept(conceptId)){
						lastCodedValue=o.getValueCoded();
					}
				}
			}
			return lastCodedValue;
		} catch (Exception e) {
			log.error("Failed to retrieve Value(s) With concept: "+ conceptId, e);
		}
		return null;
	}

	//Infant

    public String getInfantNames()
    {
        String names="";
        List<Relationship> relationships= Context.getPersonService().getRelationshipsByPerson(getPatient());
        for (Relationship r:relationships){
            if(r.getRelationshipType()==Context.getPersonService().getRelationshipType(9)){
             names=r.getPersonB().getGivenName()+ " "+r.getPersonB().getFamilyName();
            }
        }
        return names;
    }
    public Date getLastBirthDateBabyOne()
    {
        try {
            return (Date) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.DATE_OF_BIRTH_BABY_ONE));
        } catch (Exception e) {
            log.error("Failed to retrieve LAST DATE_OF_BIRTH_BABY_ONE PERIOD date", e);
        }
        return null;
    }
    public Double getBirthWeight()
    {
        try {
            return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.BIRTH_WEIGHT));
        } catch (Exception e) {
            log.error("Failed to retrieve BIRTH_WEIGHT", e);
        }
        return null;
    }

	public Double getBirthHeight()
	{
		try {
			return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.BIRTH_HEIGHT));
		} catch (Exception e) {
			log.error("Failed to retrieve BIRTH_WEIGHT", e);
		}
		return null;
	}

	public Concept getLastResultOfHIVOfBaby(){
    	// Delivery outcome and New Born Registration form
		return (Concept) getLastCodedValuesFromForm("d4483501-c58c-4117-af75-b4167d7f3d54",ConceptDictionary.RESULT_OF_HIV_TEST);
	}
	public Concept getLastRPROfBaby(){
		// Delivery outcome and New Born Registration form
		return getLastCodedValuesFromForm("d4483501-c58c-4117-af75-b4167d7f3d54",ConceptDictionary.RAPID_PLASMA_REAGENT);
	}
	public Double getLastMUACOfBaby(){
		try {
			return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.MIDDLE_UPPER_ARM_CIRCUMFERENCE));
		} catch (Exception e) {
			log.error("Failed to retrieve MIDDLE_UPPER_ARM_CIRCUMFERENCE", e);
		}
		return null;
	}
    public Concept getMuacLTTwenty()
    {
        try {
            return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.MIDDLE_UPPER_ARM_CIRCUMFERENCE_LESS_THAN_TWENTY));
        } catch (Exception e) {
            log.error("Failed to retrieve MIDDLE_UPPER_ARM_CIRCUMFERENCE_LESS_THAN_TWENTY", e);
        }
        return null;
    }
/*	public List<Concept> getAllLastHistoryOfDelivery(){
		return getAllLastValues(Context.getFormService().getForm(Integer.parseInt(Context.getAdministrationService().getGlobalProperty("mch.Form_PNC_0_24H"))).getUuid(),ConceptDictionary.HISTORY_OF_DELIVERY);
	}*/

	public List<Concept> getAllLastHistoryOfDelivery()
	{
		try {
			List<List<Object>> conceptsObjectList=getObsWithValues(Context.getConceptService().getConcept(ConceptDictionary.HISTORY_OF_DELIVERY),null);
			List<Concept> concepts=new ArrayList<Concept>();

			for(List<Object> o:conceptsObjectList){
				concepts.add((Concept) o.get(0));
			}
			if (concepts.size()>0)
				return concepts;
		} catch (Exception e) {
			log.error("Failed to retrieve HISTORY_OF_DELIVERY", e);
		}
		return null;
	}

	public List<Concept> getAllConditions()
	{
		try {
			List<List<Object>> conceptsObjectList=getObsWithValues(Context.getConceptService().getConcept(ConceptDictionary.VAGINAL_BLEEDING),null);
			List<Concept> concepts=new ArrayList<Concept>();

			for(List<Object> o:conceptsObjectList){
				concepts.add((Concept) o.get(0));
			}
			if (concepts.size()>0)
				return concepts;
		} catch (Exception e) {
			log.error("Failed to retrieve VAGINAL_BLEEDING", e);
		}
		return null;
	}

	public List<Concept> getAllConvulsions()
	{
		try {
			List<List<Object>> conceptsObjectList=getObsWithValues(Context.getConceptService().getConcept(ConceptDictionary.CONVULSIONS),null);
			List<Concept> concepts=new ArrayList<Concept>();

			for(List<Object> o:conceptsObjectList){
				concepts.add((Concept) o.get(0));
			}
			if (concepts.size()>0)
				return concepts;
		} catch (Exception e) {
			log.error("Failed to retrieve CONVULSIONS", e);
		}
		return null;
	}
	public List<Concept> getAllEclampsiainvestigation()
	{
		try {
			List<List<Object>> conceptsObjectList=getObsWithValues(Context.getConceptService().getConcept(ConceptDictionary.ECLAMPSIA_INVESTIGATION),null);
			List<Concept> concepts=new ArrayList<Concept>();

			for(List<Object> o:conceptsObjectList){
				concepts.add((Concept) o.get(0));
			}
			if (concepts.size()>0)
				return concepts;
		} catch (Exception e) {
			log.error("Failed to retrieve ECLAMPSIA_INVESTIGATION", e);
		}
		return null;
	}
	public List<Concept> getAllDiagnosis()
	{
		try {
			List<List<Object>> conceptsObjectList=getObsWithValues(Context.getConceptService().getConcept(ConceptDictionary.DANGER_SIGNS_FROM_HISTORY),null);
			List<Concept> concepts=new ArrayList<Concept>();

			for(List<Object> o:conceptsObjectList){
				concepts.add((Concept) o.get(0));
			}
			if (concepts.size()>0)
				return concepts;
		} catch (Exception e) {
			log.error("Failed to retrieve DANGER_SIGNS_FROM_HISTORY", e);
		}
		return null;
	}
	public Concept getLastApgar()
	{
		try {
			return (Concept) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.APGAR));

		} catch (Exception e) {
			log.error("Failed to retrieve APGAR", e);
		}
		return null;
	}
	public Date getLastDateStartedOnKMC()
	{
		try {
			return (Date) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.DATE_STARTED_ON_KMC));
		} catch (Exception e) {
			log.error("Failed to retrieve DATE_STARTED_ON_KMC date", e);
		}
		return null;
	}

	public Double getLastWeightStartingOnKMC()
	{
		try {
			return (Double) getLastObs(Context.getConceptService().getConcept(ConceptDictionary.WEIGHT_STARTING_ON_KMC));
		} catch (Exception e) {
			log.error("Failed to retrieve WEIGHT_STARTING_ON_KMC", e);
		}
		return null;
	}


	public List<Concept> getAllSeriousPatologies()
	{
		try {
			List<List<Object>> conceptsObjectList=getObsWithValues(Context.getConceptService().getConcept(ConceptDictionary.SERIOUS_PATOLOGIES),null);
			List<Concept> concepts=new ArrayList<Concept>();

			for(List<Object> o:conceptsObjectList){
				concepts.add((Concept) o.get(0));
			}
			if (concepts.size()>0)
				return concepts;
		} catch (Exception e) {
			log.error("Failed to retrieve SERIOUS_PATOLOGIES", e);
		}
		return null;
	}

	public Map<Date,Double> getWeightImprovement(){
		return getObsDateAndNumericValue(Context.getConceptService().getConcept(ConceptDictionary.WEIGHT_KG));
	}


    public Map<Date,Encounter> getIntrapartumFollowUpEnconters()
    {
       Map<Date,Encounter> encountersMap= new HashMap<Date,Encounter>();
        try {
            List<Form> forms=new ArrayList<Form>();
            forms.add(Context.getFormService().getForm(237));
            List<Concept> concepts=new ArrayList<Concept>();
            List<Encounter> encounters=Context.getEncounterService().getEncounters(this.getPatient(),null,null,null,forms,null,null,null,null,false);
            if (encounters != null && encounters.size() > 0){
                //sorts descending
                Collections.sort(encounters,new Comparator<Encounter>() {
                    public int compare(Encounter left, Encounter right) {
                        Date now = new Date();
                        long leftWeight = (left.getEncounterDatetime() == null) ? now.getTime() : left.getEncounterDatetime().getTime();
                        long rightWeight = (right.getEncounterDatetime() == null) ? now.getTime() : right.getEncounterDatetime().getTime();
                        if (leftWeight < rightWeight)
                            return 1;
                        return -1;
                    }
                });

                for(Encounter e:encounters){
                    encountersMap.put(e.getEncounterDatetime(),e);
                }
            }
            return encountersMap;
        } catch (Exception e) {
            log.error("Failed to retrieve Value(s) With Form: "+ Context.getFormService().getForm(237).getFormId(), e);
        }
        return null;
    }


	public PatientProgram getMCHProgram() {
		Program mchProgram = Context.getProgramWorkflowService().getProgram(25);
		if (mchProgram == null)
			throw new RuntimeException("Please configure the mapping in the global property for the hiv program.  The current value resolves to " + ConceptDictionary.MCH_PROGRAM);
		PatientProgram ret = null;
		if (mchProgram != null){
			List<PatientProgram> ppList = Context.getProgramWorkflowService().getPatientPrograms(this.getPatient(), mchProgram, null, null, null, null, false);
			if (ppList != null && ppList.size() > 0){
				//sorts descending
				Collections.sort(ppList,new Comparator<PatientProgram>() {
					public int compare(PatientProgram left, PatientProgram right) {
						Date now = new Date();
						long leftWeight = (left.getDateEnrolled() == null) ? now.getTime() : left.getDateEnrolled().getTime();
						long rightWeight = (right.getDateEnrolled() == null) ? now.getTime() : right.getDateEnrolled().getTime();
						if (leftWeight < rightWeight)
							return 1;
						return -1;
					}
				});
				return ppList.get(0);
			}
		}
		return ret;
	}



	public Map<Obs,PatientState> getEpiVaccinesRecord()
	{
		Map<Obs,PatientState> stateObsMap= new HashMap<Obs,PatientState>();
		try {

			List<PatientState> states= new ArrayList<PatientState>();

			PatientProgram program = getMCHProgram();
			List<Person> people=new ArrayList<Person>();
			people.add(this.getPatient());
			List<Concept> conceptList=new ArrayList<Concept>();

			conceptList.add(Context.getConceptService().getConcept(106943));
			conceptList.add(Context.getConceptService().getConcept(106944));
			conceptList.add(Context.getConceptService().getConcept(106945));
			conceptList.add(Context.getConceptService().getConcept(106946));
			conceptList.add(Context.getConceptService().getConcept(106947));
			conceptList.add(Context.getConceptService().getConcept(106948));
			conceptList.add(Context.getConceptService().getConcept(106949));






			List<Obs> obsList= Context.getObsService().getObservations(people,null,conceptList,null,null,null,null,null,null,null,null,false);
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			if(program!=null){
				for(PatientState ps:program.getStates()){
					if (ps.getState().getProgramWorkflow().getProgramWorkflowId()==60){
						for (Obs ob:obsList){
							if(formatter.format(ob.getObsDatetime()).equals(formatter.format(ps.getStartDate())) && ob.getConcept()==ps.getState().getConcept()){
								stateObsMap.put(ob,ps);
							}
						}
					}
				}
			}


/*
			List<Form> forms=new ArrayList<Form>();
			forms.add(Context.getFormService().getForm(237));
			List<Concept> concepts=new ArrayList<Concept>();
			List<Encounter> encounters=Context.getEncounterService().getEncounters(this.getPatient(),null,null,null,forms,null,false);
			if (encounters != null && encounters.size() > 0){
				//sorts descending
				Collections.sort(encounters,new Comparator<Encounter>() {
					public int compare(Encounter left, Encounter right) {
						Date now = new Date();
						long leftWeight = (left.getEncounterDatetime() == null) ? now.getTime() : left.getEncounterDatetime().getTime();
						long rightWeight = (right.getEncounterDatetime() == null) ? now.getTime() : right.getEncounterDatetime().getTime();
						if (leftWeight < rightWeight)
							return 1;
						return -1;
					}
				});

				for(Encounter e:encounters){
					encountersMap.put(e.getEncounterDatetime(),e);
				}
			}*/
			return stateObsMap;
		} catch (Exception e) {
			log.error("Failed to retrieve Value(s) With Form: "+ Context.getFormService().getForm(237).getFormId(), e);
		}
		return null;
	}



}
