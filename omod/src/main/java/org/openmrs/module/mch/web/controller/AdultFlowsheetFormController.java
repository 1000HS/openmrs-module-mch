package org.openmrs.module.mch.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Patient;
import org.openmrs.module.mch.MetadataHelper;
import org.openmrs.module.mch.web.model.AdultFlowsheetFormData;
import org.springframework.validation.Errors;

/**
 * This controller displays a patient summary
 * 
 */
public class AdultFlowsheetFormController extends FlowsheetFormController {

   
    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request, Object obj, Errors err) throws Exception {
        Patient patient = (Patient) obj;
        MetadataHelper.setupConstants();
       
        Map<String, Object> ret = new HashMap<String, Object>();
        //setup reference object
		ret.put("formData", new AdultFlowsheetFormData(patient));

        return ret;
    }
  	
}
