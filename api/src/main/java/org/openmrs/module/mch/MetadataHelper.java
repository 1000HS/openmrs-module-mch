package org.openmrs.module.mch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.module.mch.impl.pih.ConceptDictionary;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MetadataHelper {
	
	private static Log log = LogFactory.getLog(MetadataHelper.class);
    
    public static List<String> setupConstants(){
    	List<String> unfoundItems = new ArrayList<String>();
        String gpString = Context.getAdministrationService().getGlobalProperty("mch.constants");
        Reader reader = new StringReader(gpString);
        Properties props = new Properties();
        try{
            props.load(reader);
        } catch (Exception ex){
            throw new RuntimeException("invalid values found in global property mch.constants, please correct and try again.");
        } finally {
            reader = null;
        }
        if (props.size() > 0){
        	
        	ConceptDictionary.TESTS_ORDERED_NON_CODED = setup("TESTS_ORDERED_NON_CODED", props, unfoundItems);
        	ConceptDictionary.OIS_THAT_ARE_STIS = setup("OIS_THAT_ARE_STIS", props, unfoundItems);
        	ConceptDictionary.NORMAL = setup("NORMAL", props, unfoundItems);
        	ConceptDictionary.ASSISTANCE_REQUIRED = setup("ASSISTANCE_REQUIRED", props, unfoundItems);
        	ConceptDictionary.BEDRIDDEN = setup("BEDRIDDEN", props, unfoundItems);
        	ConceptDictionary.ABSTINENCE = setup("ABSTINENCE", props, unfoundItems);
        	ConceptDictionary.CONDOMS = setup("CONDOMS", props, unfoundItems);
        	ConceptDictionary.NATURAL_FAMILY_PLANNING = setup("NATURAL_FAMILY_PLANNING", props, unfoundItems);
        	
            ConceptDictionary.ABSOLUTE_LYMPHOCYTE_COUNT = setup("ABSOLUTE_LYMPHOCYTE_COUNT", props, unfoundItems);
            ConceptDictionary.ADVERSE_EFFECT  = setup("ADVERSE_EFFECT", props, unfoundItems);
            ConceptDictionary.ADVERSE_EFFECT_ACTION_TAKEN_NONCODED  = setup("ADVERSE_EFFECT_ACTION_TAKEN_NONCODED", props, unfoundItems);
            ConceptDictionary.ADVERSE_EFFECT_CONSTRUCT  = setup("ADVERSE_EFFECT_CONSTRUCT", props, unfoundItems);
            ConceptDictionary.ADVERSE_EFFECT_DATE  = setup("ADVERSE_EFFECT_DATE", props, unfoundItems);
            ConceptDictionary.ADVERSE_EFFECT_MEDICATION  = setup("ADVERSE_EFFECT_MEDICATION", props, unfoundItems);
            ConceptDictionary.ADVERSE_EFFECT_MEDICATION_NONCODED  = setup("ADVERSE_EFFECT_MEDICATION_NONCODED", props, unfoundItems);
            ConceptDictionary.ADVERSE_EFFECT_NONCODED  = setup("ADVERSE_EFFECT_NONCODED", props, unfoundItems);
            ConceptDictionary.ADVERSE_EVENT_ACTION_TAKEN  = setup("ADVERSE_EVENT_ACTION_TAKEN", props, unfoundItems);
            
            ConceptDictionary.ANAPHYLAXIS  = setup("ANAPHYLAXIS", props, unfoundItems);
            
            ConceptDictionary.ANEMIA = setup("ANEMIA", props, unfoundItems);
            ConceptDictionary.ASSESSMENT_COMMENTS  = setup("ASSESSMENT_COMMENTS", props, unfoundItems);
            ConceptDictionary.ASTHMA  = setup("ASTHMA", props, unfoundItems);
            ConceptDictionary.CD4_COUNT  = setup("CD4_COUNT", props, unfoundItems);
            ConceptDictionary.CHANGE_OF_DOSAGE  = setup("CHANGE_OF_DOSAGE", props, unfoundItems);
            ConceptDictionary.CHEST_XRAY  = setup("CHEST_XRAY", props, unfoundItems);
            ConceptDictionary.CHEST_XRAY_CONSTRUCT  = setup("CHEST_XRAY_CONSTRUCT", props, unfoundItems);
            ConceptDictionary.CHRONIC_CARE_CONSTRUCT  = setup("CHRONIC_CARE_CONSTRUCT", props, unfoundItems);
            ConceptDictionary.CHRONIC_CARE_DIAGNOSIS = setup("CHRONIC_CARE_DIAGNOSIS", props, unfoundItems);
            ConceptDictionary.CHRONIC_CARE_DIAGNOSIS_OTHER = setup("CHRONIC_CARE_DIAGNOSIS_OTHER", props, unfoundItems);
            
            ConceptDictionary.CLINICAL_IMPRESSION_COMMENTS  = setup("CLINICAL_IMPRESSION_COMMENTS", props, unfoundItems);
            ConceptDictionary.COMPUTED_TOMOGRAPHY_SCAN_HEAD  = setup("COMPUTED_TOMOGRAPHY_SCAN_HEAD", props, unfoundItems);
            ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION  = setup("CURRENT_OPPORTUNISTIC_INFECTION", props, unfoundItems);
            ConceptDictionary.Current_opportunistic_infection_construct  = setup("Current_opportunistic_infection_construct", props, unfoundItems);
            ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED  = setup("CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED", props, unfoundItems);
            ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED_NON_CODED  = setup("CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED_NON_CODED", props, unfoundItems);
            
            ConceptDictionary.CURRENT_OI_CARDIOVASCULAR_DISEASE  = setup("CURRENT_OI_CARDIOVASCULAR_DISEASE", props, unfoundItems);
            ConceptDictionary.CURRENT_OI_CEREBRAL_LYMPHOMA  = setup("CURRENT_OI_CEREBRAL_LYMPHOMA", props, unfoundItems);
            ConceptDictionary.CURRENT_OI_DEPRESSION  = setup("CURRENT_OI_DEPRESSION", props, unfoundItems);
            ConceptDictionary.CURRENT_OI_EXTRAPULMONARY_TUBERCULOSIS  = setup("CURRENT_OI_EXTRAPULMONARY_TUBERCULOSIS", props, unfoundItems);
            ConceptDictionary.CURRENT_OI_KAPOSIS_SARCOMA  = setup("CURRENT_OI_KAPOSIS_SARCOMA", props, unfoundItems);
            ConceptDictionary.CURRENT_OI_NEUROLOGICAL_DEFICIT  = setup("CURRENT_OI_NEUROLOGICAL_DEFICIT", props, unfoundItems);
            ConceptDictionary.CURRENT_OI_PULMONARY_TUBERCULOSIS  = setup("CURRENT_OI_PULMONARY_TUBERCULOSIS", props, unfoundItems);
            ConceptDictionary.CURRENT_OI_TUBERCULOSIS  = setup("CURRENT_OI_TUBERCULOSIS", props, unfoundItems);
            ConceptDictionary.CURRENT_OI_WEIGHT_LOSS_GREATER_THAN_10_PERCENT  = setup("CURRENT_OI_WEIGHT_LOSS_GREATER_THAN_10_PERCENT", props, unfoundItems);
            
            ConceptDictionary.DATE_OF_GENERAL_TEST  = setup("DATE_OF_GENERAL_TEST", props, unfoundItems);
            ConceptDictionary.DATE_OF_HOSPITAL_ADMITTANCE  = setup("DATE_OF_HOSPITAL_ADMITTANCE", props, unfoundItems);
            ConceptDictionary.DATE_OF_LABORATORY_TEST  = setup("DATE_OF_LABORATORY_TEST", props, unfoundItems);
            ConceptDictionary.DAYS  = setup("DAYS", props, unfoundItems);
            ConceptDictionary.DIABETES  = setup("DIABETES", props, unfoundItems);
            ConceptDictionary.DIABETES_MELLITUS  = setup("DIABETES_MELLITUS", props, unfoundItems);
            ConceptDictionary.DIAGNOSIS_WHILE_HOSPITALIZED  = setup("DIAGNOSIS_WHILE_HOSPITALIZED", props, unfoundItems);
            ConceptDictionary.DIAGNOSIS_WHILE_HOSPITALIZED_NON_CODED  = setup("DIAGNOSIS_WHILE_HOSPITALIZED_NON_CODED", props, unfoundItems);
            ConceptDictionary.DRUG_3TC = setup("DRUG_3TC", props, unfoundItems);
            ConceptDictionary.DRUG_ABC  = setup("DRUG_ABC", props, unfoundItems);
            ConceptDictionary.DRUG_AZT  = setup("DRUG_AZT", props, unfoundItems);
            ConceptDictionary.DRUG_AZT_3TC  = setup("DRUG_AZT_3TC", props, unfoundItems);
            ConceptDictionary.DRUG_AZT_3TC_AND_ABC  = setup("DRUG_AZT_3TC_AND_ABC", props, unfoundItems);
            ConceptDictionary.DRUG_AZT_3TC_EFV  = setup("DRUG_AZT_3TC_EFV", props, unfoundItems);
            ConceptDictionary.DRUG_AZT_3TC_LORR  = setup("DRUG_AZT_3TC_LORR", props, unfoundItems);
            ConceptDictionary.DRUG_AZT_3TC_NVP  = setup("DRUG_AZT_3TC_NVP", props, unfoundItems);
            ConceptDictionary.DRUG_D4T  = setup("DRUG_D4T", props, unfoundItems);
            ConceptDictionary.DRUG_D4T_3TC = setup("DRUG_D4T_3TC", props, unfoundItems);
            ConceptDictionary.DRUG_D4T_3TC_EFV = setup("DRUG_D4T_3TC_EFV", props, unfoundItems);
            ConceptDictionary.DRUG_D4T_3TC_NVP  = setup("DRUG_D4T_3TC_NVP", props, unfoundItems);
            ConceptDictionary.DRUG_D4T30_3TC_EFV  = setup("DRUG_D4T30_3TC_EFV", props, unfoundItems);
            ConceptDictionary.DRUG_D4T30_3TC_NVP  = setup("DRUG_D4T30_3TC_NVP", props, unfoundItems);
            ConceptDictionary.DRUG_D4T40_3TC_EFV  = setup("DRUG_D4T40_3TC_EFV", props, unfoundItems);
            ConceptDictionary.DRUG_D4T40_3TC_NVP  = setup("DRUG_D4T40_3TC_NVP", props, unfoundItems);
            ConceptDictionary.DRUG_DDI  = setup("DRUG_DDI", props, unfoundItems);
            ConceptDictionary.DRUG_DDI_3TC_LOPR  = setup("DRUG_DDI_3TC_LOPR", props, unfoundItems);
            ConceptDictionary.DRUG_DDI_ABC_LOPR  = setup("DRUG_DDI_ABC_LOPR", props, unfoundItems);
            ConceptDictionary.DRUG_DDI_AZT_LOPR  = setup("DRUG_DDI_AZT_LOPR", props, unfoundItems);
            ConceptDictionary.DRUG_DDI_TDF_LOPR  = setup("DRUG_DDI_TDF_LOPR", props, unfoundItems);
            ConceptDictionary.DRUG_DIDANOSINE_ABACAVIR_LOPINAVIR_RITONAVIR  = setup("DRUG_DIDANOSINE_ABACAVIR_LOPINAVIR_RITONAVIR", props, unfoundItems);
            ConceptDictionary.DRUG_EFV  = setup("DRUG_EFV", props, unfoundItems);
            ConceptDictionary.DRUG_INFANT_NVP  = setup("DRUG_INFANT_NVP", props, unfoundItems);
            ConceptDictionary.DRUG_LOPINAVIR_AND_RITONAVIR  = setup("DRUG_LOPINAVIR_AND_RITONAVIR", props, unfoundItems);
            ConceptDictionary.DRUG_NVP  = setup("DRUG_NVP", props, unfoundItems);
            ConceptDictionary.DRUG_NVP_UNIQUE_DOSE  = setup("DRUG_NVP_UNIQUE_DOSE", props, unfoundItems);
            ConceptDictionary.DRUG_TDF  = setup("DRUG_TDF", props, unfoundItems);
            ConceptDictionary.DRUG_TDF_3TC_EFV  = setup("DRUG_TDF_3TC_EFV", props, unfoundItems);
            ConceptDictionary.DRUG_TDF_3TC_LOPR  = setup("DRUG_TDF_3TC_LOPR", props, unfoundItems);
            ConceptDictionary.DRUG_TDF_3TC_NVP  = setup("DRUG_TDF_3TC_NVP", props, unfoundItems);
            ConceptDictionary.DRUG_TDF_ABC_LOPR  = setup("DRUG_TDF_ABC_LOPR", props, unfoundItems);
            ConceptDictionary.DRUG_TDF_AZT_LOPR  = setup("DRUG_TDF_AZT_LOPR", props, unfoundItems);
            ConceptDictionary.DRUG_TDF_FTC  = setup("DRUG_TDF_FTC", props, unfoundItems);
            ConceptDictionary.DRUG_TDF_FTC_NVP  = setup("DRUG_TDF_FTC_NVP", props, unfoundItems);
            ConceptDictionary.DRUG_TNF_3TC  = setup("DRUG_TNF_3TC", props, unfoundItems);
            ConceptDictionary.DRUG_UNAVAILABLE  = setup("DRUG_UNAVAILABLE", props, unfoundItems);
            
            ConceptDictionary.DRUG_COTRIMOXAZOLE  = setup("DRUG_COTRIMOXAZOLE", props, unfoundItems);
            ConceptDictionary.DRUG_FLUCONAZOLE  = setup("DRUG_FLUCONAZOLE", props, unfoundItems);
            ConceptDictionary.DRUG_DAPSONE  = setup("DRUG_DAPSONE", props, unfoundItems);
            
            ConceptDictionary.DRUG_WITHDRAWN  = setup("DRUG_WITHDRAWN", props, unfoundItems);
            ConceptDictionary.Duration_of_hospitalization  = setup("Duration_of_hospitalization", props, unfoundItems);
            ConceptDictionary.DURATION_OF_HOSPITALIZATION_CONSTRUCT  = setup("DURATION_OF_HOSPITALIZATION_CONSTRUCT", props, unfoundItems);
            ConceptDictionary.EPILEPSY  = setup("EPILEPSY", props, unfoundItems);
            ConceptDictionary.FREE_TEXT_REASON_FOR_POOR_ADHERENCE_TO_ANTIRETROVIRAL_THERAPY  = setup("FREE_TEXT_REASON_FOR_POOR_ADHERENCE_TO_ANTIRETROVIRAL_THERAPY", props, unfoundItems);
            ConceptDictionary.FUNCTIONAL_ABILITY_OF_THE_PATIENT  = setup("FUNCTIONAL_ABILITY_OF_THE_PATIENT", props, unfoundItems);
            ConceptDictionary.GASTROENTERITIS  = setup("GASTROENTERITIS", props, unfoundItems);
            ConceptDictionary.GRANULOCYTE  = setup("GRANULOCYTE", props, unfoundItems);
            ConceptDictionary.HEART_FAILURE  = setup("HEART_FAILURE", props, unfoundItems);
            ConceptDictionary.HEIGHT_CM  = setup("HEIGHT_CM", props, unfoundItems);
            ConceptDictionary.HEMATOCRIT  = setup("HEMATOCRIT", props, unfoundItems);
            ConceptDictionary.HEMOGLOBIN  = setup("HEMOGLOBIN", props, unfoundItems);
            ConceptDictionary.HEPATITIS  = setup("HEPATITIS", props, unfoundItems);
            ConceptDictionary.HIV_VIRAL_LOAD  = setup("HIV_VIRAL_LOAD", props, unfoundItems);
            ConceptDictionary.HOSPITALIZATION_COMMENT  = setup("HOSPITALIZATION_COMMENT", props, unfoundItems);
            ConceptDictionary.HOSPITALIZATION_CONSTRUCT  = setup("HOSPITALIZATION_CONSTRUCT", props, unfoundItems);
            ConceptDictionary.HOSPITALIZATION_DISCHARGE_DATE  = setup("HOSPITALIZATION_DISCHARGE_DATE", props, unfoundItems);
            ConceptDictionary.INTERACTION_WITH_TUBERCULOSIS_DRUG  = setup("INTERACTION_WITH_TUBERCULOSIS_DRUG", props, unfoundItems);
            ConceptDictionary.JAUNDICE  = setup("JAUNDICE", props, unfoundItems);
            ConceptDictionary.LABORATORY_EXAMINATIONS_CONSTRUCT  = setup("LABORATORY_EXAMINATIONS_CONSTRUCT", props, unfoundItems);
            ConceptDictionary.LACTIC_ACIDOSIS  = setup("LACTIC_ACIDOSIS", props, unfoundItems);
            ConceptDictionary.LIPODYSTROPHY  = setup("LIPODYSTROPHY", props, unfoundItems);
            ConceptDictionary.MALARIA  = setup("MALARIA", props, unfoundItems);
            ConceptDictionary.MALNUTRITION  = setup("MALNUTRITION", props, unfoundItems);
            ConceptDictionary.MEDICAL_IMAGE_CONSTRUCT  = setup("MEDICAL_IMAGE_CONSTRUCT", props, unfoundItems);
            ConceptDictionary.MENINGITIS  = setup("MENINGITIS", props, unfoundItems);
            ConceptDictionary.METHOD_OF_FAMILY_PLANNING  = setup("METHOD_OF_FAMILY_PLANNING", props, unfoundItems);
            ConceptDictionary.MONTHS  = setup("MONTHS", props, unfoundItems);
            ConceptDictionary.NAUSEA  = setup("NAUSEA", props, unfoundItems);
            ConceptDictionary.NEGATIVE  = setup("NEGATIVE", props, unfoundItems);
            ConceptDictionary.NIGHTMARES  = setup("NIGHTMARES", props, unfoundItems);
            ConceptDictionary.NO  = setup("NO", props, unfoundItems);
            ConceptDictionary.NON_REACTIVE  = setup("NON_REACTIVE", props, unfoundItems);
            ConceptDictionary.NONE  = setup("NONE", props, unfoundItems);
            ConceptDictionary.NOT_DONE  = setup("NOT_DONE", props, unfoundItems);
            ConceptDictionary.NUMBER_OF_DOSES_OF_ANTIRETROVIRALS_MISSED_IN_THE_PAST_MONTH  = setup("NUMBER_OF_DOSES_OF_ANTIRETROVIRALS_MISSED_IN_THE_PAST_MONTH", props, unfoundItems);
            ConceptDictionary.OTHER_LAB_TEST_CONSTRUCT  = setup("OTHER_LAB_TEST_CONSTRUCT", props, unfoundItems);
            ConceptDictionary.OTHER_LAB_TEST_NAME  = setup("OTHER_LAB_TEST_NAME", props, unfoundItems);
            ConceptDictionary.OTHER_LAB_TEST_RESULT  = setup("OTHER_LAB_TEST_RESULT", props, unfoundItems);
            ConceptDictionary.OTHER_NON_CODED  = setup("OTHER_NON_CODED", props, unfoundItems);
            ConceptDictionary.OUTPATIENT_DIAGNOSIS  = setup("OUTPATIENT_DIAGNOSIS", props, unfoundItems);
            ConceptDictionary.CURRENT_OI = setup("OPPORTUNISTIC_INFECTION", props, unfoundItems);
            ConceptDictionary.PATIENT_DEFAULTED  = setup("PATIENT_DEFAULTED", props, unfoundItems);
            ConceptDictionary.PATIENT_DIED  = setup("PATIENT_DIED", props, unfoundItems);
            ConceptDictionary.PATIENT_HAD_SIDE_EFFECTS  = setup("PATIENT_HAD_SIDE_EFFECTS", props, unfoundItems);
            
            ConceptDictionary.PATIENT_HAS_DIABETES   = setup("PATIENT_HAS_DIABETES", props, unfoundItems);
            ConceptDictionary.PATIENT_PREGNANT   = setup("PATIENT_PREGNANT", props, unfoundItems);
            ConceptDictionary.PATIENT_VISIT_CONSTRUCT   = setup("PATIENT_VISIT_CONSTRUCT", props, unfoundItems);
            ConceptDictionary.PERIPHERAL_NEUROPATHY   = setup("PERIPHERAL_NEUROPATHY", props, unfoundItems);
            ConceptDictionary.PLATELETS   = setup("PLATELETS", props, unfoundItems);
            ConceptDictionary.PNEUMONIA   = setup("PNEUMONIA", props, unfoundItems);
            ConceptDictionary.POSITIVE   = setup("POSITIVE", props, unfoundItems);
            ConceptDictionary.PREGNANCY_STATUS   = setup("PREGNANCY_STATUS", props, unfoundItems);
            ConceptDictionary.PREVIOUS_DIAGNOSIS   = setup("PREVIOUS_DIAGNOSIS", props, unfoundItems);
            ConceptDictionary.PREVIOUS_DIAGNOSIS_CONSTRUCT   = setup("PREVIOUS_DIAGNOSIS_CONSTRUCT", props, unfoundItems);
            ConceptDictionary.PREVIOUS_DIAGNOSIS_DATE   = setup("PREVIOUS_DIAGNOSIS_DATE", props, unfoundItems);
            ConceptDictionary.PULMONARY_EFFUSION   = setup("PULMONARY_EFFUSION", props, unfoundItems);
            ConceptDictionary.RAPID_PLASMIN_REAGENT   = setup("RAPID_PLASMIN_REAGENT", props, unfoundItems);
            ConceptDictionary.RASH_MINOR   = setup("RASH_MINOR", props, unfoundItems);
            ConceptDictionary.RASH_MODERATE   = setup("RASH_MODERATE", props, unfoundItems);
            ConceptDictionary.RASH_SEVERE   = setup("RASH_SEVERE", props, unfoundItems);
            ConceptDictionary.REACTIVE   = setup("REACTIVE", props, unfoundItems);
            ConceptDictionary.REGIMEN_FAILURE   = setup("REGIMEN_FAILURE", props, unfoundItems);
            ConceptDictionary.RENAL_FAILURE   = setup("RENAL_FAILURE", props, unfoundItems);
            ConceptDictionary.RESULT_OF_TUBERCULOSIS_SCREENING_QUALITATIVE   = setup("RESULT_OF_TUBERCULOSIS_SCREENING_QUALITATIVE", props, unfoundItems);

            ConceptDictionary.RESULT_OF_TB_SMEAR  = setup("RESULT_OF_TB_SMEAR", props, unfoundItems);
            ConceptDictionary.RESULT_OF_TB_SMEAR_POSITIVE = setup("RESULT_OF_TB_SMEAR_POSITIVE", props, unfoundItems);
            ConceptDictionary.RESULT_OF_TB_SMEAR_WEAKLY_POSITIVE = setup("RESULT_OF_TB_SMEAR_WEAKLY_POSITIVE", props, unfoundItems);
            ConceptDictionary.RESULT_OF_TB_SMEAR_MODERATELY_POSITIVE = setup("RESULT_OF_TB_SMEAR_MODERATELY_POSITIVE", props, unfoundItems);
            ConceptDictionary.RESULT_OF_TB_SMEAR_STRONGLY_POSITIVE = setup("RESULT_OF_TB_SMEAR_STRONGLY_POSITIVE", props, unfoundItems);
            ConceptDictionary.RESULT_OF_TB_SMEAR_NEGATIVE  = setup("RESULT_OF_TB_SMEAR_NEGATIVE", props, unfoundItems);
            
            ConceptDictionary.SERUM_CREATININE   = setup("SERUM_CREATININE", props, unfoundItems);

            ConceptDictionary.CREATININE_CLEARANCE   = setup("CREATININE_CLEARANCE", props, unfoundItems);

            ConceptDictionary.SERUM_GLUCOSE   = setup("SERUM_GLUCOSE", props, unfoundItems);
            ConceptDictionary.SERUM_GLUTAMIC_OXALOACETIC_TRANSAMINASE   = setup("SERUM_GLUTAMIC_OXALOACETIC_TRANSAMINASE", props, unfoundItems);
            ConceptDictionary.SERUM_GLUTAMIC_PYRUVIC_TRANSAMINASE   = setup("SERUM_GLUTAMIC_PYRUVIC_TRANSAMINASE", props, unfoundItems);
            ConceptDictionary.SEVERITY_OF_CARDIOMEGALY   = setup("SEVERITY_OF_CARDIOMEGALY", props, unfoundItems);
            ConceptDictionary.SEXUALLY_TRANSMITTED_INFECTION_SYMPTOMS_COMMENT   = setup("SEXUALLY_TRANSMITTED_INFECTION_SYMPTOMS_COMMENT", props, unfoundItems);
            ConceptDictionary.SYMPTOMATIC   = setup("SYMPTOMATIC", props, unfoundItems);
            
            ConceptDictionary.TB_DRUG_ISONIAZID_PROPHYLAXIS = setup("TB_DRUG_ISONIAZID_PROPHYLAXIS",props, unfoundItems);
            ConceptDictionary.TB_DRUG_RHEZ= setup("TB_DRUG_RHEZ",props, unfoundItems);
            ConceptDictionary.TB_DRUG_RH= setup("TB_DRUG_RH",props, unfoundItems);
            ConceptDictionary.TB_DRUG_RHZ= setup("TB_DRUG_RHZ",props, unfoundItems);
            ConceptDictionary.TB_DRUG_RHE= setup("TB_DRUG_RHE",props, unfoundItems);
            ConceptDictionary.TB_TREATMENT_TYPE= setup("TB_TREATMENT_TYPE",props, unfoundItems);
            
            ConceptDictionary.TB_TREATMENT_TYPE_INITIAL= setup("TB_TREATMENT_TYPE_INITIAL",props, unfoundItems);
            ConceptDictionary.TB_TREATMENT_TYPE_MDRTB= setup("TB_TREATMENT_TYPE_MDRTB",props, unfoundItems);
            ConceptDictionary.TB_TREATMENT_TYPE_RETREATMENT= setup("TB_TREATMENT_TYPE_RETREATMENT",props, unfoundItems);
            
            ConceptDictionary.TB_TREATMENT_OUTCOME_GUERI_CURED= setup("TB_TREATMENT_OUTCOME_GUERI_CURED",props, unfoundItems);
            ConceptDictionary.TB_TREATMENT_OUTCOME_ECHEC_FAILED= setup("TB_TREATMENT_OUTCOME_ECHEC_FAILED",props, unfoundItems);
            ConceptDictionary.TB_TREATMENT_OUTCOME_COMPLETE= setup("TB_TREATMENT_OUTCOME_COMPLETE",props, unfoundItems);
            ConceptDictionary.TB_TREATMENT_OUTCOME_ABAONDONED= setup("TB_TREATMENT_OUTCOME_ABAONDONED",props, unfoundItems);
            ConceptDictionary.TB_TREATMENT_OUTCOME_TRANSFERED= setup("TB_TREATMENT_OUTCOME_TRANSFERED",props, unfoundItems);
            ConceptDictionary.TB_TREATMENT_OUTCOME_DIED= setup("TB_TREATMENT_OUTCOME_DIED",props, unfoundItems);
            
            ConceptDictionary.TESTS_ORDERED   = setup("TESTS_ORDERED", props, unfoundItems);
            ConceptDictionary.TIME_UNITS   = setup("TIME_UNITS", props, unfoundItems);
            ConceptDictionary.TREATMENT_GROUP = setup("TREATMENT_GROUP", props, unfoundItems);
            ConceptDictionary.VOMITING   = setup("VOMITING", props, unfoundItems);
            ConceptDictionary.WEEKS   = setup("WEEKS", props, unfoundItems);
            ConceptDictionary.WEIGHT_KG   = setup("WEIGHT_KG", props, unfoundItems);
            ConceptDictionary.WHITE_BLOOD_CELLS   = setup("WHITE_BLOOD_CELLS", props, unfoundItems);
            ConceptDictionary.WHO_STAGE   = setup("WHO_STAGE", props, unfoundItems);
            
            ConceptDictionary.XRAY_CHEST    = setup("XRAY_CHEST", props, unfoundItems);
            ConceptDictionary.YES    = setup("YES", props, unfoundItems);
            ConceptDictionary.Z_SCORE_WEIGHT   = setup("Z_SCORE_WEIGHT", props, unfoundItems); 
            ConceptDictionary.Z_SCORE_HEIGHT   = setup("Z_SCORE_HEIGHT", props, unfoundItems); 
            
            ConceptDictionary.PatientIdentiferId_Tracnet    = setupPatientIdentifierType("PatientIdentiferId_Tracnet", props);
            ConceptDictionary.ProgramId_AdultHIVProgram    = setupProgram("ProgramId_AdultHIVProgram", props, unfoundItems);
            ConceptDictionary.ProgramId_PediHIVProgram    = setupProgram("ProgramId_PediHIVProgram", props, unfoundItems);
            ConceptDictionary.ProgramId_PMTCT_mother    = setupProgram("ProgramId_PMTCT_mother", props, unfoundItems);
            ConceptDictionary.ProgramId_Exposed_Infant_mother    = setupProgram("ProgramId_Exposed_Infant_mother", props, unfoundItems);
            
          
            
            ConceptDictionary.ADULT_FLOWSHEET_ENCOUNTER_ID    = setupEncounterTypeId("ADULT_FLOWSHEET_ENCOUNTER_ID", props);
            ConceptDictionary.PEDI_FLOWSHEET_ENCOUNTER_ID    = setupEncounterTypeId("PEDI_FLOWSHEET_ENCOUNTER_ID", props);
            
            ConceptDictionary.HEIGHT_WEIGHT_PERCENTILE   = setup("HEIGHT_WEIGHT_PERCENTILE", props, unfoundItems);
            ConceptDictionary.PATIENT_INFORMED   = setup("PATIENT_INFORMED", props, unfoundItems); 
            ConceptDictionary.PATIENT_NOT_INFORMED   = setup("PATIENT_NOT_INFORMED", props, unfoundItems); 
            
            ConceptDictionary.PROPHYLAXIS_REASON_FOR_STOPPING_ABANDONED    = setup("PROPHYLAXIS_REASON_FOR_STOPPING_ABANDONED", props, unfoundItems);
            ConceptDictionary.PROPHYLAXIS_REASON_FOR_STOPPING_CD4_IMPROVEMENT   = setup("PROPHYLAXIS_REASON_FOR_STOPPING_CD4_IMPROVEMENT", props, unfoundItems);
            ConceptDictionary.PROPHYLAXIS_REASON_FOR_STOPPING_OUT_OF_STOCK    = setup("PROPHYLAXIS_REASON_FOR_STOPPING_OUT_OF_STOCK", props, unfoundItems);
            ConceptDictionary.PROPHYLAXIS_REASON_FOR_STOPPING_TOXICITY    = setup("PROPHYLAXIS_REASON_FOR_STOPPING_TOXICITY", props, unfoundItems);
            
            ConceptDictionary.CD4_PERCENTAGE   = setup("CD4_PERCENTAGE", props, unfoundItems); 
            
            ConceptDictionary.ABDOMINAL_ULTRASOUND   = setup("ABDOMINAL_ULTRASOUND", props, unfoundItems);
            
            ConceptDictionary.HIV_DIAGNOSIS_DATE    = setup("HIV_DIAGNOSIS_DATE", props, unfoundItems);
            
            ConceptDictionary.TB_DRUG_AMOX_CLAV    = setup("TB_DRUG_AMOX_CLAV", props, unfoundItems);
            
            ConceptDictionary.TREATMENT_COMPLETE    = setup("TREATMENT_COMPLETE", props, unfoundItems);
            
            ConceptDictionary.INFORMED_STATUS    = setup("INFORMED_STATUS", props, unfoundItems);
            
            ConceptDictionary.NEXT_VISIT    = setup("NEXT_VISIT", props, unfoundItems);
            
            ConceptDictionary.OPPORTUNISTIC_INFECTION    = setup("OPPORTUNISTIC_INFECTIONS", props, unfoundItems);
            
            ConceptDictionary.OPPORTUNISTIC_INFECTION_NON_CODED    = setup("OPPORTUNISTIC_INFECTIONS_NON_CODED", props, unfoundItems);
            
            ConceptDictionary.OPPORTUNISTIC_INFECTION_SET    = setup("OPPORTUNISTIC_INFECTIONS_SET", props, unfoundItems);
            
            ConceptDictionary.OPPORTUNISTIC_INFECTION_START_DATE    = setup("OPPORTUNISTIC_INFECTIONS_START_DATE", props, unfoundItems);
            
            ConceptDictionary.OPPORTUNISTIC_INFECTION_COMMENTS    = setup("OPPORTUNISTIC_INFECTIONS_COMMENTS", props, unfoundItems);
            
            ConceptDictionary.STI_INFECTION    = setup("STI_INFECTIONS", props, unfoundItems);
            
            ConceptDictionary.STI_INFECTION_NON_CODED    = setup("STI_INFECTIONS_NON_CODED", props, unfoundItems);
            
            ConceptDictionary.STI_SET    = setup("STI_SET", props, unfoundItems);
            
            ConceptDictionary.ADULT_ALLERGY_FORM = setupForm("rwandaadulthivflowsheet.Form_NewAllergy");
            ConceptDictionary.ADULT_OI_FORM = setupForm("rwandaadulthivflowsheet.Form_NewOI");
            ConceptDictionary.ADULT_HOSPITALISATION_FORM = setupForm("rwandaadulthivflowsheet.Form_NewHospitalisations");
            ConceptDictionary.ADULT_PROBLEM_FORM = setupForm("rwandaadulthivflowsheet.Form_NewProblem");
            ConceptDictionary.ADULT_VISIT_FORM = setupForm("rwandaadulthivflowsheet.Form_NewVisit");
            ConceptDictionary.ADULT_LAB_FORM = setupForm("rwandaadulthivflowsheet.Form_NewLab");
            ConceptDictionary.ADULT_IMAGE_FORM = setupForm("rwandaadulthivflowsheet.Form_NewImage");
            ConceptDictionary.PEDI_ALLERGY_FORM = setupForm("rwandapedihivflowsheet.Form_NewAllergy");
            ConceptDictionary.PEDI_OI_FORM = setupForm("rwandapedihivflowsheet.Form_NewOI");
            ConceptDictionary.PEDI_HOSPITALISATION_FORM = setupForm("rwandapedihivflowsheet.Form_NewHospitalisations");
            ConceptDictionary.PEDI_PROBLEM_FORM = setupForm("rwandapedihivflowsheet.Form_NewProblem");
            ConceptDictionary.PEDI_VISIT_FORM = setupForm("rwandapedihivflowsheet.Form_NewVisit");
            ConceptDictionary.PEDI_LAB_FORM = setupForm("rwandapedihivflowsheet.Form_NewLab");
            ConceptDictionary.PEDI_IMAGE_FORM = setupForm("rwandapedihivflowsheet.Form_NewImage");
            ConceptDictionary.ANC_FORM = setupForm("mch.Form_ANC");
            /*ConceptDictionary.ASRH = setupForm("mch.Form_ASRH");
            ConceptDictionary.ECD = setupForm("mch.Form_ECD");
            ConceptDictionary.EPI = setupForm("mch.Form_EPI");
            ConceptDictionary.FP = setupForm("mch.Form_FP");
            ConceptDictionary.GBV = setupForm("mch.Form_GBV");
            ConceptDictionary.GMP = setupForm("mch.Form_GMP");
            ConceptDictionary.IMNCI = setupForm("mch.Form_IMNCI");
            ConceptDictionary.INTRAPARTUM = setupForm("mch.Form_INTRAPARTUM");
            ConceptDictionary.KMC = setupForm("mch.Form_KMC");
            ConceptDictionary.NEWBORNTRANSFER = setupForm("mch.Form_NEWBORNTRANSFER");*/
            ConceptDictionary.PNC_FORM = setupForm("mch.Form_PNC");

            ConceptDictionary.LMP    = setup("LMP", props, unfoundItems);
            ConceptDictionary.GRAVIDITY = setup("GRAVIDITY", props, unfoundItems);
            ConceptDictionary.EXPECTED_DUE_DATE = setup("EXPECTED_DUE_DATE", props, unfoundItems);
            ConceptDictionary.DURATION_OF_PREGNANCY = setup("DURATION_OF_PREGNANCY", props, unfoundItems);
            ConceptDictionary.PATIENT_ARRIVED_WITH_PARTNER = setup("PATIENT_ARRIVED_WITH_PARTNER", props, unfoundItems);
            ConceptDictionary.PARITY = setup("PARITY", props, unfoundItems);
            ConceptDictionary.NUMBER_OF_ABORTIONS = setup("NUMBER_OF_ABORTIONS", props, unfoundItems);
            ConceptDictionary.NUMBER_OF_CESAREAN_SECTIONS = setup("NUMBER_OF_CESAREAN_SECTIONS", props, unfoundItems);
            ConceptDictionary.OBSTETRIC_HISTORY = setup("OBSTETRIC_HISTORY", props, unfoundItems);
            ConceptDictionary.CURRENT_PROBLEMS = setup("CURRENT_PROBLEMS", props, unfoundItems);
            ConceptDictionary.DANGER_SIGNS = setup("DANGER_SIGNS", props, unfoundItems);
            ConceptDictionary.MEDICAL_HISTORY = setup("MEDICAL_HISTORY", props, unfoundItems);
            ConceptDictionary.TEMPERATURE = setup("TEMPERATURE", props, unfoundItems);
            ConceptDictionary.DIASTOLIC_BLOOD_PRESSURE = setup("DIASTOLIC_BLOOD_PRESSURE", props, unfoundItems);
            ConceptDictionary.SYSTOLIC_BLOOD_PRESSURE = setup("SYSTOLIC_BLOOD_PRESSURE", props, unfoundItems);
            ConceptDictionary.PULSE = setup("PULSE", props, unfoundItems);
            ConceptDictionary.GENITAL_EXAMINATION = setup("GENITAL_EXAMINATION", props, unfoundItems);
            ConceptDictionary.ABNORMAL = setup("ABNORMAL", props, unfoundItems);
            ConceptDictionary.IS_BABY_MOVING = setup("IS_BABY_MOVING", props, unfoundItems);
            ConceptDictionary.DOES_FUNDAL_HEIGHT_MATCH_GESTATIONAL_AGE = setup("DOES_FUNDAL_HEIGHT_MATCH_GESTATIONAL_AGE", props, unfoundItems);
            ConceptDictionary.FETAL_POSITION = setup("FETAL_POSITION", props, unfoundItems);
            ConceptDictionary.FETAL_HEART = setup("FETAL_HEART", props, unfoundItems);
            ConceptDictionary.RESPIRATION_MOVEMENTS = setup("RESPIRATION_MOVEMENTS", props, unfoundItems);
            ConceptDictionary.FETAL_TONE = setup("FETAL_TONE", props, unfoundItems);
            ConceptDictionary.WHICH_TETANUS_WAS_GIVEN_AT_THIS_VISIT = setup("WHICH_TETANUS_WAS_GIVEN_AT_THIS_VISIT", props, unfoundItems);
            ConceptDictionary.LUNGS = setup("LUNGS", props, unfoundItems);
            ConceptDictionary.HEART = setup("HEART", props, unfoundItems);
            ConceptDictionary.RAPID_PLASMA_REAGENT = setup("RAPID_PLASMA_REAGENT", props, unfoundItems);
            ConceptDictionary.RESULT_OF_HIV_TEST = setup("RESULT_OF_HIV_TEST", props, unfoundItems);
            ConceptDictionary.BIRTH_WEIGHT= setup("BIRTH_WEIGHT", props, unfoundItems);
            ConceptDictionary.DATE_OF_BIRTH_BABY_ONE= setup("DATE_OF_BIRTH_BABY_ONE", props, unfoundItems);
            ConceptDictionary.BIRTH_HEIGHT = setup("BIRTH_HEIGHT",props,unfoundItems);
            ConceptDictionary.MIDDLE_UPPER_ARM_CIRCUMFERENCE=setup("MIDDLE_UPPER_ARM_CIRCUMFERENCE",props,unfoundItems);
            ConceptDictionary.MIDDLE_UPPER_ARM_CIRCUMFERENCE_LESS_THAN_TWENTY=setup("MIDDLE_UPPER_ARM_CIRCUMFERENCE_LESS_THAN_TWENTY",props,unfoundItems);
            ConceptDictionary.HISTORY_OF_DELIVERY=setup("HISTORY_OF_DELIVERY",props,unfoundItems);
            ConceptDictionary.VAGINAL_BLEEDING = setup("VAGINAL_BLEEDING",props,unfoundItems);
            ConceptDictionary.CONVULSIONS = setup("CONVULSIONS",props,unfoundItems);
            ConceptDictionary.ECLAMPSIA_INVESTIGATION = setup("ECLAMPSIA_INVESTIGATION",props,unfoundItems);
            ConceptDictionary.DANGER_SIGNS_FROM_HISTORY = setup("DANGER_SIGNS_FROM_HISTORY",props,unfoundItems);
            ConceptDictionary.APGAR = setup("APGAR",props,unfoundItems);
            ConceptDictionary.DATE_STARTED_ON_KMC = setup("DATE_STARTED_ON_KMC",props,unfoundItems);
            ConceptDictionary.WEIGHT_STARTING_ON_KMC = setup("WEIGHT_STARTING_ON_KMC",props,unfoundItems);
            ConceptDictionary.SERIOUS_PATOLOGIES = setup("SERIOUS_PATOLOGIES",props,unfoundItems);
            ConceptDictionary.BLOOD_GROUP = setup("BLOOD_GROUP",props,unfoundItems);
            ConceptDictionary.HIV_RESULT = setup("HIV_RESULT",props,unfoundItems);
            ConceptDictionary.PARTNER_RESULT_OF_HIV_TEST = setup("PARTNER_RESULT_OF_HIV_TEST",props,unfoundItems);







            for (String str: unfoundItems)
            	log.error("mch module could not load the folowing item in global property mch.constants: " + str);
            
        }
        return unfoundItems;
    }
    
    private static Integer setup(String key, Properties props, List<String> unfoundItems){
    	try {
    		String input = props.getProperty(key).trim();
	        if (input != null && !"".equals(input)){
	            Concept c = Context.getConceptService().getConceptByUuid(input);
	        	if (c == null){
	        		try {
	        			c = Context.getConceptService().getConcept(Integer.valueOf(input));
	        		} catch (Exception ex){
	        			//pass, string was not numeric
	        		}
	        	}
	            if (c != null)
	            	return c.getConceptId();
	            else
	            	unfoundItems.add(key);
	        } else
	        	unfoundItems.add(key);
	           
    	} catch (Exception ex){
    		unfoundItems.add(key);
    		//log.warn("RwandaFlowsheetModule unable to load metadata for key " + key + ". Please check your mappings in the global property mch.constants.");
    	}
    	return -100;
    }
    
    private static Integer setupPatientIdentifierType(String key, Properties props){
    	if (props.getProperty(key) == null){
    		throw new RuntimeException("Unable to load value for key " + key + ".  Please check the mappings in the global property mch.constants.");
    	}
    		
        String input = props.getProperty(key).trim();
        if (input != null && !"".equals(input)){
        	PatientIdentifierType c = Context.getPatientService().getPatientIdentifierTypeByUuid(input);
        	if (c == null){
        		try {
        			c = Context.getPatientService().getPatientIdentifierType(Integer.valueOf(input));
        		} catch (Exception ex){
        			//pass, string was not numeric
        		}
        	}
            if (c != null)
            	return c.getPatientIdentifierTypeId();
            else
            	throw new RuntimeException("RwandaFlowsheetModule unable to load metadata for key " + key + ". Please check your mappings in the global property mch.constants.");
        } else
            throw new RuntimeException("RwandaFlowsheetModule unable to load metadata for key " + key + ". Please check your mappings in the global property mch.constants.");
    }
    
    private static Integer setupProgram(String key, Properties props, List<String> unfoundItems){
    	if (props.getProperty(key) != null){
	        String input = props.getProperty(key).trim();
	        if (input != null && !"".equals(input)){
	            Program c = Context.getProgramWorkflowService().getProgramByUuid(input);
	            if (c == null)
	            	c = Context.getProgramWorkflowService().getProgramByName(input);
	        	if (c == null){
	        		try {
	        			c = Context.getProgramWorkflowService().getProgram(Integer.valueOf(input));
	        		} catch (Exception ex){
	        			//pass, string was not numeric
	        		}
	        	}
	            if (c != null)
	            	return c.getProgramId();
	            else
	            	unfoundItems.add(key);
	        } else
	        	unfoundItems.add(key);
    	} else
    		unfoundItems.add(key);
        return -100;
    }
    
    private static Integer setupEncounterTypeId(String key, Properties props){
    	if (props.getProperty(key) == null){
    		throw new RuntimeException("RwandaFlowsheetModule unable to load metadata for key " + key + ". Please check your mappings in the global property mch.constants.");
    	}
        String input = props.getProperty(key).trim();
        if (input != null && !"".equals(input)){
        	EncounterType c = Context.getEncounterService().getEncounterTypeByUuid(input);
        	if (c == null){
        		try {
        			c = Context.getEncounterService().getEncounterType(Integer.valueOf(input));
        		} catch (Exception ex){
        			//pass, string was not numeric
        		}
        	}
            if (c != null)
            	return c.getEncounterTypeId();
            else
            	throw new RuntimeException("RwandaFlowsheetModule unable to load metadata for key " + key + ". Please check your mappings in the global property mch.constants.");
        } else
            throw new RuntimeException("RwandaFlowsheetModule unable to load metadata for key " + key + ". Please check your mappings in the global property mch.constants.");
    }
    
    private static Integer setupForm(String key){
        String input = Context.getAdministrationService().getGlobalProperty(key);
        if (input != null && !"".equals(input)){
            Form c = Context.getFormService().getFormByUuid(input);
        	if (c == null){
        		try {
        			c = Context.getFormService().getForm(Integer.valueOf(input));
        		} catch (Exception ex){
        			//pass, string was not numeric
        		}
        	}
            if (c != null)
            	return c.getFormId();
            else
            	throw new RuntimeException("RwandaFlowsheetModule unable to load metadata for key " + key + ". Please check your mappings in the global property mch.constants.");
        } else
            throw new RuntimeException("RwandaFlowsheetModule unable to load metadata for key " + key + ". Please check your mappings in the global property mch.constants.");
    }

    
}
