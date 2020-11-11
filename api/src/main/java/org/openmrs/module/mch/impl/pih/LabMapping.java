package org.openmrs.module.mch.impl.pih;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.module.mch.mapper.Lab;

/**
 * Obs data can be represented in a number of ways.  
 * <ol>
 * <li> A SGOT lab test might entered directly, so, you would there 
 * would be an obs with a concept id = SGOT (653) and value.  Look at the lab 
 * entry for details on this.</li>
 * <li> SGOT lab test might be directly included in an obs group like 2214 (OTHER LAB TEST CONSTRUCT).
 * See the Diabetes form on synctest is an example of this.</li>
 * <li> SGOT lab test might be part of a construct like 2214 (OTHER LAB TEST CONSTRUCT).
 * The question is TESTS ORDERED (1271) and the value would be SGOT (653).  And the test result
 * is OTHER LAB TEST RESULT (2216).  See the Diabetes form on synctest is an example of this.</li>
 * </ol>
 * <p>
 * This container is meant to make it easier to deal with various ways data can be stored
 * at the UI level.  This class is not efficient and could be designed better.  Refactoring
 * once all the cases are identified should be done.
 * <p>
 * NOTE: This class assumes that any test is not in the object graph more than
 * once.  If there are two representations of SGOT then only the first encountered is used.
 * <p>
 * NOTE: This does not handle obs group embedded within an obs group.  
 */
public class LabMapping extends ObsMapping implements Comparable<LabMapping>, Lab {

	public LabMapping(Obs obs) {
		super(obs);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getDate()
	 */
	@Override
	public Date getDate() {
		
//		return date;
//		if(getObs().getGroupMembers() != null) {
//			for(Obs group : getObs().getGroupMembers()) {
//				if (!group.isVoided()){
//					if(group.getConcept().getConceptId().equals(ConceptDictionary.DATE_OF_LABORATORY_TEST))
//						if(group.getValueDatetime() != null)
//							return group.getValueDatetime();
//				}
//			}
//		}
//		
//		if (getObs().getEncounter() != null && getObs().getEncounter().getEncounterDatetime() != null)
//			return getObs().getEncounter().getEncounterDatetime();
		
		return getObsDate();
	}
	
	private Obs getObsOfType(Integer conceptId) {
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
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getHb()
	 */
	@Override
	public Obs getHb() {
		return getObsOfType(ConceptDictionary.HEMOGLOBIN);
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getHt()
	 */
	@Override
	public Obs getHt() {
		return getObsOfType(ConceptDictionary.HEMATOCRIT);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getGb()
	 */
	@Override
	public Obs getGb() {
		return getObsOfType(ConceptDictionary.WHITE_BLOOD_CELLS);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getNeutro()
	 */
	@Override
	public Obs getNeutro() {
		return getObsOfType(ConceptDictionary.GRANULOCYTE);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getLympho()
	 */
	@Override
	public Obs getLympho() {
		return getObsOfType(ConceptDictionary.ABSOLUTE_LYMPHOCYTE_COUNT);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getPlt()
	 */
	@Override
	public Obs getPlt() {
		return getObsOfType(ConceptDictionary.PLATELETS);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getSgot()
	 */
	@Override
	public Obs getSgot() {
		return getObsOfType(ConceptDictionary.SERUM_GLUTAMIC_OXALOACETIC_TRANSAMINASE);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getSgpt()
	 */
	@Override
	public Obs getSgpt() {
		return getObsOfType(ConceptDictionary.SERUM_GLUTAMIC_PYRUVIC_TRANSAMINASE);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getCreat()
	 */
	@Override
	public Obs getCreat() {
		return getObsOfType(ConceptDictionary.SERUM_CREATININE);
	}

	@Override
	public Obs getCreatClearence()  {
		return getObsOfType(ConceptDictionary.CREATININE_CLEARANCE);
	}


	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getGlucose()
	 */
	@Override
	public Obs getGlucose() {
		return getObsOfType(ConceptDictionary.SERUM_GLUCOSE);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getRpr()
	 */
	@Override
	public Obs getRpr() {
		return getObsOfType(ConceptDictionary.RAPID_PLASMIN_REAGENT);
	}

		@Override
	public Obs getCd4Percentage() {
		return getObsOfType(ConceptDictionary.CD4_PERCENTAGE);
	}
	
//	public boolean isRprPositive() {
//		Obs obs = getRpr();
//		if(obs == null)
//			return false;
//		
//		Concept concept = obs.getValueCoded();
//		if(concept != null && concept.getId() == ConceptDictionary.REACTIVE)
//			return true;
//		
//		return false;
//	}
//	
//	public boolean isRprNegative() {
//		Obs obs = getRpr();
//		if(obs == null)
//			return false;
//		
//		Concept concept = obs.getValueCoded();
//		if(concept != null && concept.getId() == ConceptDictionary.NON_REACTIVE)
//			return true;
//		
//		return false;
//	}
//	
//	public boolean isRprNotMade() {
//		Obs obs = getRpr();
//		if(obs == null)
//			return false;
//		
//		Concept concept = obs.getValueCoded();
//		if(concept != null && concept.getId() == ConceptDictionary.NOT_MADE)
//			return true;
//		
//		return false;
//	}

	
	
	@Override
	public List<Obs> getOtherLabTestName() {
		return getOtherLabTestNameHelper(getObs());
	}
	

	public List<Obs> getOtherLabTestNameHelper(Obs o) {
		Obs ret = null;
		List<Obs> names = new ArrayList<Obs>();
		if(o != null && o.getConcept().getConceptId().equals(ConceptDictionary.OTHER_LAB_TEST_CONSTRUCT)) {
			for(Obs group : o.getGroupMembers()) {
				if (!group.isVoided()){
					if(group.getConcept().getConceptId().equals(ConceptDictionary.TESTS_ORDERED) && group.getValueCoded() != null && !group.getValueCoded().getConceptId().equals(ConceptDictionary.OTHER_NON_CODED)) {
						names.add(group);
					}
					
					if (group.getConcept().getConceptId().equals(ConceptDictionary.OTHER_LAB_TEST_NAME)){
						names.add(group);
					}
				}
			}
			
		}
		
		if (o != null && ret == null && o.getConcept().getConceptId().equals(ConceptDictionary.LABORATORY_EXAMINATIONS_CONSTRUCT)){
			for (Obs oInner : o.getGroupMembers()){
				if (!oInner.isVoided() && oInner.getConcept().getConceptId().equals(ConceptDictionary.OTHER_LAB_TEST_CONSTRUCT))
					names.addAll(getOtherLabTestNameHelper(oInner));
			}
		}
		return names;
	}
	
	@Override
	public List<Obs> getOtherLabTestResult() {
		return getOtherLabTestResultHelper(getObs());
	}
	

	public List<Obs> getOtherLabTestResultHelper(Obs o) {
		List<Obs> obsList = new ArrayList<Obs>();
		if(o != null && o.getConcept().getConceptId().equals(ConceptDictionary.OTHER_LAB_TEST_CONSTRUCT)) {
			for(Obs group : o.getGroupMembers()) {
				if (!group.isVoided()){
					if(group.getConcept().getConceptId().equals(ConceptDictionary.OTHER_LAB_TEST_RESULT))
						obsList.add(group);
				}
			}
		}
		if (o != null && o.getConcept().getConceptId().equals(ConceptDictionary.LABORATORY_EXAMINATIONS_CONSTRUCT)){
			for (Obs oInner : o.getGroupMembers()){
				if (!oInner.isVoided() && oInner.getConcept().getConceptId().equals(ConceptDictionary.OTHER_LAB_TEST_CONSTRUCT)){
					obsList.addAll(getOtherLabTestResultHelper(oInner));
				}
			}
		}
		return obsList;
	}

	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getCd4()
	 */
	@Override
	public Obs getCd4() {
		return getObsOfType(ConceptDictionary.CD4_COUNT);
	}
	
	/* (non-Javadoc)
	 * @see org.openmrs.module.mch.web.controller.LabMapper#getViralLoad()
	 */
	@Override
	public Obs getViralLoad() {
		return getObsOfType(ConceptDictionary.HIV_VIRAL_LOAD);
	}

	@Override
	public int compareTo(LabMapping obj) {
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
//					if (this.getObs().getEncounter().getForm().getFormId().equals(Integer.valueOf(ConceptDictionary.ADULT_LAB_FORM))
//							|| this.getObs().getEncounter().getForm().getFormId().equals(Integer.valueOf(ConceptDictionary.PEDI_LAB_FORM)))
//						return this.getObs().getEncounter();
//		}
//		return null;
//	}
	
}
