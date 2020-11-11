package org.openmrs.module.mch.mapper;

import java.util.Date;

public interface Hospitalization extends BaseObs {

	public abstract Date getAdmittanceDate();

	public abstract Date getDischargeDate();

	public abstract String getDurationDays();

	public abstract String getDurationWeeks();

	public abstract String getDurationMonths();

	public abstract String getComments();

	public abstract boolean isGastroenteritis();

	public abstract boolean isMalaria();

	public abstract boolean isMalnutrition();

	public abstract boolean isMeningitis();

	public abstract boolean isPneumonia();

	public abstract boolean isOther();

	public abstract String getDiagnosisOtherString();

}