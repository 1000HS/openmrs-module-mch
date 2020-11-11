package org.openmrs.module.mch.mapper;

import org.openmrs.Obs;

public interface Visit extends BaseObs {

	public abstract Obs getWeight();

	public abstract Obs getFunctionalAbilityOfThePatient();
	
	public abstract Obs getHeight();
	
	public abstract Obs getZScoreWeight();
	
	public abstract Obs getZScoreHeight();
	
	public abstract Obs getHeightWeightPercentile();
	
	public abstract Obs getPatientInformed();

	public abstract Obs getOI();

	public abstract Obs getSTI();

	public abstract Obs getTBScreening();

	public abstract Obs getFamilyPlanning();

	public abstract Obs getPregnancyStatus();

	public abstract Obs getNumberOfMissedDosesInThePastMonth();

	public abstract Obs getCreat();

	public abstract Obs getReasonForPoorAdherenceToAntiRetoroviralTherapy();
	
	public abstract Obs getNextVisit();

}
