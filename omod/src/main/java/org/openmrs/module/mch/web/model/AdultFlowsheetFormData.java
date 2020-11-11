package org.openmrs.module.mch.web.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.PatientState;
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.module.mch.impl.pih.ConceptDictionary;
import org.openmrs.module.mch.impl.pih.ProphylaxisMapping;
import org.openmrs.module.mch.regimen.RegimenDrugHelper;
import org.openmrs.module.mch.utils.Utils;
import org.openmrs.module.mch.web.UIHelper;

public class AdultFlowsheetFormData extends FlowsheetFormData {

	public AdultFlowsheetFormData(Patient p) {
		super(p);
	}
			
	public String getArvTreatmentGroup(){
		PatientProgram pp = getAdultHIVProgram();
		
		PatientState patientState = null;
		if (pp != null){
			for (PatientState ps : pp.getStates()){
				if (ps.getState().getProgramWorkflow().getConcept().getConceptId().equals(ConceptDictionary.TREATMENT_GROUP))
				{
					if(patientState == null || patientState.isVoided() || !patientState.getActive())
					{
						patientState = ps;
					}
				}
			}
			
			if(patientState != null)
			{
				return patientState.getState().getConcept().getDisplayString();
			}
		}
		return "";
	}
	
	public PatientProgram getAdultHIVProgram() {
		Program hivProgram = Context.getProgramWorkflowService().getProgram(ConceptDictionary.ProgramId_AdultHIVProgram);
		if (hivProgram == null)
			return null;
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
	
	private int arvRowNum = 0;
	public String getArvRegimenHtmlTable() {
    	StringBuilder builder = new StringBuilder(4096);
    	builder.append("<table class=\"section-table\">\n");
    	builder.append("<tr>\n");
    	builder.append("<th scope=\"col\" id=\"col-arv-emr-1\">EMR</th>\n");
    	builder.append("<th scope=\"col\" id=\"col-arv-info\">TRAITEMENT ANTIRETROVIRAUX POUR TOUTE LA VIE</th>\n");
    	builder.append("<th scope=\"col\" id=\"col-arv-emr-2\">EMR</th>\n");
    	builder.append("<th scope=\"col\" id=\"col-arv-stop\"></th>\n");
    	builder.append("</tr>\n");

    	Map<Date,Set<DrugOrder>> drugOrdersGroupedByStartAndEndDate = getRegimenChanges(this.getDrugOrders(ConceptDictionary.ANTIRETROVIRAL_DRUGS));
		int index = 0;
		Map.Entry<Date, Set<DrugOrder>> prevMapEntry = null;
		for (Map.Entry<Date, Set<DrugOrder>> e : drugOrdersGroupedByStartAndEndDate.entrySet()){
			if (index > 0 && prevMapEntry.getValue().size() > 0)
				addARVRegimenTableRow(builder,  new ArrayList<DrugOrder>(prevMapEntry.getValue()), prevMapEntry.getKey(), e.getKey());
			if (index == drugOrdersGroupedByStartAndEndDate.size() -1 && e.getValue().size() > 0)
				addARVRegimenTableRow(builder,  new ArrayList<DrugOrder>(e.getValue()), e.getKey(), null);
			index ++;
			prevMapEntry = e;
			
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
		builder.append("</table>\n");
		
		return builder.toString();
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
		boolean isD4T_3TC_NVP = drugHelper.isD4T_3TC_NVP(drugOrders);
		boolean isD4T_3TC_EFV = drugHelper.isD4T_3TC_EFV(drugOrders);
		boolean isAZT_3TC_NVP = drugHelper.isAZT_3TC_NVP(drugOrders);
		boolean isAZT_3TC_EFV = drugHelper.isAZT_3TC_EFV(drugOrders);
		boolean isTDF_3TC_NVP = drugHelper.isTDF_3TC_NVP(drugOrders);
		boolean isTDF_3TC_EFV = drugHelper.isTDF_3TC_EFV(drugOrders);
		boolean isTDF_3TC_Kaletra = drugHelper.isTDF_3TC_Kaletra(drugOrders);
		boolean isABC_3TC_Kaletra = drugHelper.isABC_3TC_Kaletra(drugOrders);
		boolean isDDI_ABC_Kaletra = drugHelper.isDDI_ABC_Kaletra(drugOrders);
		boolean isAZT_3TC_Kaletra = drugHelper.isAZT_3TC_Kaletra(drugOrders);
		
		boolean isOther1 = drugRegimenInEmr && !(isD4T_3TC_NVP || isD4T_3TC_EFV || isAZT_3TC_NVP ||
				isAZT_3TC_EFV || isTDF_3TC_NVP || isTDF_3TC_EFV);
		boolean isOther2 = drugRegimenInEmr && !(isAZT_3TC_NVP || isAZT_3TC_EFV || 
				isTDF_3TC_Kaletra || isABC_3TC_Kaletra || isDDI_ABC_Kaletra || isAZT_3TC_Kaletra);

		builder.append("<td>\n");
    	if(arvRowNum == 0) {
    		builder.append("<span class=\"value-label\">TRAITEMENT INITIAL: </span>"); 
    		if (drugRegimenInEmr)
    			builder.append(" <span class=\"value-data\">(" + formatDate(FormatDate_General,changeDate) +  ")</span>");
    	    builder.append("<br/>\n");
    		builder.append(UIHelper.getCheckBoxWidget(isD4T_3TC_NVP, "D4T+3TC+NVP")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isD4T_3TC_EFV, "D4T+3TC+EFV")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isAZT_3TC_NVP, "AZT+3TC+NVP")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isAZT_3TC_EFV, "AZT+3TC+EFV")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isTDF_3TC_NVP, "TDF+3TC+NVP")).append("\n");
    		builder.append("<br/>\n");
    		builder.append(UIHelper.getCheckBoxWidget(isTDF_3TC_EFV, "TDF+3TC+EFV")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isOther1, "Autre (a specifier et inclure la dose): ")).append("\n");
    		if(drugRegimenInEmr) {
	    		builder.append("<span class=\"value-data\">").append(UIHelper.formatRegimenDisplaySummary(drugOrders)).append("</span>");
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
    		builder.append(UIHelper.getCheckBoxWidget(isAZT_3TC_NVP, "AZT+3TC+NVP")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isAZT_3TC_EFV, "AZT+3TC+EFV")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isTDF_3TC_Kaletra, "TDF+3TC+Kaletra")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isABC_3TC_Kaletra, "ABC+3TC+Kaletra")).append("\n");
    		builder.append("<br/>\n");
    		builder.append(UIHelper.getCheckBoxWidget(isDDI_ABC_Kaletra, "DDI+ABC+Kaletra")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isAZT_3TC_Kaletra, "AZT+3TC+Kaletra")).append("\n");
    		builder.append(UIHelper.getCheckBoxWidget(isOther2, "Autre: ")).append("\n");
    		if(drugRegimenInEmr) {
	    		builder.append("<span class=\"value-data\">").append(UIHelper.formatRegimenDisplaySummary(drugOrders)).append("</span>");
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
    	if(drugStoppedInEmr ) {
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
		builder.append(UIHelper.getCheckBoxWidget(isINTERACTION_WITH_TUBERCULOSIS_DRUG, "Interaction avec medicaments TB")).append("\n");
		builder.append(UIHelper.getCheckBoxWidget(isToxicity, "Toxicite")).append("\n");
		builder.append(UIHelper.getCheckBoxWidget(isPATIENT_PREGNANT, "Grossesse")).append("\n");
		builder.append("<br/>\n");
		builder.append(UIHelper.getCheckBoxWidget(isPATIENT_DEFAULTED, "Abandonne")).append("\n");
		builder.append(UIHelper.getCheckBoxWidget(isDRUG_UNAVAILABLE, "Rupture de stock")).append("\n");
		builder.append(UIHelper.getCheckBoxWidget(isPATIENT_DIED, "Deces")).append("\n");
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
		//<lookup complexExpression="#if($fn.getDrugOrders(''))#set($drugOrders = $fn.getDrugOrders('ANTIRETROVIRAL DRUGS'))#foreach($drugOrder in $drugOrders) &lt;tr&gt; &lt;td align=&quot;center&quot;&gt; &#x2713; &lt;/td&gt;     &lt;td&gt; Commencer: $!{fn.formatDate('$FormatDate_General', $drugOrder.getStartDate())} &lt;br/&gt;$!{drugOrder.getDrug().getName()} $!{drugOrder.getDose()}$!{drugOrder.getUnits().replaceAll('tab\(s\)', 'ce')} $!{drugOrder.getFrequency().replaceAll(' x 7 days/week', '').replaceAll('day', 'j')}&lt;/td&gt; #if($drugOrder.getDiscontinued())&lt;td align=&quot;center&quot;&gt; &#x2713; &lt;/td&gt;  &lt;td&gt; Arret: $!{fn.formatDate('$FormatDate_General', $drugOrder.getDiscontinuedDate())} &lt;br/&gt;Raison: $!{drugOrder.getDiscontinuedReason().getName()} &lt;/td&gt; #else &lt;td&gt;  &lt;/td&gt;  &lt;td&gt; Arret: $DateTextPlaceHolder &lt;br/&gt;Raison:  &lt;/td&gt; #end &lt;/tr&gt;#end#end" />
    }

    public List<ProphylaxisMapping> getProphylaxisEpisodes(){
    	Set<Concept> cSet = new HashSet<Concept>();
    	cSet.add(Context.getConceptService().getConcept(ConceptDictionary.DRUG_COTRIMOXAZOLE));
    	cSet.add(Context.getConceptService().getConcept(ConceptDictionary.DRUG_FLUCONAZOLE));
    	cSet.add(Context.getConceptService().getConcept(ConceptDictionary.DRUG_DAPSONE));
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
    
    public int getAllergyFormId()
    {
    	return ConceptDictionary.ADULT_ALLERGY_FORM;
    }
    
    public int getHospitalisationFormId()
    {
    	return ConceptDictionary.ADULT_HOSPITALISATION_FORM;
    }
    
    public int getOppInfectionFormId()
    {
    	return ConceptDictionary.ADULT_OI_FORM;
    }
    
    public int getProblemFormId()
    {
    	return ConceptDictionary.ADULT_PROBLEM_FORM;
    }
    
    public int getVisitFormId()
    {
    	return ConceptDictionary.ADULT_VISIT_FORM;
    }
    
    public int getLabFormId()
    {
    	return ConceptDictionary.ADULT_LAB_FORM;
    }
    
    public int getImageFormId()
    {
    	return ConceptDictionary.ADULT_IMAGE_FORM;
    }
    public boolean getIsPedi(){
    	return false;
    }
    public boolean getIsAdult(){
    	return true;
    }
}
