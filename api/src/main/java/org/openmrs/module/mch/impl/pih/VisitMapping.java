package org.openmrs.module.mch.impl.pih;

import java.util.Date;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.module.mch.mapper.Visit;

public class VisitMapping extends ObsMapping implements Comparable<VisitMapping>, Visit {
	
	public VisitMapping(Obs obs) {
		super(obs);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.VisitMapper#getDate()
	 */
	@Override
	public Date getDate() {
		
		
		if(getObs() != null && getObs().getConcept().getConceptId() == ConceptDictionary.WEIGHT_KG)
				return getObs().getObsDatetime();
			
		
		if (getObs().getEncounter() != null)
			return getObs().getEncounter().getEncounterDatetime();
		return getObsDate();
	}
	
	private Obs getObsOfType(int conceptId) {
		if(getObs() == null)
			return null;
		
		if(getObs().getConcept() != null && getObs().getConcept().getConceptId().equals(conceptId)) {
			return getObs();
		}
		
		if(getObs().getGroupMembers() != null) {
			boolean correctTest = false;
			Obs result = null;
			for(Obs group : getObs().getGroupMembers()) {
				if (!group.isVoided()){
					if(group.getConcept().getConceptId().equals(ConceptDictionary.TESTS_ORDERED) && group.getValueCoded().getConceptId().equals(conceptId))
						correctTest = true;
					else if(group.getConcept().getConceptId().equals(ConceptDictionary.OTHER_LAB_TEST_RESULT))
						result = group;
					else if(group.getConcept().getConceptId().equals(conceptId))
						return group;
				}
			}
			if(correctTest)
				return result;
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.VisitMapper#getWeight()
	 */
	@Override
	public Obs getWeight() {
		return getObsOfType(ConceptDictionary.WEIGHT_KG);
	}
	
	@Override
	public Obs getFunctionalAbilityOfThePatient(){
		return getObsOfType(ConceptDictionary.FUNCTIONAL_ABILITY_OF_THE_PATIENT);
	}
	
	@Override
	public Obs getHeight() {
		return getObsOfType(ConceptDictionary.HEIGHT_CM);
	}
	
	@Override
	public Obs getZScoreHeight() {
		return getObsOfType(ConceptDictionary.Z_SCORE_HEIGHT);
	}
	
	@Override
	public Obs getZScoreWeight() {
		return getObsOfType(ConceptDictionary.Z_SCORE_WEIGHT);
	}
	
	@Override
	public Obs getHeightWeightPercentile() {
		return getObsOfType(ConceptDictionary.HEIGHT_WEIGHT_PERCENTILE);
	}
	
	@Override
	public Obs getPatientInformed() {
		return getObsOfType(ConceptDictionary.PATIENT_INFORMED);
	}
	
	@Override
	public Obs getNextVisit() {
		return getObsOfType(ConceptDictionary.NEXT_VISIT);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.VisitMapper#getOI()
	 */
	@Override
	public Obs getOI() {
		
		Obs obs = getObsOfType(ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED_NON_CODED);
		if (obs != null) {
			return obs;
		}
		obs = getObsOfType(ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED);
		if (obs != null) {
			return obs;
		}
		
		obs = getObsOfType(ConceptDictionary.Current_opportunistic_infection_construct);
		if (obs != null) {
			for (Obs o : obs.getGroupMembers()) {
				if (o.getConcept().getConceptId().equals(ConceptDictionary.CURRENT_OI) && o.getValueCoded() != null
				        && !o.getValueCoded().getConceptId().equals(ConceptDictionary.OTHER_NON_CODED)) {
					return o;
				}
				if (o.getConcept().getConceptId().equals(ConceptDictionary.OPPORTUNISTIC_INFECTION_NON_CODED)) {
					return o;
				}
			}
		}
		
		obs = getObsOfType(ConceptDictionary.OPPORTUNISTIC_INFECTION_SET);
		if (obs != null) {
			for (Obs o : obs.getGroupMembers()) {
				if (o.getConcept().getConceptId().equals(ConceptDictionary.CURRENT_OI) && o.getValueCoded() != null
				        && !o.getValueCoded().getConceptId().equals(ConceptDictionary.OTHER_NON_CODED)) {
					return o;
				}
				if (o.getConcept().getConceptId().equals(ConceptDictionary.OPPORTUNISTIC_INFECTION_NON_CODED)) {
					return o;
				}
			}
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.VisitMapper#getSTI()
	 */
	@Override
	public Obs getSTI() {
		Obs obs = getObsOfType(ConceptDictionary.STI_INFECTION);
		if(obs != null)
		{
			return obs;
		}
		
		obs = getObsOfType(ConceptDictionary.STI_INFECTION_NON_CODED);
		if(obs != null)
		{
			return obs;
		}
		return getObsOfType(ConceptDictionary.SEXUALLY_TRANSMITTED_INFECTION_SYMPTOMS_COMMENT);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.VisitMapper#getTBScreening()
	 */
	@Override
	public Obs getTBScreening() {
		return getObsOfType(ConceptDictionary.RESULT_OF_TUBERCULOSIS_SCREENING_QUALITATIVE);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.VisitMapper#getFamilyPlanning()
	 */
	@Override
	public Obs getFamilyPlanning() {
		return getObsOfType(ConceptDictionary.METHOD_OF_FAMILY_PLANNING);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.VisitMapper#getPregnancyStatus()
	 */
	@Override
	public Obs getPregnancyStatus() {
		return getObsOfType(ConceptDictionary.PREGNANCY_STATUS);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.VisitMapper#getNumberOfMissedDosesInThePastMonth()
	 */
	@Override
	public Obs getNumberOfMissedDosesInThePastMonth() {
		//NUMBER OF MISSED DOSES IN THE PAST MONTH
		//NUMBER OF HIV DRUG DOSES MISSED LAST 30 DAYS
		//MISSED ANTIRETROVIRAL DRUG CONSTRUCT
		return getObsOfType(ConceptDictionary.NUMBER_OF_DOSES_OF_ANTIRETROVIRALS_MISSED_IN_THE_PAST_MONTH);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.VisitMapper#getCreat()
	 */
	@Override
	public Obs getCreat() {
		return getObsOfType(ConceptDictionary.SERUM_CREATININE);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.VisitMapper#getReasonForPoorAdherenceToAntiRetoroviralTherapy()
	 */
	@Override
	public Obs getReasonForPoorAdherenceToAntiRetoroviralTherapy() {
		//2686 Reason for adhesence
		return getObsOfType(ConceptDictionary.FREE_TEXT_REASON_FOR_POOR_ADHERENCE_TO_ANTIRETROVIRAL_THERAPY );
	}

	@Override
	public int compareTo(VisitMapping obj) {
		if (this == obj)
			return 0;
		if (obj == null)
			return -1;
		if(getDate() == null && obj.getDate() == null)
			return 0;
		if(getDate() == null)
			return 1;
		return getDate().compareTo(obj.getDate());
	}
	
//	@Override
//	public Encounter getEncounter() {
//		if (this.getObs() != null  && this.getObs().getEncounter() != null  && this.getObs().getEncounter().getForm() != null){
//					if (this.getObs().getEncounter().getForm().getFormId().equals(Integer.valueOf(ConceptDictionary.ADULT_VISIT_FORM))
//							|| this.getObs().getEncounter().getForm().getFormId().equals(Integer.valueOf(ConceptDictionary.PEDI_VISIT_FORM)))
//						return this.getObs().getEncounter();
//		}
//		return null;
//	}
}
