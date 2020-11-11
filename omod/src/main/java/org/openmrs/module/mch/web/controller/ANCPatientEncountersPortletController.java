/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.mch.web.controller;

import org.openmrs.web.controller.PortletController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Controller for the patientEncounters portlet. Provides a map telling which forms have their view
 * and edit links overridden by form entry modules
 */
public class ANCPatientEncountersPortletController extends PortletController {
	
	/**
	 * @see org.openmrs.web.controller.PortletController#populateModel(HttpServletRequest,
	 *      Map)
	 */
	@Override
	protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
		MCHPortletControllerUtil.addFormToEditAndViewUrlMaps(model);
	}
	
}
