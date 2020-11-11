package org.openmrs.module.mch.mapper;

import java.util.Date;

public interface Allergy extends BaseObs {

	/**
	 * @return The Adverse Effect Date from the construct or the date of the Obs.
	 */
	public abstract Date getAdverseEffectDate();

	public abstract String getMedicationsString();

	public abstract boolean isEffectAnaphylaxis();

	public abstract boolean isEffectRashMinor();

	public abstract boolean isEffectRashModerate();

	public abstract boolean isEffectRashSevere();

	public abstract boolean isEffectNausea();

	public abstract boolean isEffectVomiting();

	public abstract boolean isEffectJaundice();

	public abstract boolean isEffectPeripheralNeuropathy();

	public abstract boolean isEffectAnemia();

	public abstract boolean isEffectLacticAcidosis();

	public abstract boolean isEffectHepatitis();

	public abstract boolean isEffectNightmares();

	public abstract boolean isEffectLipodystrophy();

	public abstract boolean isEffectOther();

	public abstract String getEffectOtherString();

	public abstract boolean isActionTakenTreatmentStop();

	public abstract boolean isActionTakenChangeOfDose();

	public abstract boolean isActionTakenSymptomatic();

	public abstract boolean isActionTakenOther();

	public abstract String getActionTakenOtherString();

}