package org.openmrs.module.mch.impl.pih;

import java.util.*;

import org.openmrs.*;
import org.openmrs.module.mch.mapper.Allergy;


/**
 * This class represents "ADVERSE EFFECT CONSTRUCT" defined in 
 * the PIH Rwanda concept dictionary.  See the mappings at:
 * https://spreadsheets.google.com/ccc?key=0AsiXEfIxorTtdHY4T0pSY2s4c0NKbm82MFUyZlVGMnc&hl=en&pli=1#gid=5
 */
public class AllergyMapping extends ObsMapping implements Comparable<AllergyMapping>, Allergy {
	
	/** List of medications. */
	private List<Obs> medicationsList = new LinkedList<Obs>();

	/** Map of coded effect concept id to the obs */
	private Map<Integer, Obs> effectsCodedMap = new HashMap<Integer, Obs>();

	/** List of non-coded effects */
	private List<Obs> effectsNonCodedList = new LinkedList<Obs>();

	/** Map of coded actions taken concept id to the obs */
	private Map<Integer, Obs> actionsTakenCodedMap = new HashMap<Integer, Obs>();

	/** List of non-coded actions taken */
	private List<Obs> actionsTakenNonCodedList = new LinkedList<Obs>();

	/** Date for the adverse effect construct.  Null is valid. */
	private Date adverseEffectDate = null;
	
	/** Set containing the default answers for Effects (what's displayed on the form). */
	private static final Set<Integer> defaultAnswersForEffects = new TreeSet<Integer>();

	/** Set contain the default answers for actions taken (what's displayed on the form). */
	private static final Set<Integer> defaultAnswersForActionsTaken = new TreeSet<Integer>();

	static {
		Integer[] defaultAnswers = { ConceptDictionary.ANAPHYLAXIS, ConceptDictionary.RASH_MINOR, 
				ConceptDictionary.RASH_MODERATE, ConceptDictionary.RASH_SEVERE, ConceptDictionary.NAUSEA, ConceptDictionary.VOMITING, 
				ConceptDictionary.JAUNDICE, ConceptDictionary.PERIPHERAL_NEUROPATHY, ConceptDictionary.ANEMIA, ConceptDictionary.LACTIC_ACIDOSIS,
				ConceptDictionary.HEPATITIS, ConceptDictionary.NIGHTMARES, ConceptDictionary.LIPODYSTROPHY, ConceptDictionary.OTHER_NON_CODED };
		defaultAnswersForEffects.addAll(Arrays.asList(defaultAnswers));
		
		defaultAnswersForActionsTaken.add(ConceptDictionary.DRUG_WITHDRAWN);
		defaultAnswersForActionsTaken.add(ConceptDictionary.CHANGE_OF_DOSAGE);
		defaultAnswersForActionsTaken.add(ConceptDictionary.SYMPTOMATIC);
	}
	
	public AllergyMapping(Obs obs) {
		super(obs);
		if(obs.getGroupMembers() != null && obs.getGroupMembers().size() > 0) {
			for(Obs group : getObs().getGroupMembers()) {
				if (!group.isVoided()){
					if(group.getConcept().getConceptId().equals(ConceptDictionary.ADVERSE_EFFECT_MEDICATION)) {
						if(group.getValueCoded() != null && !group.getValueCoded().getConceptId().equals(ConceptDictionary.OTHER_NON_CODED))
							medicationsList.add(group);
					} else if(group.getConcept().getConceptId().equals(ConceptDictionary.ADVERSE_EFFECT_MEDICATION_NONCODED)) {
						medicationsList.add(group);
					}
	
					if(group.getConcept().getConceptId().equals(ConceptDictionary.ADVERSE_EFFECT)) {
						if(group.getValueCoded() != null)
							effectsCodedMap.put(group.getValueCoded().getConceptId(), group);
					} else if(group.getConcept().getConceptId().equals(ConceptDictionary.ADVERSE_EFFECT_NONCODED)) {
						effectsNonCodedList.add(group);
					} 
					
					if(group.getConcept().getConceptId().equals(ConceptDictionary.ADVERSE_EVENT_ACTION_TAKEN)) {
						if(group.getValueCoded() != null && !group.getValueCoded().getConceptId().equals(ConceptDictionary.OTHER_NON_CODED))
							actionsTakenCodedMap.put(group.getValueCoded().getConceptId(), group);
					} else if(group.getConcept().getConceptId().equals(ConceptDictionary.ADVERSE_EFFECT_ACTION_TAKEN_NONCODED))
						actionsTakenNonCodedList.add(group);
	
					if(group.getConcept().getConceptId().equals(ConceptDictionary.ADVERSE_EFFECT_DATE)) {
						Date date = group.getValueDatetime();
						if(date != null)
							adverseEffectDate = date;
					}
				}
			}
		} else {
			
			if(obs.getConcept() != null && obs.getConcept().getConceptId().equals(ConceptDictionary.ADVERSE_EFFECT)) {
				if(obs.getValueCoded() != null){
					effectsCodedMap.put(obs.getValueCoded().getConceptId(), obs);
				}	
			} else if(obs.getConcept() != null && obs.getConcept().getConceptId().equals(ConceptDictionary.ADVERSE_EFFECT_NONCODED)) {
				effectsNonCodedList.add(obs);
			} 
			
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#getAdverseEffectDate()
	 */
	@Override
	public Date getAdverseEffectDate() {
		if(!isEmr())
			return null;
		
		if(adverseEffectDate != null)
			return adverseEffectDate;
	
		if (getObs().getEncounter() != null)
			return getObs().getEncounter().getEncounterDatetime();
		return getObs().getObsDatetime();
	}
	
	
	protected List<Obs> getMedications() {
		return medicationsList;
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#getMedicationsString()
	 */
	@Override
	public String getMedicationsString() {
		StringBuilder builder = new StringBuilder();

		for(Obs obs : getMedications()) {
			if(builder.length() > 0)
				builder.append("; ");
			builder.append(obs.getValueAsString(null));
		}
		
		return builder.toString();
	}
	
	protected Map<Integer, Obs> getEffectsCoded() {
		return effectsCodedMap;
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectAnaphylaxis()
	 */
	@Override
	public boolean isEffectAnaphylaxis() {
		return getEffectsCoded().containsKey(ConceptDictionary.ANAPHYLAXIS);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectRashMinor()
	 */
	@Override
	public boolean isEffectRashMinor() {
		return getEffectsCoded().containsKey(ConceptDictionary.RASH_MINOR);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectRashModerate()
	 */
	@Override
	public boolean isEffectRashModerate() {
		return getEffectsCoded().containsKey(ConceptDictionary.RASH_MODERATE);
	}
	
	public boolean getDoNotShow() {
		
		return getEffectsCoded().containsKey(ConceptDictionary.NONE);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectRashSevere()
	 */
	@Override
	public boolean isEffectRashSevere() {
		return getEffectsCoded().containsKey(ConceptDictionary.RASH_SEVERE);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectNausea()
	 */
	@Override
	public boolean isEffectNausea() {
		return getEffectsCoded().containsKey(ConceptDictionary.NAUSEA);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectVomiting()
	 */
	@Override
	public boolean isEffectVomiting() {
		return getEffectsCoded().containsKey(ConceptDictionary.VOMITING);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectJaundice()
	 */
	@Override
	public boolean isEffectJaundice() {
		return getEffectsCoded().containsKey(ConceptDictionary.JAUNDICE);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectPeripheralNeuropathy()
	 */
	@Override
	public boolean isEffectPeripheralNeuropathy() {
		return getEffectsCoded().containsKey(ConceptDictionary.PERIPHERAL_NEUROPATHY);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectAnemia()
	 */
	@Override
	public boolean isEffectAnemia() {
		return getEffectsCoded().containsKey(ConceptDictionary.ANEMIA);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectLacticAcidosis()
	 */
	@Override
	public boolean isEffectLacticAcidosis() {
		return getEffectsCoded().containsKey(ConceptDictionary.LACTIC_ACIDOSIS);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectHepatitis()
	 */
	@Override
	public boolean isEffectHepatitis() {
		return getEffectsCoded().containsKey(ConceptDictionary.HEPATITIS);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectNightmares()
	 */
	@Override
	public boolean isEffectNightmares() {
		return getEffectsCoded().containsKey(ConceptDictionary.NIGHTMARES);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectLipodystrophy()
	 */
	@Override
	public boolean isEffectLipodystrophy() {
		return getEffectsCoded().containsKey(ConceptDictionary.LIPODYSTROPHY);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isEffectOther()
	 */
	@Override
	public boolean isEffectOther() {
		if(effectsNonCodedList.size() > 0)
			return true;

		for(Integer conceptId : getEffectsCoded().keySet()) {
			if(!defaultAnswersForEffects.contains(conceptId))
				return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#getEffectOtherString()
	 */
	@Override
	public String getEffectOtherString() {
		StringBuilder builder = new StringBuilder();

		for(Integer conceptId : getEffectsCoded().keySet()) {
			if(!defaultAnswersForEffects.contains(conceptId)) {
				if(builder.length() > 0)
					builder.append("; ");
				builder.append(getEffectsCoded().get(conceptId).getValueCoded().getDisplayString());
			}
		}
		
		for(Obs obs : effectsNonCodedList) {
			if(builder.length() > 0)
				builder.append("; ");
			builder.append(obs.getValueText());			
		}
		
		return builder.toString();
	}
	
	
	protected Map<Integer, Obs> getActionsTakenCoded() {
		return actionsTakenCodedMap;
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isActionTakenTreatmentStop()
	 */
	@Override
	public boolean isActionTakenTreatmentStop() {
		return getActionsTakenCoded().containsKey(ConceptDictionary.DRUG_WITHDRAWN);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isActionTakenChangeOfDose()
	 */
	@Override
	public boolean isActionTakenChangeOfDose() {
		return getActionsTakenCoded().containsKey(ConceptDictionary.CHANGE_OF_DOSAGE);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isActionTakenSymptomatic()
	 */
	@Override
	public boolean isActionTakenSymptomatic() {
		return getActionsTakenCoded().containsKey(ConceptDictionary.SYMPTOMATIC);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#isActionTakenOther()
	 */
	@Override
	public boolean isActionTakenOther() {
		if(actionsTakenNonCodedList.size() > 0)
			return true;

		for(Integer conceptId : getActionsTakenCoded().keySet()) {
			if(!defaultAnswersForActionsTaken.contains(conceptId))
				return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.AllergyMapper#getActionTakenOtherString()
	 */
	@Override
	public String getActionTakenOtherString() {
		StringBuilder builder = new StringBuilder();

		for(Integer conceptId : getActionsTakenCoded().keySet()) {
			if(!defaultAnswersForActionsTaken.contains(conceptId)) {
				if(builder.length() > 0)
					builder.append("; ");
				builder.append(getActionsTakenCoded().get(conceptId).getValueAsString(null));
			}
		}
		
		for(Obs obs : actionsTakenNonCodedList) {
			if(builder.length() > 0)
				builder.append("; ");
			builder.append(obs.getValueAsString(null));
		}
		
		return builder.toString();
	}
	
	@Override
	public boolean isBlank() {
		return (super.isBlank() || getAdverseEffectDate() == null);// || getMedications() == null || getMedications().size() == 0);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		AllergyMapping other = (AllergyMapping) obj;
		if (!other.getAdverseEffectDate().equals(getAdverseEffectDate()))
			return false;
		if(getObs() == null || other.getObs() == null)
			return false;

		if(medicationsList.size() != other.medicationsList.size())
			return false;

		for(int index = 0; index < medicationsList.size(); index++) {
			if(areObsDifferentValue(medicationsList.get(index), other.medicationsList.get(index)))
				return false;
		}

		if(getEffectsCoded().size() != other.getEffectsCoded().size())
			return false;
		
		for(Integer conceptId : getEffectsCoded().keySet()) {
			if(areObsDifferentValue(getEffectsCoded().get(conceptId), other.getEffectsCoded().get(conceptId)))
				return false;
		}

		return true;
	}


	@Override
	public int compareTo(AllergyMapping obj) {
		if (this == obj)
			return 0;
		if (obj == null)
			return -1;
		if(getAdverseEffectDate() == null && obj.getAdverseEffectDate() == null)
			return 0;
		if(getAdverseEffectDate() == null)
			return 1;
		return getAdverseEffectDate().compareTo(obj.getAdverseEffectDate());
	}
	
//	@Override
//	public Encounter getEncounter() {
//		if (this.getObs() != null  && this.getObs().getEncounter() != null  && this.getObs().getEncounter().getForm() != null){
//					if (this.getObs().getEncounter().getForm().getFormId().equals(Integer.valueOf(ConceptDictionary.ADULT_ALLERGY_FORM))
//							|| this.getObs().getEncounter().getForm().getFormId().equals(Integer.valueOf(ConceptDictionary.PEDI_ALLERGY_FORM)))
//						return this.getObs().getEncounter();
//		}
//		return null;
//	}

}