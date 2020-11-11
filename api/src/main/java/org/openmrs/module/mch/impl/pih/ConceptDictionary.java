package org.openmrs.module.mch.impl.pih;

import org.openmrs.module.mch.MetadataHelper;

/**
 * Class mirrors the concept ids in the PIH Concept dictionary. NOTE: We would
 * want an abstraction layer, however, this is being done specifically for
 * Rwanda PIH.
 */
public class ConceptDictionary {

	public static int ANC_FORM;
	/*public static int ASRH;
	public static int ECD;
	public static int EPI;
	public static int FP;
	public static int GBV;
	public static int GMP;
	public static int IMNCI;
	public static int INTRAPARTUM;
	public static int KMC;
	public static int NEWBORNTRANSFER;*/
	public static int PNC_FORM;
	public static int MCH_PROGRAM;


	public static int LMP;
	public static int GRAVIDITY;
	public static int EXPECTED_DUE_DATE;
	public static int DURATION_OF_PREGNANCY;
	public static int PATIENT_ARRIVED_WITH_PARTNER;
	public static int PARITY;
	public static int NUMBER_OF_ABORTIONS;
	public static int NUMBER_OF_CESAREAN_SECTIONS;
	public static int OBSTETRIC_HISTORY;
	public static int CURRENT_PROBLEMS;
	public static int DANGER_SIGNS;
	public static int MEDICAL_HISTORY;
	public static int TEMPERATURE;
	public static int DIASTOLIC_BLOOD_PRESSURE;
	public static int SYSTOLIC_BLOOD_PRESSURE;
	public static int PULSE;
    public static int GENITAL_EXAMINATION;
    public static int ABNORMAL;
    public static int IS_BABY_MOVING;
    public static int DOES_FUNDAL_HEIGHT_MATCH_GESTATIONAL_AGE;
    public static int FETAL_POSITION;
    public static int FETAL_HEART;
    public static int RESPIRATION_MOVEMENTS;
    public static int FETAL_TONE;
    public static int WHICH_TETANUS_WAS_GIVEN_AT_THIS_VISIT;
    public static int LUNGS;
    public static int HEART;
    public static int RAPID_PLASMA_REAGENT;
    public static int RESULT_OF_HIV_TEST;
	public static int BIRTH_WEIGHT;
	public static int DATE_OF_BIRTH_BABY_ONE;
	public static int BIRTH_HEIGHT;
	public static int  MIDDLE_UPPER_ARM_CIRCUMFERENCE;
	public static  int MIDDLE_UPPER_ARM_CIRCUMFERENCE_LESS_THAN_TWENTY;
	public static  int HISTORY_OF_DELIVERY;
	public static  int VAGINAL_BLEEDING;
	public static  int CONVULSIONS;
	public static  int ECLAMPSIA_INVESTIGATION;
	public static  int DANGER_SIGNS_FROM_HISTORY;
	public static  int APGAR;
	public static  int DATE_STARTED_ON_KMC;
	public static  int WEIGHT_STARTING_ON_KMC;
	public static  int  SERIOUS_PATOLOGIES;
	public static  int  BLOOD_GROUP;
	public static  int HIV_RESULT;
	public static  int PARTNER_RESULT_OF_HIV_TEST;





	public static int TESTS_ORDERED_NON_CODED;
	public static int OIS_THAT_ARE_STIS;
	public static int  NORMAL;
	public static int  ASSISTANCE_REQUIRED ;
	public static int  BEDRIDDEN ;
	public static int  ABSTINENCE ;
	public static int  CONDOMS ;
	public static int  NATURAL_FAMILY_PLANNING ;
	
	public static int ABSOLUTE_LYMPHOCYTE_COUNT;

	public static int ADVERSE_EFFECT;

	public static int ADVERSE_EFFECT_ACTION_TAKEN_NONCODED;
	public static int ADVERSE_EFFECT_CONSTRUCT;
	public static int ADVERSE_EFFECT_DATE;
	public static int ADVERSE_EFFECT_MEDICATION;
	public static int ADVERSE_EFFECT_MEDICATION_NONCODED;
	public static int ADVERSE_EFFECT_NONCODED;
	public static int ADVERSE_EVENT_ACTION_TAKEN;

	/** Anaphylaxie */
	public static int ANAPHYLAXIS;
	/** Anemie */
	public static int ANEMIA;
	public static int ASSESSMENT_COMMENTS;
	public static int ASTHMA;
	public static int CD4_COUNT;
	/** Changement de dose */
	public static int CHANGE_OF_DOSAGE;
	public static int CHEST_XRAY;
	public static int CHEST_XRAY_CONSTRUCT;
	public static int CHRONIC_CARE_CONSTRUCT;
	public static int CHRONIC_CARE_DIAGNOSIS;
	public static int CHRONIC_CARE_DIAGNOSIS_OTHER;
	public static int CLINICAL_IMPRESSION_COMMENTS;
	public static int COMPUTED_TOMOGRAPHY_SCAN_HEAD;
	public static int CURRENT_OPPORTUNISTIC_INFECTION;
	public static int Current_opportunistic_infection_construct;
	public static int CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED;
	public static int CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED_NON_CODED;
	
	public static int CURRENT_OI;
	//these should only get shown once total
	public static int CURRENT_OI_DEPRESSION;
	public static int CURRENT_OI_CARDIOVASCULAR_DISEASE;
	public static int CURRENT_OI_CEREBRAL_LYMPHOMA;
	public static int CURRENT_OI_KAPOSIS_SARCOMA;
	public static int CURRENT_OI_NEUROLOGICAL_DEFICIT;
	public static int CURRENT_OI_TUBERCULOSIS;
	public static int CURRENT_OI_PULMONARY_TUBERCULOSIS;
	public static int CURRENT_OI_EXTRAPULMONARY_TUBERCULOSIS;
	public static int CURRENT_OI_WEIGHT_LOSS_GREATER_THAN_10_PERCENT;

	public static int DATE_OF_GENERAL_TEST;

	public static int DATE_OF_HOSPITAL_ADMITTANCE;
	public static int DATE_OF_LABORATORY_TEST;
	public static int DAYS;
	public static int DIABETES;

	public static int DIABETES_MELLITUS;
	public static int DIAGNOSIS_WHILE_HOSPITALIZED;
	public static int DIAGNOSIS_WHILE_HOSPITALIZED_NON_CODED;
	public static int DRUG_3TC;
	public static int DRUG_ABC;
	public static int DRUG_AZT;
	public static int DRUG_AZT_3TC;
	public static int DRUG_AZT_3TC_AND_ABC;
	public static int DRUG_AZT_3TC_EFV;
	public static int DRUG_AZT_3TC_LORR;
	public static int DRUG_AZT_3TC_NVP;

	public static int DRUG_D4T = 1;
	// public static int DRUG_D4T30_3TC_EFV = 1505;
	// public static int DRUG_D4T40_3TC_EFV = 1506;
	// public static int DRUG_D4T30_3TC_NVP = 1502;
	// public static int DRUG_D4T40_3TC_NVP = 1503;
	// public static int DRUG_D4T_3TC_EFV = 1613;
	// public static int DRUG_D4T_3TC_NVP = 792;
	// public static int DRUG_d4T_3TC = 2833;
	// public static int DRUG_AZT_3TC_AND_ABC = 2204;
	// public static int DRUG_AZT_3TC = 630;
	public static int DRUG_D4T_3TC;

	// public static int DRUG_AZT_3TC_EFV = 1612;
	public static int DRUG_D4T_3TC_EFV;

	public static int DRUG_D4T_3TC_NVP;
	public static int DRUG_D4T30_3TC_EFV;

	public static int DRUG_D4T30_3TC_NVP;
	public static int DRUG_D4T40_3TC_EFV;

	public static int DRUG_D4T40_3TC_NVP;
	public static int DRUG_DDI;
	// public static int DRUG_DDI_3TC_LOPR = 2989;
	// public static int DRUG_DDI_ABC_LOPR = 2988;
	// public static int DRUG_DDI_AZT_LOPR = 2990;
	// public static int DRUG_DDI_TDF_LOPR = 2987;
	public static int DRUG_DDI_3TC_LOPR;

	public static int DRUG_DDI_ABC_LOPR;
	// public static int DRUG_TDF_ABC_LOPR = 2991;
	// public static int DRUG_AZT_3TC_AND_ABC = 2204;
	public static int DRUG_DDI_AZT_LOPR;

	public static int DRUG_DDI_TDF_LOPR;

	// public static int DRUG_TDF_3TC_LOPR = 2992;
	// public static int DRUG_TDF_AZT_LOPR = 2993;
	public static int DRUG_DIDANOSINE_ABACAVIR_LOPINAVIR_RITONAVIR;
	// public static int DRUG_DDI_TDF_LOPR = 2987;
	// public static int DRUG_DDI_3TC_LOPR = 2989;
	// public static int DRUG_DDI_AZT_LOPR = 2990;
	// public static int DRUG_TDF_ABC_LOPR = 2991;
	// public static int DRUG_TDF_3TC_LOPR = 2992;
	// public static int DRUG_TDF_AZT_LOPR = 2993;
	// public static int DRUG_AZT_3TC_LORR = 2994;
	public static int DRUG_EFV;
	// public static int DRUG_D4T30_3TC_NVP = 1502;
	// public static int DRUG_D4T40_3TC_NVP = 1503;
	public static int DRUG_INFANT_NVP;
	public static int DRUG_LOPINAVIR_AND_RITONAVIR;
	public static int DRUG_NVP;

	// public static int DRUG_AZT_3TC_NVP = 1610;
	// public static int DRUG_TDF_3TC_NVP = 2984;
	public static int DRUG_NVP_UNIQUE_DOSE;

	public static int DRUG_TDF;

	public static int DRUG_TDF_3TC_EFV;

	public static int DRUG_TDF_3TC_LOPR;
	// public static int DRUG_AZT_3TC_LORR = 2994;
	// public static int DRUG_AZT_3TC_NVP = 1610;
	// public static int DRUG_AZT_3TC_EFV = 1612;
	// public static int DRUG_TDF_3TC_EFV = 2985;
	// public static int DRUG_D4T_3TC_EFV = 1613;
	public static int DRUG_TDF_3TC_NVP;
	// public static int DRUG_TDF_3TC_EFV = 2985;
	// public static int DRUG_TDF_3TC_NVP = 2984;
	// public static int DRUG_TDF_FTC_NVP = 2986;
	public static int DRUG_TDF_ABC_LOPR;
	public static int DRUG_TDF_AZT_LOPR;
	public static int DRUG_TDF_FTC;
	public static int DRUG_TDF_FTC_NVP;
	// public static int DRUG_D4T_3TC_NVP = 792;
	// public static int DRUG_D4T30_3TC_EFV = 1505;
	// public static int DRUG_D4T40_3TC_EFV = 1506;
	public static int DRUG_TNF_3TC;

	public static int DRUG_UNAVAILABLE;

	public static int  DRUG_COTRIMOXAZOLE;
	public static int  DRUG_FLUCONAZOLE;
	public static int DRUG_DAPSONE;
	
	/** Arret du medicament */
	public static int DRUG_WITHDRAWN;
	public static int Duration_of_hospitalization;
	public static int DURATION_OF_HOSPITALIZATION_CONSTRUCT ;
	public static int EPILEPSY ;

	public static int FREE_TEXT_REASON_FOR_POOR_ADHERENCE_TO_ANTIRETROVIRAL_THERAPY;

	public static int FUNCTIONAL_ABILITY_OF_THE_PATIENT;
	public static int GASTROENTERITIS ;
	public static int GRANULOCYTE ;

	public static int HEART_FAILURE ;

	public static int HEIGHT_CM ;
	public static int HEMATOCRIT ;
	public static int HEMOGLOBIN ;
	/** Hepatite */
	public static int HEPATITIS ;

	public static int HIV_VIRAL_LOAD;
	public static int HOSPITALIZATION_COMMENT;
	public static int HOSPITALIZATION_CONSTRUCT;
	public static int HOSPITALIZATION_DISCHARGE_DATE;
	public static int INTERACTION_WITH_TUBERCULOSIS_DRUG;
	/** Ictere */
	public static int JAUNDICE;
	public static int LABORATORY_EXAMINATIONS_CONSTRUCT;

	/** Acidose Lactique */
	public static int LACTIC_ACIDOSIS ;
	/** Lipodystrophie */
	public static int LIPODYSTROPHY ;
	public static int MALARIA ;
	public static int MALNUTRITION ;
	public static int MEDICAL_IMAGE_CONSTRUCT ;

	public static int MENINGITIS;
	public static int METHOD_OF_FAMILY_PLANNING;
	public static int MONTHS;
	/** Nausees */
	public static int NAUSEA;
	public static int NEGATIVE;
	/** Cauchemars */
	public static int NIGHTMARES;
	public static int NO ;
	public static int NON_REACTIVE;

	public static int NEXT_VISIT;
	
	public static int NONE;
	public static int NOT_DONE;
	public static int NUMBER_OF_DOSES_OF_ANTIRETROVIRALS_MISSED_IN_THE_PAST_MONTH;
	public static int OTHER_LAB_TEST_CONSTRUCT;

	public static int OTHER_LAB_TEST_NAME;
	public static int OTHER_LAB_TEST_RESULT;

	public static int OTHER_NON_CODED;
	public static int OUTPATIENT_DIAGNOSIS;

	public static int PATIENT_DEFAULTED;

	public static int PATIENT_DIED;

	public static int PATIENT_HAD_SIDE_EFFECTS;

	public static int PATIENT_HAS_DIABETES;

	public static int PATIENT_PREGNANT;

	public static int PATIENT_VISIT_CONSTRUCT;

	/** Neuropathie */
	public static int PERIPHERAL_NEUROPATHY;

	public static int PLATELETS;

	public static int PNEUMONIA;

	public static int POSITIVE;

	public static int PREGNANCY_STATUS;

	public static int PREVIOUS_DIAGNOSIS;

	public static int PREVIOUS_DIAGNOSIS_CONSTRUCT;
	
	public static int PREVIOUS_DIAGNOSIS_DATE;

	public static int PULMONARY_EFFUSION;

	public static int RAPID_PLASMIN_REAGENT;

	/** Eruption cutanee legere */
	public static int RASH_MINOR;

	/** Eruption cutanee moderee */
	public static int RASH_MODERATE;

	/** Eruption cutanee severe (desquamant) */
	public static int RASH_SEVERE;

	public static int REACTIVE;

	public static int REGIMEN_FAILURE ;

	public static int RENAL_FAILURE;

	public static int RESULT_OF_TUBERCULOSIS_SCREENING_QUALITATIVE;
	
	public static int RESULT_OF_TB_SMEAR;
	
	public static int RESULT_OF_TB_SMEAR_POSITIVE;
	public static int RESULT_OF_TB_SMEAR_WEAKLY_POSITIVE;
	public static int RESULT_OF_TB_SMEAR_MODERATELY_POSITIVE;
	public static int RESULT_OF_TB_SMEAR_STRONGLY_POSITIVE;
	public static int RESULT_OF_TB_SMEAR_NEGATIVE;

	public static int SERUM_CREATININE ;

	public static int CREATININE_CLEARANCE;

	public static int SERUM_GLUCOSE;

	public static int SERUM_GLUTAMIC_OXALOACETIC_TRANSAMINASE ;

	public static int SERUM_GLUTAMIC_PYRUVIC_TRANSAMINASE;

	public static int SEVERITY_OF_CARDIOMEGALY ;

	public static int SEXUALLY_TRANSMITTED_INFECTION_SYMPTOMS_COMMENT;

	/** Traitement symptomatique */
	public static int SYMPTOMATIC;
	
	public static int TB_DRUG_ISONIAZID_PROPHYLAXIS;
	public static int TB_DRUG_RHEZ;
	public static int TB_DRUG_RH;
	public static int TB_DRUG_RHZ;
	public static int TB_DRUG_RHE;
	public static int TB_DRUG_AMOX_CLAV;
	
	public static int TB_TREATMENT_TYPE;
	public static int TB_TREATMENT_TYPE_INITIAL;
	public static int TB_TREATMENT_TYPE_RETREATMENT;
	public static int TB_TREATMENT_TYPE_MDRTB;
	public static int TB_TREATMENT_OUTCOME_GUERI_CURED;
	public static int TB_TREATMENT_OUTCOME_ECHEC_FAILED;
	public static int TB_TREATMENT_OUTCOME_COMPLETE;
	public static int TB_TREATMENT_OUTCOME_ABAONDONED;
	public static int TB_TREATMENT_OUTCOME_TRANSFERED;
	public static int TB_TREATMENT_OUTCOME_DIED;

	public static int TESTS_ORDERED;

	public static int TIME_UNITS;
	
	public static int TREATMENT_GROUP;
	
	public static int INFORMED_STATUS;
	
	public static int PATIENT_INFORMED;
	
	public static int PATIENT_NOT_INFORMED;
	
	
	/** Vomissements */
	public static int VOMITING;

	public static int WEEKS;

	public static int WEIGHT_KG;

	public static int WHITE_BLOOD_CELLS;

	public static int WHO_STAGE;

	public static int XRAY_CHEST;

	public static int YES;
	
	public static int PatientIdentiferId_Tracnet;
	
	public static Integer ProgramId_AdultHIVProgram;
	public static Integer ProgramId_PediHIVProgram;
	public static Integer ProgramId_PMTCT_mother;
	public static Integer ProgramId_Exposed_Infant_mother;
	
	public static Integer ADULT_FLOWSHEET_ENCOUNTER_ID;
	public static Integer PEDI_FLOWSHEET_ENCOUNTER_ID;
	
	public static String ANTIRETROVIRAL_DRUGS = "ANTIRETROVIRAL DRUGS";
	public static String TUBERCULOSIS_TREATMENT_DRUGS = "TUBERCULOSIS TREATMENT DRUGS";
	
	
	public static int PROPHYLAXIS_REASON_FOR_STOPPING_TOXICITY;
	public static int PROPHYLAXIS_REASON_FOR_STOPPING_ABANDONED;
	public static int PROPHYLAXIS_REASON_FOR_STOPPING_OUT_OF_STOCK;
	public static int PROPHYLAXIS_REASON_FOR_STOPPING_CD4_IMPROVEMENT;
	public static int PROPHYLAXIS_REASON_FOR_STOPPING_TERMINE;
	
	public static int HIV_DIAGNOSIS_DATE;

	public static int DRUG_ISONIAZID;
	
	public static int Z_SCORE_WEIGHT;
	
	public static int Z_SCORE_HEIGHT;
	
	public static int HEIGHT_WEIGHT_PERCENTILE;
	
	public static int ABDOMINAL_ULTRASOUND;
	
	public static int CD4_PERCENTAGE;
	
	public static int TREATMENT_COMPLETE;
	
	public static int OPPORTUNISTIC_INFECTION;
	
	public static int OPPORTUNISTIC_INFECTION_NON_CODED;
	
	public static int OPPORTUNISTIC_INFECTION_SET;
	
	public static int OPPORTUNISTIC_INFECTION_START_DATE;
	
	public static int OPPORTUNISTIC_INFECTION_COMMENTS;
	
	public static int STI_INFECTION;
	
	public static int STI_INFECTION_NON_CODED;
	
	public static int STI_SET;
	
	public static int ADULT_ALLERGY_FORM;
	
	public static int ADULT_HOSPITALISATION_FORM;
	
	public static int ADULT_OI_FORM;
	
	public static int ADULT_PROBLEM_FORM;
	
	public static int ADULT_VISIT_FORM;
	
	public static int ADULT_LAB_FORM;
	
	public static int ADULT_IMAGE_FORM;
	
	public static int PEDI_ALLERGY_FORM;
	
	public static int PEDI_HOSPITALISATION_FORM;
	
	public static int PEDI_OI_FORM;
	
	public static int PEDI_PROBLEM_FORM;
	
	public static int PEDI_VISIT_FORM;
	
	public static int PEDI_LAB_FORM;
	
	public static int PEDI_IMAGE_FORM;
	
	

	private ConceptDictionary() {
	    MetadataHelper.setupConstants();
	}

}
