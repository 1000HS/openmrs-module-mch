package org.openmrs.module.mch.impl.pih;

import java.util.Date;

import org.openmrs.DrugOrder;
import org.openmrs.api.context.Context;
import org.openmrs.module.mch.utils.Utils;

public class ProphylaxisMapping extends DrugOrder {
	
		DrugOrder drugOrder;
		
		public ProphylaxisMapping(DrugOrder dor){
			this.drugOrder = dor;
		}
		public ProphylaxisMapping(){}
		
		
		public DrugOrder getDrugOrder() {
			return drugOrder;
		}

		public void setDrugOrder(DrugOrder drugOrder) {
			this.drugOrder = drugOrder;
		}
		
		public boolean isToxicity(){
			if (drugOrder != null && Utils.getDiscontinuedReason(drugOrder) != null 
					&& Utils.getDiscontinuedReason(drugOrder).getConceptId().equals(ConceptDictionary.PROPHYLAXIS_REASON_FOR_STOPPING_TOXICITY))
				return true;
			return false;
		}
		public boolean isAbandoned(){
			if (drugOrder != null && Utils.getDiscontinuedReason(drugOrder) != null 
					&& Utils.getDiscontinuedReason(drugOrder).getConceptId().equals(ConceptDictionary.PROPHYLAXIS_REASON_FOR_STOPPING_ABANDONED))
				return true;
			return false;
		}
		public boolean isOutOfStock(){
			if (drugOrder != null && Utils.getDiscontinuedReason(drugOrder) != null 
					&& Utils.getDiscontinuedReason(drugOrder).getConceptId().equals(ConceptDictionary.PROPHYLAXIS_REASON_FOR_STOPPING_OUT_OF_STOCK))
				return true;
			return false;
		}
		public boolean isCd4Improved(){
			if (drugOrder != null && Utils.getDiscontinuedReason(drugOrder) != null 
					&& Utils.getDiscontinuedReason(drugOrder).getConceptId().equals(ConceptDictionary.PROPHYLAXIS_REASON_FOR_STOPPING_CD4_IMPROVEMENT))
				return true;
			return false;
		}
		public boolean isTermine(){
			if (drugOrder != null && Utils.getDiscontinuedReason(drugOrder) != null 
					&& Utils.getDiscontinuedReason(drugOrder).getConceptId().equals(ConceptDictionary.PROPHYLAXIS_REASON_FOR_STOPPING_TERMINE))
				return true;
			return false;
		}
		public boolean isReasonForStoppingOther(){
			if (drugOrder != null && Utils.getDiscontinuedReason(drugOrder) != null 
					&& !isCd4Improved() && !isOutOfStock() && !isAbandoned() && !isToxicity())
				return true;
			return false;
		}
		
		public boolean getReasonForStoppingOther(){
			return isReasonForStoppingOther();
		}
		public String getDiscontinueReasonOther(){
			if (this.isReasonForStoppingOther())
				return Utils.getDiscontinuedReason(drugOrder).getDisplayString();
			return "";
		}
		
		public boolean isCotrimoxisole(){
			if (drugOrder != null && drugOrder.getConcept() != null 
					&& drugOrder.getConcept().getConceptId().equals(ConceptDictionary.DRUG_COTRIMOXAZOLE))
				return true;
			return false;
		}
		
		public boolean isFluconazole(){
			if (drugOrder != null && drugOrder.getConcept() != null 
					&& drugOrder.getConcept().getConceptId().equals(ConceptDictionary.DRUG_FLUCONAZOLE))
				return true;	
			return false;
		}	
		
		public boolean isIsoniazid(){
			if (drugOrder != null && drugOrder.getConcept() != null 
					&& drugOrder.getConcept().getConceptId().equals(ConceptDictionary.DRUG_ISONIAZID))
				return true;	
			return false;
		}
		
		public boolean isDapsone(){
			if (drugOrder != null && drugOrder.getConcept() != null 
					&& drugOrder.getConcept().getConceptId().equals(ConceptDictionary.DRUG_DAPSONE))
				return true;	
			return false;
			
		}
		public boolean isProphylaxisOther() {
			if (drugOrder != null && drugOrder.getConcept() != null 
					&& !isCotrimoxisole() && !isFluconazole() && !isDapsone())
				return true;
			return false;
		}
		
		public boolean getProphylaxisOther() {
			return this.isProphylaxisOther();
		}
		
		public String getProphylaxisTypeOther(){
			if (drugOrder != null && drugOrder.getConcept() != null 
					&& !isCotrimoxisole() && !isFluconazole() && !isDapsone())
				return drugOrder.getConcept().getDisplayString();
			return "";
		}
		
		public Date getStopDate(){
			if (drugOrder != null){
				return drugOrder.getEffectiveStopDate();
			}
			return null;
		}
		
		public Date getStartDate(){
			if (drugOrder != null)
				return drugOrder.getEffectiveStartDate();
			return null;
		}
	
}
