package org.openmrs.module.mch.mapper;

import org.openmrs.Obs;

public interface Problem extends BaseObs {

	public abstract Obs getDiagnosis();

	public abstract String getComments();

	public abstract boolean isAsthma();

	public abstract boolean isDiabetes();

	public abstract boolean isEpilepsy();

	public abstract boolean isHeartFailure();

	public abstract boolean isRenalFailure();

	public abstract boolean isOther();

}