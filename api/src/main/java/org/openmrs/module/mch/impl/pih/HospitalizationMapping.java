package org.openmrs.module.mch.impl.pih;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.module.mch.mapper.Hospitalization;

public class HospitalizationMapping extends ObsMapping implements Comparable<HospitalizationMapping>, Hospitalization {

	/** Map of coded effect concept id to the obs */
	private Map<Integer, Obs> diagnosisCodedMap = new HashMap<Integer, Obs>();

	/** List of non-coded effects */
	private List<Obs> diagnosisNonCodedList = new LinkedList<Obs>();

	/** DATE_OF_HOSPITAL_ADMITTANCE.  Null is valid. */
	private Date dateOfHospitalAdmittance = null;

	/** HOSPITALIZATION_DISCHARGE_DATE.  Null is valid. */
	private Date dateOfHospitalDischarge = null;
	
	/** DURATION_OF_HOSPITALIZATION.  Null is valud. */
	private Obs durationOfHospitalization = null;
	
	/** HOSPITALIZATION_COMMENT.  Null is valud. */
	private Obs hospitalizationComment = null;
	
	/** Set contain the default answers for actions taken (what's displayed on the form). */
	private static final Set<Integer> defaultAnswersForDiagnosis = new TreeSet<Integer>();

	static {
		defaultAnswersForDiagnosis.add(ConceptDictionary.GASTROENTERITIS);
		defaultAnswersForDiagnosis.add(ConceptDictionary.MALARIA);
		defaultAnswersForDiagnosis.add(ConceptDictionary.MALNUTRITION);
		defaultAnswersForDiagnosis.add(ConceptDictionary.MENINGITIS);
		defaultAnswersForDiagnosis.add(ConceptDictionary.PNEUMONIA);
	}

	public HospitalizationMapping(Obs obs) {
		super(obs);
		
		if(getObs().getGroupMembers() != null) {
			for(Obs group : getObs().getGroupMembers()) {
				if (!group.isVoided()){
					if(group.getConcept().getConceptId().equals(ConceptDictionary.DIAGNOSIS_WHILE_HOSPITALIZED)) {
						if(group.getValueCoded() != null && !group.getValueCoded().getConceptId().equals(ConceptDictionary.OTHER_NON_CODED))
							diagnosisCodedMap.put(group.getValueCoded().getConceptId(), group);
					} else if(group.getConcept().getConceptId().equals(ConceptDictionary.DIAGNOSIS_WHILE_HOSPITALIZED_NON_CODED)) {
						diagnosisNonCodedList.add(group);
					}
	
					if(group.getConcept().getConceptId().equals(ConceptDictionary.DATE_OF_HOSPITAL_ADMITTANCE)) {
						Date date = group.getValueDatetime();
						if(date != null)
							dateOfHospitalAdmittance = date;
					}
					
					if(group.getConcept().getConceptId().equals(ConceptDictionary.HOSPITALIZATION_DISCHARGE_DATE)) {
						Date date = group.getValueDatetime();
						if(date != null)
							dateOfHospitalDischarge = date;
					}
	
					if(group.getConcept().getConceptId().equals(ConceptDictionary.DURATION_OF_HOSPITALIZATION_CONSTRUCT)) {
						durationOfHospitalization = group;
					}
					
					if(group.getConcept().getConceptId().equals(ConceptDictionary.HOSPITALIZATION_COMMENT)) {
						hospitalizationComment = group;
					}
				}
			}
		}

	}
	
/*	public Obs getDiagnosis() {
		if(!isEmr())
			return null;
		
		if(getObs().getGroupMembers() == null)
			return null;
		
		Obs obsValue = null;
		for(Obs group : getObs().getGroupMembers()) {
		}
		
		return obsValue;
	}
*/	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.HospitalMapper#getAdmittanceDate()
	 */
	@Override
	public Date getAdmittanceDate() {
		if(!isEmr())
			return null;
		
		if(dateOfHospitalAdmittance != null)
			return dateOfHospitalAdmittance;
		
		if (getObs().getEncounter() != null)
			return getObs().getEncounter().getEncounterDatetime();
		
		return getObsDate();
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.HospitalMapper#getDischargeDate()
	 */
	@Override
	public Date getDischargeDate() {
		return dateOfHospitalDischarge;
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.HospitalMapper#getDurationDays()
	 */
	@Override
	public String getDurationDays() {
		Date dischargeDate = getDischargeDate();
		Date admittanceDate = getAdmittanceDate();

		long days = 0;
		if(admittanceDate != null && dischargeDate != null) {
			days = (dischargeDate.getTime() - admittanceDate.getTime()) / (1000 * 60 * 60 * 24);
			return "<u>&nbsp;"+days+"&nbsp;</u>";
		}

		if(durationOfHospitalization != null && durationOfHospitalization.getGroupMembers() != null) {
			boolean daysConstruct = false;
			for(Obs group : durationOfHospitalization.getGroupMembers()) {
				if (!group.isVoided()){
					if(group.getConcept().getConceptId().equals(ConceptDictionary.Duration_of_hospitalization)) {
						Double daysValue = group.getValueNumeric();
						if(daysValue != null)
							days = daysValue.longValue();
					} else if(group.getConcept().getConceptId().equals(ConceptDictionary.TIME_UNITS)) {
						Concept concept = group.getValueCoded();
						if(concept != null && concept.getConceptId().equals(ConceptDictionary.DAYS))
							daysConstruct = true;
					}
				}
			}
			if(daysConstruct && days > 0)
				return "<u>&nbsp;"+days+"&nbsp;</u>";
		}
		return "<u>&nbsp;&nbsp;&nbsp;&nbsp;</u>";
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.HospitalMapper#getDurationWeeks()
	 */
	@Override
	public String getDurationWeeks() {
			long weeks = 0;
			if(durationOfHospitalization != null && durationOfHospitalization.getGroupMembers() != null) {
				boolean weeksConstruct = false;
				for(Obs group : durationOfHospitalization.getGroupMembers()) {
					if (!group.isVoided()){
						if(group.getConcept().getConceptId().equals(ConceptDictionary.Duration_of_hospitalization)) {
							Double weeksValue = group.getValueNumeric();
							if(weeksValue != null)
								weeks = weeksValue.longValue();
						} else if(group.getConcept().getConceptId().equals(ConceptDictionary.TIME_UNITS)) {
							Concept concept = group.getValueCoded();
							if(concept != null && concept.getConceptId().equals(ConceptDictionary.WEEKS))
								weeksConstruct = true;
						}
					}
				}
				if(weeksConstruct && weeks > 0)
					return "<u>&nbsp;"+weeks+"&nbsp;</u>";
			}
		return "<u>&nbsp;&nbsp;&nbsp;&nbsp;</u>";
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.HospitalMapper#getDurationMonths()
	 */
	@Override
	public String getDurationMonths() {
		long months = 0;
		if(durationOfHospitalization != null && durationOfHospitalization.getGroupMembers() != null) {
			boolean monthsConstruct = false;
			for(Obs group : durationOfHospitalization.getGroupMembers()) {
				if (!group.isVoided()){
					if(group.getConcept().getConceptId().equals(ConceptDictionary.Duration_of_hospitalization)) {
						Double daysValue = group.getValueNumeric();
						if(daysValue != null)
							months = daysValue.longValue();
					} else if(group.getConcept().getConceptId().equals(ConceptDictionary.TIME_UNITS)) {
						Concept concept = group.getValueCoded();
						if(concept != null && concept.getConceptId().equals(ConceptDictionary.MONTHS))
							monthsConstruct = true;
					}
				}
			}
			if(monthsConstruct && months > 0)
				return "<u>&nbsp;"+months+"&nbsp;</u>";
		}
		return "<u>&nbsp;&nbsp;&nbsp;&nbsp;</u>";
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.HospitalMapper#getComments()
	 */
	@Override
	public String getComments() {
		if(!isEmr() || hospitalizationComment == null)
			return "";

		return hospitalizationComment.getValueAsString(null);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.HospitalMapper#isGastroenteritis()
	 */
	@Override
	public boolean isGastroenteritis() {
		return diagnosisCodedMap.containsKey(ConceptDictionary.GASTROENTERITIS);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.HospitalMapper#isMalaria()
	 */
	@Override
	public boolean isMalaria() {
		return diagnosisCodedMap.containsKey(ConceptDictionary.MALARIA);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.HospitalMapper#isMalnutrition()
	 */
	@Override
	public boolean isMalnutrition() {
		return diagnosisCodedMap.containsKey(ConceptDictionary.MALNUTRITION);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.HospitalMapper#isMeningitis()
	 */
	@Override
	public boolean isMeningitis() {
		return diagnosisCodedMap.containsKey(ConceptDictionary.MENINGITIS);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.HospitalMapper#isPneumonia()
	 */
	@Override
	public boolean isPneumonia() {
		return diagnosisCodedMap.containsKey(ConceptDictionary.PNEUMONIA);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.HospitalMapper#isOther()
	 */
	@Override
	public boolean isOther() {
		if(diagnosisNonCodedList.size() > 0)
			return true;

		for(Integer conceptId : diagnosisCodedMap.keySet()) {
			if(!defaultAnswersForDiagnosis.contains(conceptId))
				return true;
		}
		
		return false;
	}
	
	public boolean isBlank() {
		return super.isBlank() || getAdmittanceDate() == null;
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.HospitalMapper#getDiagnosisOtherString()
	 */
	@Override
	public String getDiagnosisOtherString() {
		StringBuilder builder = new StringBuilder();

		for(Integer conceptId : diagnosisCodedMap.keySet()) {
			if(!defaultAnswersForDiagnosis.contains(conceptId)) {
				if(builder.length() > 0)
					builder.append("; ");
				builder.append(diagnosisCodedMap.get(conceptId).getValueCoded().getDisplayString());
			}
		}
		
		for(Obs obs : diagnosisNonCodedList) {
			if(builder.length() > 0)
				builder.append("; ");
			builder.append(obs.getValueText());			
		}
		
		return builder.toString();
	}
	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getAdmittanceDate() == null) ? 0 : getAdmittanceDate().hashCode());
		return result;
	}

	
	//i'm making this very lenient -- you can only be in the hospital on a given day once.  If there are different lenghts -- show both as data entry erro?
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		HospitalizationMapping other = (HospitalizationMapping) obj;
		if(getObs() == null || other.getObs() == null)
			return false;
		
//		if(getDate() == null || other.getDate() == null || !getDate().equals(other.getDate()))
//			return false;

//		if(diagnosisCodedMap.size() != other.diagnosisCodedMap.size())
//			return false;
//		
//		for(Integer conceptId : diagnosisCodedMap.keySet()) {
//			if(areObsDifferentValue(diagnosisCodedMap.get(conceptId), other.diagnosisCodedMap.get(conceptId)))
//				return false;
//		}
//		
//		if(diagnosisNonCodedList.size() != other.diagnosisNonCodedList.size())
//			return false;
//		
//		for(int index = 0; index < diagnosisNonCodedList.size(); index++) {
//			if(areObsDifferentValue(diagnosisNonCodedList.get(index), other.diagnosisNonCodedList.get(index)))
//				return false;
//		}

		if(getAdmittanceDate() == null || other.getAdmittanceDate() == null)
			return false;
		if((getAdmittanceDate() != null && other.getAdmittanceDate() != null) && !getAdmittanceDate().equals(other.getAdmittanceDate()))
			return false;
		
		if(getDurationDays() == null || other.getDurationDays() == null)
			return false;
		if((getDurationDays() != null && other.getDurationDays() != null) && !getDurationDays().equals(other.getDurationDays()))
			return false;
		
//		if(getDischargeDate() == null ^ other.getDischargeDate() == null)
//			return false;
//		if((getDischargeDate() != null && other.getDischargeDate() != null) && !getDischargeDate().equals(other.getDischargeDate()))
//			return false;
//		
//		if(getComments() == null ^ other.getComments() == null)
//			return false;
//		if((getComments() != null && other.getComments() != null) && !getComments().equals(other.getComments()))
//			return false;
		
		return true;
	}
	
	@Override
	public int compareTo(HospitalizationMapping obj) {
		if (this == obj)
			return 0;
		if (obj == null)
			return -1;
		if(getAdmittanceDate() == null && obj.getAdmittanceDate() == null)
			return 0;
		if(getAdmittanceDate() == null)
			return 1;
		return getAdmittanceDate().compareTo(obj.getAdmittanceDate());
	}
	
//	@Override
//	public Encounter getEncounter() {
//		if (this.getObs() != null  && this.getObs().getEncounter() != null  && this.getObs().getEncounter().getForm() != null){
//					if (this.getObs().getEncounter().getForm().getFormId().equals(Integer.valueOf(ConceptDictionary.ADULT_HOSPITALISATION_FORM))
//							|| this.getObs().getEncounter().getForm().getFormId().equals(Integer.valueOf(ConceptDictionary.PEDI_HOSPITALISATION_FORM)))
//						return this.getObs().getEncounter();
//		}
//		return null;
//	}


}