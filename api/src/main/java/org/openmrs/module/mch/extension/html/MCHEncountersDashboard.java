/**
 * 
 */
package org.openmrs.module.mch.extension.html;

import org.openmrs.module.web.extension.PatientDashboardTabExt;

/**
 * @author rbcemr
 * 
 */
public class MCHEncountersDashboard extends PatientDashboardTabExt {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openmrs.module.web.extension.PatientDashboardTabExt#getPortletUrl()
	 */
	@Override
	public String getPortletUrl() {
		return "ancPatientEncounters";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openmrs.module.web.extension.PatientDashboardTabExt#getRequiredPrivilege
	 * ()
	 */
	@Override
	public String getRequiredPrivilege() {
		return "Patient Dashboard - View Encounters Section";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openmrs.module.web.extension.PatientDashboardTabExt#getTabId()
	 */
	@Override
	public String getTabId() {
		return "ancPatientEncountersID";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openmrs.module.web.extension.PatientDashboardTabExt#getTabName()
	 */
	@Override
	public String getTabName() {
		return "Maternal and Newborn Encounters";
	}

}
