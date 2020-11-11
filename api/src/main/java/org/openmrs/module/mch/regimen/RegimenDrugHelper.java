package org.openmrs.module.mch.regimen;

import java.util.List;

import org.openmrs.DrugOrder;
import org.openmrs.module.mch.impl.pih.ConceptDictionary;
import org.openmrs.module.mch.utils.Utils;

public class RegimenDrugHelper {
	
	public boolean isRegimenFailure(List<DrugOrder> drugOrders) {
		return checkForStopReason(ConceptDictionary.REGIMEN_FAILURE, drugOrders);
	}

	public boolean isPATIENT_DIED(List<DrugOrder> drugOrders) {
		return checkForStopReason(ConceptDictionary.PATIENT_DIED, drugOrders);
	}

	public boolean isDRUG_UNAVAILABLE(List<DrugOrder> drugOrders) {
		return checkForStopReason(ConceptDictionary.DRUG_UNAVAILABLE, drugOrders);
	}

	public boolean isPATIENT_DEFAULTED(List<DrugOrder> drugOrders) {
		return checkForStopReason(ConceptDictionary.PATIENT_DEFAULTED, drugOrders);
	}

	public boolean isPATIENT_PREGNANT(List<DrugOrder> drugOrders) {
		return checkForStopReason(ConceptDictionary.PATIENT_PREGNANT, drugOrders);
	}

	public boolean isINTERACTION_WITH_TUBERCULOSIS_DRUG(List<DrugOrder> drugOrders) {
		return checkForStopReason(ConceptDictionary.INTERACTION_WITH_TUBERCULOSIS_DRUG, drugOrders);
	}

	public boolean isToxicity(List<DrugOrder> drugOrders) {
		return checkForStopReason(ConceptDictionary.PATIENT_HAD_SIDE_EFFECTS, drugOrders);
	}

	private boolean checkForStopReason(int stopConceptId, List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundStopReason = false;
		for(DrugOrder drugOrder : drugOrders) {
			if(Utils.getDiscontinuedReason(drugOrder) != null)
				foundStopReason |= Utils.getDiscontinuedReason(drugOrder).getConceptId() == stopConceptId;
		}

		return foundStopReason;
	}

	public boolean isAZT_3TC_Kaletra(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundAZT = false;
		boolean found3TC = false;
		boolean foundKaletra = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundAZT |= containsAZT(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundKaletra |= containsKaletra(drugOrder);
		}

		return foundAZT && found3TC && foundKaletra;
	}

	public boolean isDDI_ABC_Kaletra(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundDDI = false;
		boolean foundABC = false;
		boolean foundKaletra = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundDDI |= containsDDI(drugOrder);
			foundABC |= containsABC(drugOrder);
			foundKaletra |= containsKaletra(drugOrder);
		}

		return foundDDI && foundABC && foundKaletra;
	}

	public boolean isABC_3TC_Kaletra(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundABC = false;
		boolean found3TC = false;
		boolean foundKaletra = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundABC |= containsABC(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundKaletra |= containsKaletra(drugOrder);
		}

		return foundABC && found3TC && foundKaletra;
	}

	public boolean isTDF_3TC_EFV(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundTDF = false;
		boolean found3TC = false;
		boolean foundEFV = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundTDF |= containsTDF(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundEFV |= containsEFV(drugOrder);
		}

		return foundTDF && found3TC && foundEFV;
	}

	public boolean isTDF_3TC_Kaletra(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundTDF = false;
		boolean found3TC = false;
		boolean foundKaletra = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundTDF |= containsTDF(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundKaletra |= containsKaletra(drugOrder);
		}

		return foundTDF && found3TC && foundKaletra;
	}

	public boolean isTDF_3TC_NVP(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundTDF = false;
		boolean found3TC = false;
		boolean foundNVP = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundTDF |= containsTDF(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundNVP |= containsNVP(drugOrder);
		}

		return foundTDF && found3TC && foundNVP;
	}

	public boolean isAZT_3TC_EFV(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundAZT = false;
		boolean found3TC = false;
		boolean foundEFV = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundAZT |= containsAZT(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundEFV |= containsEFV(drugOrder);
		}

		return foundAZT && found3TC && foundEFV;
	}

	public boolean isAZT_3TC_NVP(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundAZT = false;
		boolean found3TC = false;
		boolean foundNVP = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundAZT |= containsAZT(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundNVP |= containsNVP(drugOrder);
		}

		return foundAZT && found3TC && foundNVP;
	}

	public boolean isD4T_3TC_EFV(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundD4T = false;
		boolean found3TC = false;
		boolean foundEFV = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundD4T |= containsD4T(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundEFV |= containsEFV(drugOrder);
		}

		return foundD4T && found3TC && foundEFV;
	}

	public boolean isD4T_3TC_NVP(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundD4T = false;
		boolean found3TC = false;
		boolean foundNVP = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundD4T |= containsD4T(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundNVP |= containsNVP(drugOrder);
		}

		return foundD4T && found3TC && foundNVP;
	}

	public boolean isABC_3TC_NVP(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundABC = false;
		boolean found3TC = false;
		boolean foundNVP = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundABC |= containsABC(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundNVP |= containsNVP(drugOrder);
		}

		return foundABC && found3TC && foundNVP;
	}
	
	public boolean isABC_3TC_EFV(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundABC = false;
		boolean found3TC = false;
		boolean foundEFV = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundABC |= containsABC(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundEFV |= containsEFV(drugOrder);
		}

		return foundABC && found3TC && foundEFV;
	}
	
	public boolean isABC_3TC_LPVr(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundABC = false;
		boolean found3TC = false;
		boolean foundLPVr = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundABC |= containsABC(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundLPVr |= containsLPVr(drugOrder);
		}

		return foundABC && found3TC && foundLPVr;
	}
	
	public boolean isD4T_3TC_LPVr(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundD4T = false;
		boolean found3TC = false;
		boolean foundLPVr = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundD4T |= containsD4T(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundLPVr |= containsLPVr(drugOrder);
		}

		return foundD4T && found3TC && foundLPVr;
	}
	
	private boolean containsLPVr(DrugOrder drugOrder) {
	    int val = drugOrder.getConcept().getConceptId().intValue();
	    if (       val == ConceptDictionary.DRUG_LOPINAVIR_AND_RITONAVIR
                || val == ConceptDictionary.DRUG_DIDANOSINE_ABACAVIR_LOPINAVIR_RITONAVIR
                || val == ConceptDictionary.DRUG_DDI_TDF_LOPR
                || val == ConceptDictionary.DRUG_DDI_3TC_LOPR
                ||val == ConceptDictionary.DRUG_DDI_AZT_LOPR
                || val == ConceptDictionary.DRUG_TDF_ABC_LOPR
                || val == ConceptDictionary.DRUG_TDF_3TC_LOPR
                || val == ConceptDictionary.DRUG_TDF_AZT_LOPR
                || val == ConceptDictionary.DRUG_AZT_3TC_LORR
                 ) {
             return true;
	    }
		return false;
	}
	
	public boolean isAZT_3TC_LPVr(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return false;
		
		boolean foundAZT = false;
		boolean found3TC = false;
		boolean foundLPVr = false;
		for(DrugOrder drugOrder : drugOrders) {
			foundAZT |= containsAZT(drugOrder);
			found3TC |= contains3TC(drugOrder);
			foundLPVr |= containsLPVr(drugOrder);
		}

		return foundAZT && found3TC && foundLPVr;
	}
	
	private boolean containsDDI(DrugOrder drugOrder) {
	    int val = drugOrder.getConcept().getConceptId().intValue();
		if (val == ConceptDictionary.DRUG_DDI
		       || val == ConceptDictionary.DRUG_DDI_ABC_LOPR
		       || val == ConceptDictionary.DRUG_DDI_3TC_LOPR
		       || val == ConceptDictionary.DRUG_DDI_AZT_LOPR
		       || val == ConceptDictionary.DRUG_DDI_TDF_LOPR
		        ) {
			return true;
		}
		return false;
	}

	private boolean containsABC(DrugOrder drugOrder) {
	    int val = drugOrder.getConcept().getConceptId().intValue();
	    if (val == ConceptDictionary.DRUG_ABC
	               || val == ConceptDictionary.DRUG_DDI_ABC_LOPR
	               || val == ConceptDictionary.DRUG_TDF_ABC_LOPR
	               || val == ConceptDictionary.DRUG_AZT_3TC_AND_ABC
	                ) {
	            return true;
	    }
		return false;
	}

	private boolean containsKaletra(DrugOrder drugOrder) {
	    int val = drugOrder.getConcept().getConceptId().intValue();
	    if (       val == ConceptDictionary.DRUG_LOPINAVIR_AND_RITONAVIR
                || val == ConceptDictionary.DRUG_DIDANOSINE_ABACAVIR_LOPINAVIR_RITONAVIR
                || val == ConceptDictionary.DRUG_DDI_TDF_LOPR
                || val == ConceptDictionary.DRUG_DDI_3TC_LOPR
                ||val == ConceptDictionary.DRUG_DDI_AZT_LOPR
                || val == ConceptDictionary.DRUG_TDF_ABC_LOPR
                || val == ConceptDictionary.DRUG_TDF_3TC_LOPR
                || val == ConceptDictionary.DRUG_TDF_AZT_LOPR
                || val == ConceptDictionary.DRUG_AZT_3TC_LORR
                 ) {
             return true;
	    }
		return false;
	}

	private boolean containsTDF(DrugOrder drugOrder) {
	    int val = drugOrder.getConcept().getConceptId().intValue();
	    if (       val == ConceptDictionary.DRUG_TDF
                || val == ConceptDictionary.DRUG_TDF_FTC
                || val == ConceptDictionary.DRUG_TDF_3TC_EFV
                || val == ConceptDictionary.DRUG_TDF_3TC_NVP
                ||val == ConceptDictionary.DRUG_TDF_FTC_NVP
                || val == ConceptDictionary.DRUG_TDF_ABC_LOPR
                || val == ConceptDictionary.DRUG_DDI_TDF_LOPR
                || val == ConceptDictionary.DRUG_TDF_3TC_LOPR
                || val == ConceptDictionary.DRUG_TDF_AZT_LOPR
                 ) {
             return true;
        }
		return false;
	}

	private boolean containsAZT(DrugOrder drugOrder) {
	    int val = drugOrder.getConcept().getConceptId().intValue();
	    if (       val == ConceptDictionary.DRUG_AZT
                || val == ConceptDictionary.DRUG_AZT_3TC_NVP
                || val == ConceptDictionary.DRUG_AZT_3TC_EFV
                || val == ConceptDictionary.DRUG_DDI_AZT_LOPR
                ||val == ConceptDictionary.DRUG_AZT_3TC_AND_ABC
                || val == ConceptDictionary.DRUG_AZT_3TC
                || val == ConceptDictionary.DRUG_TDF_AZT_LOPR
                || val == ConceptDictionary.DRUG_AZT_3TC_LORR
                 ) {
             return true;
        }
		return false;
	}

	private boolean containsEFV(DrugOrder drugOrder) {
	    int val = drugOrder.getConcept().getConceptId().intValue();
	    if (       val == ConceptDictionary.DRUG_EFV
                || val == ConceptDictionary.DRUG_D4T30_3TC_EFV
                || val == ConceptDictionary.DRUG_D4T40_3TC_EFV
                || val == ConceptDictionary.DRUG_TDF_3TC_EFV
                ||val == ConceptDictionary.DRUG_AZT_3TC_EFV
                || val == ConceptDictionary.DRUG_D4T_3TC_EFV    
                 ) {
             return true;
        }
		return false;
	}

    private boolean contains3TC(DrugOrder drugOrder) {
        int val = drugOrder.getConcept().getConceptId().intValue();
        if (       val == ConceptDictionary.DRUG_3TC
                || val == ConceptDictionary.DRUG_D4T30_3TC_NVP
                || val == ConceptDictionary.DRUG_D4T40_3TC_NVP
                || val == ConceptDictionary.DRUG_D4T30_3TC_EFV
                || val == ConceptDictionary.DRUG_D4T40_3TC_EFV
                || val == ConceptDictionary.DRUG_TNF_3TC 
                || val == ConceptDictionary.DRUG_AZT_3TC_NVP
                || val == ConceptDictionary.DRUG_AZT_3TC_EFV
                || val == ConceptDictionary.DRUG_TDF_3TC_EFV
                || val == ConceptDictionary.DRUG_D4T_3TC_EFV
                || val == ConceptDictionary.DRUG_TDF_3TC_NVP 
                || val == ConceptDictionary.DRUG_D4T_3TC_NVP
                || val == ConceptDictionary.DRUG_DDI_3TC_LOPR
                || val == ConceptDictionary.DRUG_AZT_3TC_AND_ABC
                || val == ConceptDictionary.DRUG_AZT_3TC 
                || val == ConceptDictionary.DRUG_D4T_3TC
                || val == ConceptDictionary.DRUG_TDF_3TC_LOPR
                || val == ConceptDictionary.DRUG_AZT_3TC_LORR
                 ) {
             return true;
        }
		return false;
	}

	private boolean containsNVP(DrugOrder drugOrder) {
	    int val = drugOrder.getConcept().getConceptId().intValue();
	    if (       val == ConceptDictionary.DRUG_NVP
                || val == ConceptDictionary.DRUG_D4T30_3TC_NVP
                || val == ConceptDictionary.DRUG_D4T40_3TC_NVP
                || val == ConceptDictionary.DRUG_INFANT_NVP
                || val == ConceptDictionary.DRUG_AZT_3TC_NVP
                || val == ConceptDictionary.DRUG_TDF_3TC_NVP
                || val == ConceptDictionary.DRUG_NVP_UNIQUE_DOSE
                || val == ConceptDictionary.DRUG_TDF_FTC_NVP
                || val == ConceptDictionary.DRUG_D4T_3TC_NVP
                 ) {
             return true;
        }
		
		return false;
	}

	private boolean containsD4T(DrugOrder drugOrder) {
	    int val = drugOrder.getConcept().getConceptId().intValue();
	    if (       val == ConceptDictionary.DRUG_D4T
                || val == ConceptDictionary.DRUG_D4T30_3TC_EFV
                || val == ConceptDictionary.DRUG_D4T40_3TC_EFV
                || val == ConceptDictionary.DRUG_D4T30_3TC_NVP
                || val == ConceptDictionary.DRUG_D4T40_3TC_NVP
                || val == ConceptDictionary.DRUG_D4T_3TC_EFV
                || val == ConceptDictionary.DRUG_D4T_3TC_NVP
                || val == ConceptDictionary.DRUG_D4T_3TC
                 ) {
             return true;
        }
		return false;
	}

	private int getConceptInt(int input){
	    return input;
	}
	
}

