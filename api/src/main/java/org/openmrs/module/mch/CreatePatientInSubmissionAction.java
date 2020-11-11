package org.openmrs.module.mch;

import org.openmrs.*;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.CustomFormSubmissionAction;
import org.openmrs.module.htmlformentry.FormEntrySession;

import java.util.*;

public class CreatePatientInSubmissionAction implements CustomFormSubmissionAction {

    private PatientService patientService = Context.getPatientService();

   /* public CreatePatientInSubmissionAction(PatientService patientService) {
        this.patientService = patientService;
    }*/

    @Override
    public void applyAction(FormEntrySession session) {

        String babyFamillyName="";
        String motherFamillyName="";
        String babyGender="";
        Person mother=new Person();


        List<Obs> obs=session.getSubmissionActions().getObsToCreate();

        for (Obs o:obs) {
            if(o.getConcept().getConceptId()==105658){
                babyFamillyName=o.getValueText();
            }
            if(o.getConcept().getConceptId()==8160){
                babyGender=o.getValueCoded().getName().getName();

            }
            motherFamillyName=o.getPerson().getFamilyName();
            mother=o.getPerson();
        }
        if(babyFamillyName.equalsIgnoreCase("")){
            babyFamillyName=motherFamillyName;
        }
        if(babyGender.equalsIgnoreCase("")){
            babyGender="unknown";
        }

        Set<PersonName> pnSet=new TreeSet<PersonName>();
        PersonName pn=new PersonName();
        pn.setFamilyName(babyFamillyName);
        pn.setGivenName("Baby");
        pnSet.add(pn);

        Set<PatientIdentifier> paSet=new TreeSet<PatientIdentifier>();
       // String addIdentifier = PrimaryCareBusinessLogic.getNewPrimaryIdentifierString();
        PatientIdentifier pa=new PatientIdentifier();
        pa.setIdentifierType(Context.getPatientService().getPatientIdentifierType(10));
        pa.setIdentifier(("NB-"+new Date()).replaceAll("\\s", ""));
        pa.setLocation(Context.getLocationService().getLocation(891));
        paSet.add(pa);

        Patient patient=new Patient();
        patient.setIdentifiers(paSet);
        patient.setGender(babyGender);
        patient.setBirthdate(new Date());
        patient.setCreator(Context.getAuthenticatedUser());
        patient.setDateCreated(new Date());
        patient.setNames(pnSet);

        patientService.savePatient(patient);

        Relationship relationshipMotherChild=new Relationship();
        relationshipMotherChild.setRelationshipType(Context.getPersonService().getRelationshipType(9));
        relationshipMotherChild.setPersonA(mother);
        relationshipMotherChild.setPersonB(patient);
        Context.getPersonService().saveRelationship(relationshipMotherChild);


        PatientProgram pp=new PatientProgram();
        pp.setPatient(patient);
        pp.setProgram(Context.getProgramWorkflowService().getProgram(Integer.parseInt(Context.getAdministrationService().getGlobalProperty("mch.program"))));
        pp.setDateEnrolled(new Date());
        Context.getProgramWorkflowService().savePatientProgram(pp);


        /*Date fromDate = new DateTime(session.getSubmissionActions().getCurrentEncounter().getEncounterDatetime()).toDateMidnight().toDate();
        Date toDate = new DateTime(session.getSubmissionActions().getCurrentEncounter().getEncounterDatetime()).withTime(23, 59, 59, 999).toDate();

        // TODO do we need to handle RESCHEDULED here?
        List<Appointment> appointmentList = patientService.getAppointmentsByConstraints(fromDate, toDate,
                session.getSubmissionActions().getCurrentEncounter().getLocation(), null, null, Appointment.AppointmentStatus.SCHEDULED, session.getPatient());

        for (Appointment appointment : appointmentList) {
            appointment.setStatus(Appointment.AppointmentStatus.WAITING);
            patientService.saveAppointment(appointment);
        }*/
    }


}