package org.openmrs.module.mch.impl.pih;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openmrs.DrugOrder;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.module.mch.utils.Utils;

public class TbEpisodeMapping implements Comparable<TbEpisodeMapping>{

	private Obs smearResult;
	private Obs schema;
	private List<DrugOrder> tbDrugOrders = new ArrayList<DrugOrder>();
	private Obs firstNegativeSmear;


	public TbEpisodeMapping (){}
	
	public DrugOrder getContinuationPhaseDrugOrder(){
		if(tbDrugOrders.size() == 1 && tbDrugOrders.get(0).isDiscontinued(null))
		{
			return tbDrugOrders.get(0);
		}
		
		Date initialDate = null;
		for (DrugOrder dor : tbDrugOrders){
			if (initialDate == null)
				initialDate = dor.getEffectiveStartDate();
			else if (dor.getEffectiveStartDate().getTime() > initialDate.getTime() && (
					dor.getConcept().getConceptId().equals(ConceptDictionary.TB_DRUG_RH) 
					|| dor.getConcept().getConceptId().equals(ConceptDictionary.TB_DRUG_RHE) )
					|| dor.getConcept().getConceptId().equals(ConceptDictionary.TB_DRUG_RHZ))
				return dor;
		}
		
		return null;
	}
	
	public Obs getSchema() {
		return schema;
	}

	public void setSchema(Obs schema) {
		this.schema = schema;
	}


	public void setSmearResult(Obs smearResult) {
		this.smearResult = smearResult;
	}
	

	public boolean isEmr() {
		if (tbDrugOrders.size() > 0)
			return true;
		return false;
	}



	public Date getEpisodeDate() {
		if (tbDrugOrders.size() > 0)
			return tbDrugOrders.get(0).getEffectiveStartDate();
		return null;
	}

	public Date getEpisodeEnd(){
		if (tbDrugOrders.size() > 0){
			Calendar cal = Calendar.getInstance();
			cal.setTime(tbDrugOrders.get(0).getEffectiveStartDate());
			cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf((356/2 + 30)));
			return cal.getTime();
		}	
		return null;
	}

	public DrugOrder getInitialDrugOrder() {
		if (tbDrugOrders.size() > 0)
			return tbDrugOrders.get(0);
		return null;
	}


	public Obs getSmearResult() {
		return this.smearResult;
	}


	public Obs getSchemaTherapeutique() {
		return this.schema;
	}

	@Override
	public int compareTo(TbEpisodeMapping obj) {
		if (this == obj)
			return 0;
		if (obj == null)
			return -1;
		if(getEpisodeDate() == null && obj.getEpisodeDate() == null)
			return 0;
		if(getEpisodeDate() == null)
			return 1;
		return getEpisodeDate().compareTo(obj.getEpisodeDate());
	}

	public List<DrugOrder> getTbDrugOrders() {
		return tbDrugOrders;
	}

	public void setTbDrugOrders(List<DrugOrder> tbDrugOrders) {
		this.tbDrugOrders = tbDrugOrders;
	}

	public boolean isSmearPositive(){
		if (this.getSmearResult() != null && this.getSmearResult().getValueCoded() != null &&
				(this.getSmearResult().getValueCoded().getConceptId().equals(ConceptDictionary.RESULT_OF_TB_SMEAR_POSITIVE)
						|| this.getSmearResult().getValueCoded().getConceptId().equals(ConceptDictionary.RESULT_OF_TB_SMEAR_WEAKLY_POSITIVE)
						|| this.getSmearResult().getValueCoded().getConceptId().equals(ConceptDictionary.RESULT_OF_TB_SMEAR_MODERATELY_POSITIVE)
						|| this.getSmearResult().getValueCoded().getConceptId().equals(ConceptDictionary.RESULT_OF_TB_SMEAR_STRONGLY_POSITIVE)))
			return true;
		return false;	
	}
	
	public boolean isSmearNegative(){
		if (this.getSmearResult() != null && this.getSmearResult().getValueCoded() != null &&
				this.getSmearResult().getValueCoded().getConceptId().equals(ConceptDictionary.RESULT_OF_TB_SMEAR_NEGATIVE))
			return true;
		return false;
	}
	
	public boolean isInitialTreatment(){
		if (this.getSchema() != null && this.getSchema().getValueCoded() != null &&
				this.getSchema().getValueCoded().getConceptId().equals(ConceptDictionary.TB_TREATMENT_TYPE_INITIAL))
			return true;
		return false;
	}
	
	public boolean isRetreatment(){
		if (this.getSchema() != null && this.getSchema().getValueCoded() != null &&
				this.getSchema().getValueCoded().getConceptId().equals(ConceptDictionary.TB_TREATMENT_TYPE_RETREATMENT))
			return true;
		return false;
	}
	
	public boolean isMdrtb(){
		if (this.getSchema() != null && this.getSchema().getValueCoded() != null &&
				this.getSchema().getValueCoded().getConceptId().equals(ConceptDictionary.TB_TREATMENT_TYPE_MDRTB))
			return true;
		return false;
	}


	public Obs getFirstNegativeSmear() {
		return firstNegativeSmear;
	}


	public void setFirstNegativeSmear(Obs firstNegativeSmear) {
		this.firstNegativeSmear = firstNegativeSmear;
	}
	
	
	public boolean isCured() {
		if (this.getContinuationPhaseDrugOrder() != null &&
			Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder())!= null
			&& Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder()).getConceptId().equals(ConceptDictionary.TB_TREATMENT_OUTCOME_GUERI_CURED)){
			return true;
	}
		return false;
	}
	
	public boolean isFailed(){
		if (this.getContinuationPhaseDrugOrder() != null &&
				Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder())!= null
				&& Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder()).getConceptId().equals(ConceptDictionary.TB_TREATMENT_OUTCOME_ECHEC_FAILED)){
				return true;
		}
			return false;
	}
	public boolean isComplete(){
		if (this.getContinuationPhaseDrugOrder() != null &&
				Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder())!= null
				&& Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder()).getConceptId().equals(ConceptDictionary.TB_TREATMENT_OUTCOME_COMPLETE)){
				return true;
		}
			return false;
	}
	public boolean isAbandoned(){
		if (this.getContinuationPhaseDrugOrder() != null &&
				Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder())!= null
				&& Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder()).getConceptId().equals(ConceptDictionary.TB_TREATMENT_OUTCOME_ABAONDONED)){
				return true;
		}
			return false;
	}
	public boolean isTransfered(){
		if (this.getContinuationPhaseDrugOrder() != null &&
				Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder())!= null
				&& Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder()).getConceptId().equals(ConceptDictionary.TB_TREATMENT_OUTCOME_TRANSFERED)){
				return true;
		}
			return false;
	}
	public boolean isDead(){
		if (this.getContinuationPhaseDrugOrder() != null &&
				Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder())!= null
				&& Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder()).getConceptId().equals(ConceptDictionary.TB_TREATMENT_OUTCOME_DIED)){
				return true;
		}
			return false;
	}
	public boolean isOther(){
		if (this.getContinuationPhaseDrugOrder() != null &&
				Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder())!= null
				&& !isCured() && !isFailed() && !isComplete() && !isAbandoned() && !isTransfered() && !isDead()){
				return true;
		}
			return false;
	}
	public String getOutcomeOther(){
		if (this.getContinuationPhaseDrugOrder() != null &&
				Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder())!= null
				&& !isCured() && !isFailed() && !isComplete() && !isAbandoned() && !isTransfered() && !isDead()){
				return Utils.getDiscontinuedReason(this.getContinuationPhaseDrugOrder()).getDisplayString();
		}
			return "";
	}
	

}
