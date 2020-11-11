package org.openmrs.module.mch.impl.pih;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptSet;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.module.mch.mapper.OI;

public class OIMapping extends ObsMapping implements Comparable<OIMapping>, OI {
	
	private Date obsDate = null;
	
	public OIMapping(Obs obs) {
		super(obs);
	}
	
	
	public Date getOpportunisticInfectionDate(){
		if(!isEmr())
			return null;
		return getObs().getObsDatetime();
		
	}
	
	public Date getObsDate() {
		
		if(obsDate != null)
		{
			return obsDate;
		}
		return super.getObsDate();
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.OIMapper#getDiagnosis()
	 */
	@Override
	public Obs getDiagnosis() {
		if(getObs().getConcept() == null)
			return null;
		
		Obs diagnosis = getObs();
//		if(getObs().getConcept().getId() == ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION && getObs().getValueNumeric() == 0)
//			return null;
		if(getObs().getConcept().getConceptId().equals(ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED) && getObs().getValueCoded() != null && getObs().getValueCoded().getId() == ConceptDictionary.NONE)
			return null;
		if(getObs().getConcept().getConceptId().equals(ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED_NON_CODED) && getObs().getValueText() != null && !"".equals(getObs().getValueText()))
			return diagnosis;
//		if(getObs().getConcept().getId() == ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION)
//			return null;
		if(getObs().getConcept().getConceptId().equals(ConceptDictionary.Current_opportunistic_infection_construct) || getObs().getConcept().getConceptId().equals(ConceptDictionary.OPPORTUNISTIC_INFECTION_SET)) {
			for(Obs group : getObs().getGroupMembers()) {
				if (!group.isVoided()){
					if(group.getConcept().getConceptId().equals(ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED) || group.getConcept().getConceptId().equals(ConceptDictionary.CURRENT_OI))
						diagnosis = group;
					if(group.getConcept().getConceptId().equals(ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED_NON_CODED) || group.getConcept().getConceptId().equals(ConceptDictionary.OPPORTUNISTIC_INFECTION_NON_CODED))
						return group;
					if(group.getConcept().getConceptId().equals(ConceptDictionary.OPPORTUNISTIC_INFECTION_START_DATE))
						obsDate = group.getValueDatetime();
				}
			}			
		}

		return diagnosis;
	}
	
	public boolean getDoNotShow(){
		if(getDiagnosis() != null && getDiagnosis().getValueCoded() != null)
		{
			if(getOIs().contains(getDiagnosis().getValueCoded().getConceptId()) || getDiagnosis().getValueCoded().getConceptId().equals(ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED_NON_CODED))
			{
				return false;
			}
		}
		if(getDiagnosis() != null && (getDiagnosis().getConcept().getConceptId().equals(ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED_NON_CODED) || getDiagnosis().getConcept().getConceptId().equals(ConceptDictionary.OPPORTUNISTIC_INFECTION_NON_CODED)) && getDiagnosis().getValueText() != null && getDiagnosis().getValueText().trim().length() > 0)
		{
			return false;
		}
		if(getDiagnosis() == null)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Returns a set of OI concepts which are used in the OI concept which is defined as
	 * OPPORTUNISTIC_INFECTION. The OPPORTUNISTIC_INFECTION concept is checked for sets and answers
	 * 
	 * @return set of concept Ids
	 */
	public static Set<Integer> getOIs() {
		Set<Integer> ret = new HashSet<Integer>();
		Concept c = Context.getConceptService().getConcept(ConceptDictionary.OPPORTUNISTIC_INFECTION);
		if (c != null) {
			for (ConceptSet cs : c.getConceptSets()) {
				ret.add(cs.getConcept().getConceptId());
			}
			for (ConceptAnswer ca : c.getAnswers()) {
				ret.add(ca.getAnswerConcept().getConceptId());
			}
		}
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.OIMapper#getStage()
	 */
	@Override
	public Concept getStage() {
		if(!isEmr())
			return null;
		
		if(getObs().getGroupMembers() == null)
			return null;
		
		for(Obs group : getObs().getGroupMembers()) {
			if(!group.isVoided() && group.getConcept().getConceptId().equals(ConceptDictionary.WHO_STAGE))
				return group.getValueCoded();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.OIMapper#getComments()
	 */
	@Override
	public String getComments() {
		if(!isEmr())
			return "";
		
		if(getObs().getGroupMembers() == null)
			return null;
		
		for(Obs group : getObs().getGroupMembers()) {
			if(!group.isVoided() && (group.getConcept().getConceptId().equals(ConceptDictionary.CLINICAL_IMPRESSION_COMMENTS) || group.getConcept().getConceptId().equals(ConceptDictionary.OPPORTUNISTIC_INFECTION_COMMENTS)))
				return group.getValueAsString(null);
		}
		return "";
	}
	
	@Override
	public boolean isBlank() {
		return super.isBlank() || getDiagnosis() == null;		
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		OIMapping other = (OIMapping) obj;
		if(getDiagnosis() == null || other.getDiagnosis() == null)
			return false;
		
		if (getObsDate() == null || other.getObsDate() == null)
			return false;
		
		if (getDiagnosis() != null && other.getDiagnosis() != null && getDiagnosis().getValueCoded() != null && other.getDiagnosis().getValueCoded() != null){
			// 1) these are chronic, so only show once.  TODO: move these to chronic disease section??
			//having this return equals means that this row won't be added to the list
			if (getDiagnosis().getValueCoded().equals(other.getDiagnosis().getValueCoded()) && 
					((getDiagnosis().getValueCoded().getConceptId().equals(ConceptDictionary.CURRENT_OI_CARDIOVASCULAR_DISEASE))
				    ||   getDiagnosis().getValueCoded().getConceptId().equals(ConceptDictionary.CURRENT_OI_CEREBRAL_LYMPHOMA)			
				    ||   getDiagnosis().getValueCoded().getConceptId().equals(ConceptDictionary.CURRENT_OI_DEPRESSION)
				    ||   getDiagnosis().getValueCoded().getConceptId().equals(ConceptDictionary.CURRENT_OI_KAPOSIS_SARCOMA)
				    ||   getDiagnosis().getValueCoded().getConceptId().equals(ConceptDictionary.CURRENT_OI_NEUROLOGICAL_DEFICIT)))
					return true;
			
			// special handling for TB:
			if (((getDiagnosis().getValueCoded().getConceptId().equals(ConceptDictionary.CURRENT_OI_TUBERCULOSIS) || 
					getDiagnosis().getValueCoded().getConceptId().equals(ConceptDictionary.CURRENT_OI_PULMONARY_TUBERCULOSIS)))){
					if ((other.getDiagnosis().getValueCoded().getConceptId().equals(ConceptDictionary.CURRENT_OI_TUBERCULOSIS) || 
						other.getDiagnosis().getValueCoded().getConceptId().equals(ConceptDictionary.CURRENT_OI_PULMONARY_TUBERCULOSIS))) {
						if (Math.abs((getDiagnosis().getObsDatetime().getTime() - other.getDiagnosis().getObsDatetime().getTime())) < Math.abs(1000*60*60*24*31*6)) {//6 months
							return true;
								
						}
					}
			}
			
			if (getDiagnosis().getValueCoded().getConceptId().equals(ConceptDictionary.CURRENT_OI_WEIGHT_LOSS_GREATER_THAN_10_PERCENT) &&
					other.getDiagnosis().getValueCoded().getConceptId().equals(ConceptDictionary.CURRENT_OI_WEIGHT_LOSS_GREATER_THAN_10_PERCENT) &&
					Math.abs((getDiagnosis().getObsDatetime().getTime() - other.getDiagnosis().getObsDatetime().getTime())) < Math.abs(1000*60*60*24*31*3))  { //3 months
				return true;
			}
			
		}
		
		
		
		if( !getObsDate().equals(other.getObsDate()))
			return false;

		if(areObsDifferentValue(getDiagnosis(), other.getDiagnosis()))
			return false;

		if(getComments() == null ^ other.getComments() == null)
			return false;
		if((getComments() != null && other.getComments() != null) && !getComments().equals(other.getComments()))
			return false;
		
		return true;

	}
	
	@Override
	public int compareTo(OIMapping obj) {
		if (this == obj)
			return 0;
		if (obj == null)
			return -1;
		if(getObsDate() == null && obj.getObsDate() == null)
			return 0;
		if(getObsDate() == null)
			return 1;
		return getObsDate().compareTo(obj.getObsDate());
	}
	
//	@Override
//	public Encounter getEncounter() {
//		if (this.getObs() != null  && this.getObs().getEncounter() != null  && this.getObs().getEncounter().getForm() != null){
//					if (this.getObs().getEncounter().getForm().getFormId().equals(Integer.valueOf(ConceptDictionary.ADULT_OI_FORM))
//							|| this.getObs().getEncounter().getForm().getFormId().equals(Integer.valueOf(ConceptDictionary.PEDI_OI_FORM)))
//						return this.getObs().getEncounter();
//		}
//		return null;
//	}


}