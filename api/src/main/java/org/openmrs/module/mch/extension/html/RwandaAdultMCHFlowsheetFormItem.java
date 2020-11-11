package org.openmrs.module.mch.extension.html;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.module.web.extension.LinkExt;


public class RwandaAdultMCHFlowsheetFormItem extends LinkExt {

	private final static Log log = LogFactory.getLog(RwandaAdultMCHFlowsheetFormItem.class);
	
	/**
	 * @return the message code for this link
	 */
	@Override
	public String getLabel() {
		String ret = "";
		
		if (this.getParameterMap() != null && this.getParameterMap().size() > 0){
		    String patientId = this.getParameterMap().get("patientId");
		    Patient p = Context.getPatientService().getPatient(Integer.valueOf(patientId));
			String gp = Context.getAdministrationService().getGlobalProperty("mch.showPatientChartLinksIfInProgram");
			String gpForAge = Context.getAdministrationService().getGlobalProperty("mch.showPatientChartLinksIfPatientHasAge");
			int patientAge=Integer.parseInt(gpForAge);
			boolean programFound = false;
			if (gp != null && !gp.equals("")){
				String[] gpSplit = gp.split(",");
				for (String str : gpSplit){
					Program prog = Context.getProgramWorkflowService().getProgramByUuid(str);
					if (prog == null)
						prog = Context.getProgramWorkflowService().getProgramByName(str);
					if (prog == null){
						try {
							prog = Context.getProgramWorkflowService().getProgram(Integer.valueOf(str));
						} catch (Exception ex){}
					}
					
					if (prog != null){
						List<PatientProgram> programs = Context.getProgramWorkflowService().getPatientPrograms(p, prog, null, null, null, null, false);
						 if (programs != null && programs.size() > 0 && p.getAge()>=patientAge){
			    	            return "<br/><button>Maternal, Newborn and Child Health</button>";
						 } 
						 programFound = true;
					}
						
				}
			}
			if (!programFound)
				log.warn("mch module: No valid programs were found in the global property mch.showPatientChartLinksIfInProgram.  This means that the mch flowsheets will be available for NO patients.");
		}
		
		return ret;
	}
	
	/**
	 * @return the url to go to
	 */
	@Override
	public String getUrl() {
		return "module/mch/mchflowsheet.form";
	}
	
	/**
     * Returns the required privilege in order to see this section.  Can be a 
     * comma delimited list of privileges.  
     * If the default empty string is returned, only an authenticated 
     * user is required
     * 
     * @return Privilege string
     */
	@Override
	public String getRequiredPrivilege() {
        return "View Patients";
    }
	
}
