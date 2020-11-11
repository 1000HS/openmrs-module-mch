package org.openmrs.module.mch.web.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.context.Context;
import org.openmrs.module.mch.impl.pih.ConceptDictionary;
import org.openmrs.module.mch.impl.pih.HIVFlowsheetObsMapper;
import org.openmrs.module.mch.impl.pih.TbEpisodeMapping;

public class FlowsheetFormData extends FormDataModel {
	
	public static final String FormatDate_General = "dd/MM/yyyy";
	public static final String DateTextPlaceHolder = "__/__/____";
	public List<Integer> usedDrugOrderIds = new ArrayList<Integer>();
	
	protected HIVFlowsheetObsMapper obsMapper;

	public FlowsheetFormData(Patient p) {
		setPatient(p);
		obsMapper = new HIVFlowsheetObsMapper(p);
	}
	
	public HIVFlowsheetObsMapper getObsMapper() {
		return obsMapper;
	}

	public String getTracnetId() {
		PatientIdentifier identifier = getPatient().getPatientIdentifier(ConceptDictionary.PatientIdentiferId_Tracnet);
		if(identifier != null)
			return identifier.getIdentifier();
		return "";
	}
	
	public String getHealthCenter() {
		try{
			return getPersonAttribute("Health Center", "Location", "locationId", "name", false).toString();
		} catch (Exception ex){
			return "";
		}
	}
	
	public Date getArvStartDate() {
		return getEarliestDrugStart(ConceptDictionary.ANTIRETROVIRAL_DRUGS);
	}
	
	public List<DrugOrder> getTbDrugOrders() {
		//are these already chronological asc? yes, they should be according to patientSetService
		List<DrugOrder> tbDrugOrders = getDrugOrders(ConceptDictionary.TUBERCULOSIS_TREATMENT_DRUGS);
		
		return tbDrugOrders;
	}
		
	//returns the tb episodes:
	public List<TbEpisodeMapping> getTuberculosisEpisodes(){
		List<TbEpisodeMapping> episodes = new ArrayList<TbEpisodeMapping>();
		//load the list of all TB-related drug orders:		
		List<DrugOrder> tbOrders = getTbDrugOrders();
		
		//keeps track of already assigned drugOrders
		List<Integer> usedList = new ArrayList<Integer>();
		
		//first pass:  build full episodes
		//NOTE:  these are ascending.
		for (DrugOrder dor : tbOrders){
			if (!usedList.contains(dor.getOrderId()) && (dor.getConcept().getConceptId().equals(ConceptDictionary.TB_DRUG_RHEZ)
					|| dor.getConcept().getConceptId().equals(ConceptDictionary.TB_DRUG_RHZ)
							|| dor.getConcept().getConceptId().equals(ConceptDictionary.TB_DRUG_RHE))){
				//found a first line regimen :  RHEZ, RHE, or RHZ	
					TbEpisodeMapping tem = new TbEpisodeMapping();
					tem.getTbDrugOrders().add(dor);
					usedList.add(dor.getOrderId());
					Date startDate = dor.getEffectiveStartDate();
					Calendar cal = Calendar.getInstance();
					cal.setTime(startDate);
					cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf((365/2 + 30)));
					Date endDate = cal.getTime();
					System.out.println("Adult HIV Flowsheet Date Range Check.  Regimen Start Date " + startDate + " Regimen End Date " + endDate);
					for (DrugOrder dorInner : tbOrders){
						//found a drug with equal start date or a start date within next 7 months:
						//add this to drugOrderList in the same tb episode
						if (!usedList.contains(dorInner.getOrderId()) && !dorInner.getConcept().getConceptId().equals(ConceptDictionary.TB_DRUG_AMOX_CLAV) 
								&& dorInner.getEffectiveStartDate().getTime() >= startDate.getTime() 
								&& dorInner.getEffectiveStartDate().getTime() <= endDate.getTime()){
							   //special handling for RHEZ, just in case there was data entry error -- this will always be initial phase
								if (dorInner.getConcept().getConceptId().equals(ConceptDictionary.TB_DRUG_RHEZ) && dorInner.getEffectiveStartDate().equals(dor.getEffectiveStartDate())){
									//this will always be initial phase
									tem.getTbDrugOrders().add(0, dorInner);
									usedList.add(dorInner.getOrderId());
								} else {  //throw in all drugs in the episode
			
									tem.getTbDrugOrders().add(dorInner);
									usedList.add(dorInner.getOrderId());
								}
							
						} 
					}
					episodes.add(tem);
			}
		}
		
		//now each stray drug order gets its own row
		for (DrugOrder dor : tbOrders){
			if (!usedList.contains(dor.getOrderId()) && !dor.getConcept().getConceptId().equals(ConceptDictionary.TB_DRUG_AMOX_CLAV)){
				TbEpisodeMapping tem = new TbEpisodeMapping();
				tem.getTbDrugOrders().add(dor);
				usedList.add(dor.getOrderId());
				episodes.add(tem);
			}
		}
		//reorder the episodes
		Collections.sort(episodes);
		

		List<Concept> cList = new ArrayList<Concept>();
		cList.add(Context.getConceptService().getConcept(Integer.valueOf(ConceptDictionary.TB_TREATMENT_TYPE)));
		cList.add(Context.getConceptService().getConcept(Integer.valueOf(ConceptDictionary.RESULT_OF_TB_SMEAR)));
		//load all matching obs for this patient
		List<Obs> oList = Context.getObsService().getObservations(Collections.singletonList(Context.getPersonService().getPerson(this.getPatient().getPatientId())), null, cList, null, null, null, null, null, null, null, null, false);
		for (TbEpisodeMapping tem : episodes){
			Date startDate = tem.getEpisodeDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			//creating a buffer around episode start date to look for obs for episdode
			cal.add(Calendar.DAY_OF_MONTH, -15);
			startDate = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, +40);
			Date endDate = cal.getTime();
			for (Obs o : oList){
				if (o.getConcept().getConceptId().equals(ConceptDictionary.TB_TREATMENT_TYPE) &&
						o.getObsDatetime().getTime() >= startDate.getTime()  && o.getObsDatetime().getTime() <= endDate.getTime()){
					tem.setSchema(o);
					break;
				}
					
			}
			for (Obs o : oList){
				if (o.getConcept().getConceptId().equals(ConceptDictionary.RESULT_OF_TB_SMEAR) &&
						o.getObsDatetime().getTime() >= startDate.getTime()  && o.getObsDatetime().getTime() <= endDate.getTime()){
					tem.setSmearResult(o);
					break;
				}
					
			}
			
			for (Obs o :oList){
				try {
				if (o.getConcept().getConceptId().equals(ConceptDictionary.RESULT_OF_TB_SMEAR) && o.getValueCoded() != null
						&& o.getValueCoded().equals(ConceptDictionary.RESULT_OF_TB_SMEAR_NEGATIVE) && o.getObsDatetime().getTime() >= startDate.getTime() && 
						o.getObsDatetime().getTime() <= tem.getEpisodeEnd().getTime()){
						tem.setFirstNegativeSmear(o);
						break;
				}
				} catch (Exception ex){
					//pass, probably a null pointer exception
				}
			}
		}
		//add two empty items, if needed:

    		episodes.add(new TbEpisodeMapping());
    		episodes.add(new TbEpisodeMapping());
    	
    	//add to usedDrugOrderIds so that we can exclude these easily from 'other drugs'
    	for (TbEpisodeMapping tem : episodes){
    		List<DrugOrder> dors = tem.getTbDrugOrders();
    		if (dors != null){
	    		for (DrugOrder dor: dors){
	    			if (dor.getOrderId() != null)
	    				usedDrugOrderIds.add(dor.getOrderId());
	    		}
    		}
    	}	
		return episodes;
	}
    
    /**
     * we need this to return every hiv regien change date, and all drugOrders active on that date, so that we get a reflection of the regimen
     * @param rawOrders
     * @return
     */
    protected Map<Date, Set<DrugOrder>> getRegimenChanges(List<DrugOrder> rawOrders){
    	Map<Date, Set<DrugOrder>> ret = new TreeMap<Date, Set<DrugOrder>>(); //TreeSet will automatically sort dates
    	
    	for (DrugOrder dor : rawOrders){
    		//drug start dates:
    		Set<DrugOrder> dorListOnStartDate = ret.get(dor.getEffectiveStartDate());
    		if (dorListOnStartDate == null)
    			dorListOnStartDate = new LinkedHashSet<DrugOrder>();
    		//now check all orders to see if they're active on that date also, and add if so:
    		for (DrugOrder dorInner : rawOrders){
    			if (dorInner.isActive(dor.getEffectiveStartDate()) && !dorListOnStartDate.contains(dorInner)) //only add each DrugOrder once.
    				dorListOnStartDate.add(dorInner);
    		}
    		if (dorListOnStartDate.size() > 0)
    			ret.put(dor.getEffectiveStartDate(), dorListOnStartDate);
    		
    		Date endDate = dor.getEffectiveStartDate();
    		if (endDate == null)
    			endDate = dor.getAutoExpireDate();
    		
    		if (endDate != null){
	    		Set<DrugOrder> dorListOnEndDate = ret.get(endDate);
	    		if (dorListOnEndDate == null)
	    			dorListOnEndDate = new LinkedHashSet<DrugOrder>();
	    		//now check all orders to see if they're active on that date also, and add if so:
	    		for (DrugOrder dorInner : rawOrders){
	    			if (dorInner.isActive(endDate) && !dorListOnStartDate.contains(dorInner)) //only add each DrugOrder once.
	    					dorListOnEndDate.add(dorInner);
	    		}
	    		ret.put(endDate, dorListOnEndDate);
    		}
    		
    	}
    	
    	return ret;
    }
    
    //returns the model for the other medications section
    public List<DrugOrder> getOtherMedications(){
    	List<DrugOrder> ret = new ArrayList<DrugOrder>();
    	List<DrugOrder> dors = this.getAllPatientDrugOrders();
    	
    	for (DrugOrder dor : dors){
    		if (!usedDrugOrderIds.contains(dor.getOrderId())){
    			if (dor.getEffectiveStartDate() == null)
	    			continue;
	    		Date endDate = dor.getEffectiveStopDate();
	    		if (endDate == null)
	    			endDate = dor.getAutoExpireDate();
	    		if (endDate == null || ((endDate != null && endDate.getTime() - dor.getEffectiveStartDate().getTime() > (1000*60*60*24*30)))) // regimen that lasted more than 30 days, or there is no endDate specified
	    			ret.add(dor);
    		}
    	}
    	//add 4 extras:
    	ret.add(new DrugOrder());
    	ret.add(new DrugOrder());
    	ret.add(new DrugOrder());
    	ret.add(new DrugOrder());
    	return ret;
    }
}
