package org.openmrs.module.mch.mapper;

import java.util.Date;

import org.openmrs.Encounter;

public interface BaseObs {

	/**
	 * Check if the data exists in the EMR
	 * 
	 * @return true if this obs exists in the EMR otherwise false. 
	 */
	public abstract boolean isEmr();

	/**
	 * Check if the data is voided.
	 * 
	 * @return true if the obs 
	 */
	public abstract boolean isVoided();

	/** 
	 * Get the obs date.  Null if there is no obs. 
	 * 
	 */
	//public abstract Date getObsDate();

	/** 
	 * Get the date of the of the event.  This might be different for example
	 * if this obs is part of a construct.  This is the date to be used for display.
	 * 
	 * @return Date of the event.
	 */
	public abstract Date getDate();

	/**
	 * Encounter for the obs.  
	 * 
	 * @return The encounter.
	 */
	public abstract Encounter getEncounter();

}