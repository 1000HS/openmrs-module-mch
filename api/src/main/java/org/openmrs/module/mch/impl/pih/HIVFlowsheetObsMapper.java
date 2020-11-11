package org.openmrs.module.mch.impl.pih;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.heightweighttracker.mapper.BMIForAge;
import org.openmrs.module.heightweighttracker.mapper.WeightForHeight;
import org.openmrs.module.mch.mapper.Allergy;
import org.openmrs.module.mch.mapper.HeightForAge;
import org.openmrs.module.mch.mapper.Hospitalization;
import org.openmrs.module.mch.mapper.Image;
import org.openmrs.module.mch.mapper.ImagePedi;
import org.openmrs.module.mch.mapper.Lab;
import org.openmrs.module.mch.mapper.LabGroup;
import org.openmrs.module.mch.mapper.OI;
import org.openmrs.module.mch.mapper.Problem;
import org.openmrs.module.mch.mapper.Visit;
import org.openmrs.module.mch.mapper.VisitGroup;
import org.openmrs.module.mch.mapper.WeightForAge;

public class HIVFlowsheetObsMapper {
	
	private static final int MIN_IMAGE_EXTRA_ROWS = 2;

	private static final int MIN_IMAGE_ROWS = 4;

	private static final int MIN_VISIT_ROWS = 32;

	private static final int MIN_PROBLEM_EXTRA_ROWS = 1;

	private static final int MIN_PROBLEM_ROWS = 2;

	private static final int MIN_OI_EXTRA_ROWS = 3;

	private static final int MIN_OI_ROWS = 6;

	private static final int MIN_HOSPITALIZATION_EXTRA_ROWS = 7;

	private static final int MIN_HOSPITALIZATION_ROWS = 7;

	private static final int MIN_ALLERGY_EXTRA_ROWS = 1;

	private static final int MIN_ALLERGY_ROWS = 3;

	private static final int MIN_LABS_EXTRA_ROWS = 3;

	private static final int MIN_LAB_ROWS = 14;

	/** Logger instance */
	private final Log log = LogFactory.getLog(this.getClass());

	/** List of research encounter types.  This is used as an optional filter.  See research encounters module. */
	private static Set<Integer> researchEncounterTypes;

	/** Cache of the obs by concept id. */
	private Map<Integer, List<Obs>> conceptIdToObsMap = new HashMap<Integer, List<Obs>>();

	/** Patient. */
	private Patient patient;


	public HIVFlowsheetObsMapper(Patient patient) {
		this.patient = patient;
	}
	
	public Collection<Lab> getCd4ObsList() {
		Integer[] labConceptIds = {	ConceptDictionary.CD4_COUNT };
		
		Date birthDate = patient.getBirthdate();

		Collection<LabMapping> collection = getObsView(labConceptIds, LabMapping.class);

		//need to filter out cd4 count before the age of 5
		List<LabMapping> filteredList = new ArrayList<LabMapping>();
		for(LabMapping lm: collection)
		{
			int months = calculateMonthsDifference(lm.getObsDate(), birthDate);
			if(months > 59)
			{
				filteredList.add(lm);
			}
		}
		
    	return new ArrayList<Lab>(filteredList);
/*
    	List<Map<Integer, Obs>> map = sortAndGroupObsByDate(getObsList(ConceptDictionary.CD4_COUNT));
		List<Obs> sortedList = new LinkedList<Obs>();
		for(Map<Integer, Obs> item : map) {
			for(Obs obs : item.values())
				sortedList.add(obs);
		}
		return sortedList;
*/
	}
	
	public Collection<Lab> getCd4PercentObsList() {
		Integer[] labConceptIds = {	ConceptDictionary.CD4_PERCENTAGE };
		
		Date birthDate = patient.getBirthdate();

		Collection<LabMapping> collection = getObsView(labConceptIds, LabMapping.class);

		//need to filter out cd4 count before the age of 5
		List<LabMapping> filteredList = new ArrayList<LabMapping>();
		for(LabMapping lm: collection)
		{
			int months = calculateMonthsDifference(lm.getObsDate(), birthDate);
			if(months < 61)
			{
				filteredList.add(lm);
			}
		}
		
    	return new ArrayList<Lab>(filteredList);
/*
    	List<Map<Integer, Obs>> map = sortAndGroupObsByDate(getObsList(ConceptDictionary.CD4_COUNT));
		List<Obs> sortedList = new LinkedList<Obs>();
		for(Map<Integer, Obs> item : map) {
			for(Obs obs : item.values())
				sortedList.add(obs);
		}
		return sortedList;
*/
	}

	public Collection<Lab> getViralLoadObsList() {
		Integer[] labConceptIds = {	ConceptDictionary.HIV_VIRAL_LOAD };

		Collection<LabMapping> collection = getObsView(labConceptIds, LabMapping.class);

    	return new ArrayList<Lab>(collection);

//		return getObsList(ConceptDictionary.HIV_VIRAL_LOAD);
	}

	public Collection<Obs> getHeightObsList() {
		return getObsList(ConceptDictionary.HEIGHT_CM);
	}
	
	public Collection<Obs> getWeightObsList() {
		return getObsList(ConceptDictionary.WEIGHT_KG);
	}
	
	public Obs getLatestHeight() {
		Collection<Obs> obsList = getHeightObsList();
		if(obsList == null || obsList.size() == 0)
			return new Obs();

		Obs latestHeight = null;
		for(Obs obs : obsList) {
			if(latestHeight == null || latestHeight.getObsDatetime().before(obs.getObsDatetime()))
			{
				latestHeight = obs;
			}
		}
		return latestHeight;
	}
	
	public List<HeightForAge> getHeightForAge()
	{
		List<HeightForAge> heightForAge = new ArrayList<HeightForAge>();
		Date birthDate = patient.getBirthdate();
		
		Collection<Obs> heightObs = getHeightObsList();
		if(heightObs != null)
		{
			for(Obs o: heightObs)
			{
				int months = calculateMonthsDifference(o.getObsDatetime(), birthDate);
				if(months > 59 && months < 230)
				{
					HeightForAge hfa = new HeightForAge();
					hfa.setAgeInMonths(months);
					hfa.setHeightInCM(o.getValueNumeric().intValue());
					heightForAge.add(hfa);
				}
			}
		}
		
		return heightForAge;
	}
	
	public List<WeightForAge> getWeightForAgeFromBirth()
	{
		List<WeightForAge> weightForAge = new ArrayList<WeightForAge>();
		Date birthDate = patient.getBirthdate();
		
		Collection<Obs> weightObs = getWeightObsList();
		if(weightObs != null)
		{
			for(Obs o: weightObs)
			{
				int days = calculateDaysDifference(o.getObsDatetime(), birthDate);
				if(days < 1827)
				{
					WeightForAge wfa = new WeightForAge();
					wfa.setAgeInDays(days);
					wfa.setWeightInKG(o.getValueNumeric().intValue());
					weightForAge.add(wfa);
				}
			}
		}
		
		return weightForAge;
	}
	
	public List<WeightForAge> getWeightForAgeTill20()
	{
		List<WeightForAge> weightForAge = new ArrayList<WeightForAge>();
		Date birthDate = patient.getBirthdate();
		
		Collection<Obs> weightObs = getWeightObsList();
		if(weightObs != null)
		{
			for(Obs o: weightObs)
			{
				int months = calculateMonthsDifference(o.getObsDatetime(), birthDate);
				if(months > 23 && months < 241)
				{
					WeightForAge wfa = new WeightForAge();
					wfa.setAgeInMonths(months);
					wfa.setWeightInKG(o.getValueNumeric().intValue());
					weightForAge.add(wfa);
				}
			}
		}
		
		return weightForAge;
	}
	
	public List<WeightForAge> getWeightForAge()
	{
		List<WeightForAge> weightForAge = new ArrayList<WeightForAge>();
		Date birthDate = patient.getBirthdate();
		
		Collection<Obs> weightObs = getWeightObsList();
		if(weightObs != null)
		{
			for(Obs o: weightObs)
			{
				int months = calculateMonthsDifference(o.getObsDatetime(), birthDate);
				if(months > 59 && months < 230)
				{
					WeightForAge wfa = new WeightForAge();
					wfa.setAgeInMonths(months);
					wfa.setWeightInKG(o.getValueNumeric().intValue());
					weightForAge.add(wfa);
				}
			}
		}
		
		return weightForAge;
	}
	
	
	public List<HeightForAge> getLengthForAge()
	{
		List<HeightForAge> heightForAge = new ArrayList<HeightForAge>();
		Date birthDate = patient.getBirthdate();
		
		Collection<Obs> heightObs = getHeightObsList();
		if(heightObs != null)
		{
			for(Obs o: heightObs)
			{
				int days = calculateDaysDifference(o.getObsDatetime(), birthDate);
				if(days < 1827)
				{
					HeightForAge hfa = new HeightForAge();
					hfa.setAgeInDays(days);
					hfa.setHeightInCM(o.getValueNumeric().intValue());
					heightForAge.add(hfa);
				}
			}
		}
		
		return heightForAge;
	}
	
	public List<HeightForAge> getStatureForAge()
	{
		List<HeightForAge> heightForAge = new ArrayList<HeightForAge>();
		Date birthDate = patient.getBirthdate();
		
		Collection<Obs> heightObs = getHeightObsList();
		if(heightObs != null)
		{
			for(Obs o: heightObs)
			{
				int months = calculateMonthsDifference(o.getObsDatetime(), birthDate);
				if(months > 23 && months < 241)
				{
					HeightForAge hfa = new HeightForAge();
					hfa.setAgeInMonths(months);
					hfa.setHeightInCM(o.getValueNumeric().intValue());
					heightForAge.add(hfa);
				}
			}
		}
		
		return heightForAge;
	}
	
	public List<BMIForAge> getBmiForAge()
	{
		List<BMIForAge> bmiForAge = new ArrayList<BMIForAge>();
		Date birthDate = patient.getBirthdate();
		
		for(VisitGroup vg: getVisitsPedi())
		{
			if(vg.getDate() != null)
			{
				int months = calculateMonthsDifference(vg.getDate(), birthDate);
				if(months > 60 && months < 229)
				{
					Obs height = vg.getHeight();
					Obs weight = vg.getWeight();
					
					if(height != null && height.getValueNumeric() != null && weight != null && weight.getValueNumeric() != null)
					{
						Double bmi = weight.getValueNumeric() / (height.getValueNumeric()/100 * height.getValueNumeric()/100);
						BMIForAge bmiVal = new BMIForAge();
						
						bmiVal.setAgeInMonths(months);
						bmiVal.setBmi(bmi);
						bmiForAge.add(bmiVal);
					}
				}
			}
		}
		
		return bmiForAge;
	}
	
	public List<WeightForHeight> getWeightForLength()
	{
		List<WeightForHeight> weightForLength = new ArrayList<WeightForHeight>();
		Date birthDate = patient.getBirthdate();
		
		for(VisitGroup vg: getVisitsPedi())
		{
			if(vg.getDate() != null)
			{
				int months = calculateMonthsDifference(vg.getDate(), birthDate);
				if(months < 25)
				{
					Obs height = vg.getHeight();
					Obs weight = vg.getWeight();
					
					if(height != null && height.getValueNumeric() != null && weight != null && weight.getValueNumeric() != null)
					{
						WeightForHeight wfl = new WeightForHeight();
						wfl.setHeight(height.getValueNumeric());
						wfl.setWeight(weight.getValueNumeric());
						weightForLength.add(wfl);
					}
				}
			}
		}
		
		return weightForLength;
	}
	
	public List<WeightForHeight> getWeightForHeight()
	{
		List<WeightForHeight> weightForHeight = new ArrayList<WeightForHeight>();
		Date birthDate = patient.getBirthdate();
		
		for(VisitGroup vg: getVisitsPedi())
		{
			if(vg.getDate() != null)
			{
				int months = calculateMonthsDifference(vg.getDate(), birthDate);
				if(months > 24 && months < 60)
				{
					Obs height = vg.getHeight();
					Obs weight = vg.getWeight();
					
					if(height != null && height.getValueNumeric() != null && weight != null && weight.getValueNumeric() != null)
					{
						WeightForHeight wfh = new WeightForHeight();
						wfh.setHeight(height.getValueNumeric());
						wfh.setWeight(weight.getValueNumeric());
						weightForHeight.add(wfh);
					}
				}
			}
		}
		
		return weightForHeight;
	}
	
	private int calculateMonthsDifference(Date observation, Date startingDate)
	{
		int diff = 0;
	
		Calendar obsDate = Calendar.getInstance();	
		obsDate.setTime(observation);
	
		Calendar startDate = Calendar.getInstance();
		startDate.setTime(startingDate);
	
		//find out if there is any difference in years first
		diff = obsDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
		diff = diff * 12;
	
		int monthDiff = obsDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
		diff = diff + monthDiff;
	
		return diff;
	}
	
	private int calculateDaysDifference(Date observation, Date startingDate)
	{
		long milis1 = observation.getTime();
		long milis2 = startingDate.getTime();
		
		long diff = milis1 - milis2;
		
		long diffDays = diff / (24 * 60 * 60 * 1000);
	
		return (int)diffDays;
	}

	public List<LabGroup> getLabsTableAdult() {
		Integer[] labConceptIds = {	ConceptDictionary.LABORATORY_EXAMINATIONS_CONSTRUCT, ConceptDictionary.OTHER_LAB_TEST_CONSTRUCT, ConceptDictionary.HEMOGLOBIN, ConceptDictionary.HEMATOCRIT, ConceptDictionary.WHITE_BLOOD_CELLS, 
				ConceptDictionary.GRANULOCYTE, ConceptDictionary.ABSOLUTE_LYMPHOCYTE_COUNT, ConceptDictionary.PLATELETS, ConceptDictionary.SERUM_GLUTAMIC_OXALOACETIC_TRANSAMINASE,
				ConceptDictionary.SERUM_GLUTAMIC_PYRUVIC_TRANSAMINASE, ConceptDictionary.SERUM_CREATININE, ConceptDictionary.SERUM_GLUCOSE, ConceptDictionary.RAPID_PLASMIN_REAGENT,
				ConceptDictionary.CD4_COUNT, ConceptDictionary.HIV_VIRAL_LOAD};

		Collection<LabMapping> collection = getObsView(labConceptIds, LabMapping.class);

    	List<List<LabMapping>> labsSorted = sortAndGroupObsByDate(collection);

    	labsSorted.add(new ArrayList<LabMapping>());
    	labsSorted.add(new ArrayList<LabMapping>());
    	labsSorted.add(new ArrayList<LabMapping>());
    	labsSorted.add(new ArrayList<LabMapping>());
    	labsSorted.add(new ArrayList<LabMapping>());
    	labsSorted.add(new ArrayList<LabMapping>());
    	labsSorted.add(new ArrayList<LabMapping>());
    	labsSorted.add(new ArrayList<LabMapping>());
    	labsSorted.add(new ArrayList<LabMapping>());
    	labsSorted.add(new ArrayList<LabMapping>());
    	labsSorted.add(new ArrayList<LabMapping>());
    	labsSorted.add(new ArrayList<LabMapping>());
    	labsSorted.add(new ArrayList<LabMapping>());
    	labsSorted.add(new ArrayList<LabMapping>());
    	
    	List<LabGroup> labRows = new ArrayList<LabGroup>(labsSorted.size());
    	for(List<LabMapping> row : labsSorted) {
    		labRows.add(new LabGroup(new ArrayList<Lab>(row)));
    	}

    	return labRows;
	}
	
	public List<LabGroup> getLabsTablePedi() {
		Integer[] labConceptIds = {	ConceptDictionary.LABORATORY_EXAMINATIONS_CONSTRUCT, ConceptDictionary.OTHER_LAB_TEST_CONSTRUCT, ConceptDictionary.HEMOGLOBIN, ConceptDictionary.HEMATOCRIT, ConceptDictionary.WHITE_BLOOD_CELLS, 
				ConceptDictionary.GRANULOCYTE, ConceptDictionary.ABSOLUTE_LYMPHOCYTE_COUNT, ConceptDictionary.PLATELETS, ConceptDictionary.SERUM_GLUTAMIC_OXALOACETIC_TRANSAMINASE,
				ConceptDictionary.SERUM_GLUTAMIC_PYRUVIC_TRANSAMINASE, ConceptDictionary.SERUM_CREATININE,ConceptDictionary.CREATININE_CLEARANCE, ConceptDictionary.SERUM_GLUCOSE, ConceptDictionary.RAPID_PLASMIN_REAGENT,
				ConceptDictionary.CD4_COUNT, ConceptDictionary.HIV_VIRAL_LOAD, ConceptDictionary.CD4_PERCENTAGE};

		Collection<LabMapping> collection = getObsView(labConceptIds, LabMapping.class);

    	List<List<LabMapping>> labsSorted = sortAndGroupObsByDate(collection);

    	int existingAllergyCount = collection.size();
        for(int index = existingAllergyCount; index < Math.max(MIN_LAB_ROWS, existingAllergyCount + MIN_LABS_EXTRA_ROWS); index++)
        	labsSorted.add(new ArrayList<LabMapping>());
    	
    	List<LabGroup> labRows = new ArrayList<LabGroup>(labsSorted.size());
    	for(List<LabMapping> row : labsSorted) {
    		labRows.add(new LabGroup(new ArrayList<Lab>(row)));
    	}

    	return labRows;
	}

    public Collection<Allergy> getAllergies() {
    	Integer[] conceptIds = { ConceptDictionary.ADVERSE_EFFECT_CONSTRUCT, ConceptDictionary.ADVERSE_EFFECT_NONCODED, ConceptDictionary.ADVERSE_EFFECT };
		
		Collection<AllergyMapping> allergiesList = getObsView(conceptIds, AllergyMapping.class);
		
		//adding 3 blanks.
    	allergiesList.add(new AllergyMapping(new Obs()));
    	allergiesList.add(new AllergyMapping(new Obs()));
    	allergiesList.add(new AllergyMapping(new Obs()));
    	
    	return new ArrayList<Allergy>(allergiesList);
    }

    public Collection<Hospitalization> getHospitalizations() {
    	Integer[] conceptIds = { ConceptDictionary.HOSPITALIZATION_CONSTRUCT };

		Collection<HospitalizationMapping> hospitalizationsLists = getObsView(conceptIds, HospitalizationMapping.class);
    	
		hospitalizationsLists.add(new HospitalizationMapping(new Obs()));
		hospitalizationsLists.add(new HospitalizationMapping(new Obs()));
		hospitalizationsLists.add(new HospitalizationMapping(new Obs()));
		hospitalizationsLists.add(new HospitalizationMapping(new Obs()));
		hospitalizationsLists.add(new HospitalizationMapping(new Obs()));
		hospitalizationsLists.add(new HospitalizationMapping(new Obs()));
    	
    	return new ArrayList<Hospitalization>(hospitalizationsLists);
    }
    
    public Collection<OI> getOis() {
    	// Because Obs can be contained in sets make sure to order this ordered so sets are
		// loaded first.  The individual obs will be filtered out.
    	Integer[] conceptIds = { ConceptDictionary.Current_opportunistic_infection_construct, 
    			//ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION, -- commented this out because its a boolean concept, and 'false', or 'true' isn't really clinically useful. 
    			ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED, 
    			ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED_NON_CODED,
    			ConceptDictionary.CURRENT_OI, 
    			ConceptDictionary.OPPORTUNISTIC_INFECTION_NON_CODED,
    			ConceptDictionary.OPPORTUNISTIC_INFECTION_SET};

		Collection<OIMapping> oisLists = getObsView(conceptIds, OIMapping.class);

		oisLists.add(new OIMapping(new Obs()));
		oisLists.add(new OIMapping(new Obs()));
		oisLists.add(new OIMapping(new Obs()));
		oisLists.add(new OIMapping(new Obs()));
		oisLists.add(new OIMapping(new Obs()));
		oisLists.add(new OIMapping(new Obs()));
		oisLists.add(new OIMapping(new Obs()));
    	
    	return new ArrayList<OI>(oisLists);
    }

    public Collection<Problem> getProblems() {
    	// Because Obs can be contained in sets make sure to order this ordered so sets are
		// loaded first.  The individual obs will be filtered out.
    	Integer[] conceptIds = { ConceptDictionary.CHRONIC_CARE_CONSTRUCT, ConceptDictionary.CHRONIC_CARE_DIAGNOSIS, ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED, ConceptDictionary.OUTPATIENT_DIAGNOSIS, 
				ConceptDictionary.PATIENT_HAS_DIABETES, ConceptDictionary.PREVIOUS_DIAGNOSIS };

		Collection<ProblemMapping> list = getObsView(conceptIds, ProblemMapping.class);
    	
    	// Filter out duplicates - note this is already sorted and we only want to keep the first.
    	List<ProblemMapping> uniqueProblemList = new ArrayList<ProblemMapping>();
    	TreeSet<Integer> conceptMap = new TreeSet<Integer>();
    	TreeSet<String> nonCodedMap = new TreeSet<String>();
    	for(ProblemMapping row : list) {
    		if(row.getDiagnosis().getValueCoded() != null)
    		{
	    		int conceptId = row.getDiagnosis().getValueCoded().getConceptId();
	    		if(!conceptMap.contains(conceptId)) {
	    			uniqueProblemList.add(row);
	    			
	    			conceptMap.add(conceptId);
	    		}
    		}
    		else if(row.getDiagnosis().getValueText() != null)
    		{
    			String value = row.getDiagnosis().getValueText().toLowerCase();
	    		if(!nonCodedMap.contains(value)) {
	    			uniqueProblemList.add(row);
	    			
	    			nonCodedMap.add(value);
	    		}
    		}
    	}
    	
    	list = uniqueProblemList;
    	list.add(new ProblemMapping(new Obs()));
    	list.add(new ProblemMapping(new Obs()));
    	list.add(new ProblemMapping(new Obs()));
    	list.add(new ProblemMapping(new Obs()));
    	
    	return new ArrayList<Problem>(list);
    }

    public Collection<VisitGroup> getVisitsAdult() {
    	Integer[] conceptIds = { ConceptDictionary.Current_opportunistic_infection_construct, ConceptDictionary.NEXT_VISIT, ConceptDictionary.PATIENT_VISIT_CONSTRUCT, ConceptDictionary.WEIGHT_KG, ConceptDictionary.FUNCTIONAL_ABILITY_OF_THE_PATIENT, ConceptDictionary.OPPORTUNISTIC_INFECTION_SET, ConceptDictionary.STI_INFECTION, ConceptDictionary.STI_INFECTION_NON_CODED, ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED_NON_CODED, ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED, ConceptDictionary.SEXUALLY_TRANSMITTED_INFECTION_SYMPTOMS_COMMENT, ConceptDictionary.RESULT_OF_TUBERCULOSIS_SCREENING_QUALITATIVE, ConceptDictionary.METHOD_OF_FAMILY_PLANNING, ConceptDictionary.PREGNANCY_STATUS, ConceptDictionary.NUMBER_OF_DOSES_OF_ANTIRETROVIRALS_MISSED_IN_THE_PAST_MONTH, ConceptDictionary.FREE_TEXT_REASON_FOR_POOR_ADHERENCE_TO_ANTIRETROVIRAL_THERAPY };

		Collection<VisitMapping> set = getObsView(conceptIds, VisitMapping.class);

    	List<List<VisitMapping>> sortedObsList = sortAndGroupObsByDate(set);

    	// Add at least one blank row and make sure there are 32 listed
    	sortedObsList.add(new ArrayList<VisitMapping>());
    	
    	for (int m = 0; m<30; m++)
    		sortedObsList.add(new ArrayList<VisitMapping>());

    	List<VisitGroup> visits = new ArrayList<VisitGroup>(sortedObsList.size());
    	for(List<VisitMapping> row : sortedObsList) {
    		visits.add(new VisitGroup(new ArrayList<Visit>(row)));
    	}

    	return visits;    	
    }
    
    public Collection<VisitGroup> getVisitsPedi() {
    	Integer[] conceptIds = { ConceptDictionary.Current_opportunistic_infection_construct, ConceptDictionary.NEXT_VISIT, ConceptDictionary.PATIENT_VISIT_CONSTRUCT, ConceptDictionary.WEIGHT_KG, ConceptDictionary.HEIGHT_WEIGHT_PERCENTILE, ConceptDictionary.STI_INFECTION, ConceptDictionary.OPPORTUNISTIC_INFECTION_SET, ConceptDictionary.STI_INFECTION_NON_CODED, ConceptDictionary.FUNCTIONAL_ABILITY_OF_THE_PATIENT, ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED_NON_CODED, ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED, ConceptDictionary.SEXUALLY_TRANSMITTED_INFECTION_SYMPTOMS_COMMENT, ConceptDictionary.RESULT_OF_TUBERCULOSIS_SCREENING_QUALITATIVE, ConceptDictionary.HEIGHT_CM, ConceptDictionary.Z_SCORE_HEIGHT, ConceptDictionary.Z_SCORE_WEIGHT, ConceptDictionary.NUMBER_OF_DOSES_OF_ANTIRETROVIRALS_MISSED_IN_THE_PAST_MONTH, ConceptDictionary.FREE_TEXT_REASON_FOR_POOR_ADHERENCE_TO_ANTIRETROVIRAL_THERAPY };

		Collection<VisitMapping> set = getObsView(conceptIds, VisitMapping.class);

    	List<List<VisitMapping>> sortedObsList = sortAndGroupObsByDate(set);

    	// Add at least one blank row and make sure there are 32 listed
    	sortedObsList.add(new ArrayList<VisitMapping>());
    	
    	for (int m = 0; m<30; m++)
    		sortedObsList.add(new ArrayList<VisitMapping>());

    	List<VisitGroup> visits = new ArrayList<VisitGroup>(sortedObsList.size());
    	for(List<VisitMapping> row : sortedObsList) {
    		visits.add(new VisitGroup(new ArrayList<Visit>(row)));
    	}

    	return visits;    	
    }
    
	public Collection<Image> getImages() {
    	// Because Obs can be contained in sets make sure to order this ordered so sets are
		// loaded first.  The individual obs will be filtered out.
		Integer[] conceptIds = { ConceptDictionary.MEDICAL_IMAGE_CONSTRUCT, ConceptDictionary.CHEST_XRAY_CONSTRUCT, ConceptDictionary.TESTS_ORDERED, ConceptDictionary.XRAY_CHEST };

		Collection<ImageMapping> list = getObsView(conceptIds, ImageMapping.class);

		list.add(new ImageMapping(new Obs()));
		list.add(new ImageMapping(new Obs()));
		list.add(new ImageMapping(new Obs()));
		list.add(new ImageMapping(new Obs()));
    	
    	return new ArrayList<Image>(list);
    }
	
	public Collection<ImagePedi> getImagesPedi() {
    	// Because Obs can be contained in sets make sure to order this ordered so sets are
		// loaded first.  The individual obs will be filtered out.
		Integer[] conceptIds = { ConceptDictionary.MEDICAL_IMAGE_CONSTRUCT, ConceptDictionary.CHEST_XRAY_CONSTRUCT, ConceptDictionary.ABDOMINAL_ULTRASOUND, ConceptDictionary.TESTS_ORDERED, ConceptDictionary.XRAY_CHEST };

		Collection<ImagePediMapping> list = getObsView(conceptIds, ImagePediMapping.class);

		list.add(new ImagePediMapping(new Obs()));
		list.add(new ImagePediMapping(new Obs()));
		list.add(new ImagePediMapping(new Obs()));
		list.add(new ImagePediMapping(new Obs()));
    	
    	return new ArrayList<ImagePedi>(list);
    }

	/**
	 * Wraps the underlying representation of obs values with higher level "view".  NOTE: this sorts 
	 * the data.
	 * 
	 * @param <T> Class that knows how to read the various ways the obs are stored.
	 * @param conceptIds List of concept id's that contain the data.
	 * @param clazz Same class as T.  This is needed to construct the instances as java doesn't support T value = new T();
	 * @return Set of view objects
	 */
	protected <T extends ObsMapping & Comparable<? super T>> Collection<T> getObsView(Integer[] conceptIds, Class<T> clazz) {
		List<Obs> labObs = new LinkedList<Obs>();
		
		
		//process sets first so that set members don't generate new rows if conceptId list contains both sets and regular concepts
		for(Integer labConceptId : conceptIds) {
			Concept c = Context.getConceptService().getConcept(labConceptId);
			if (c != null && c.isSet()){
				List<Obs> oList = getObsList(labConceptId);
				labObs.addAll(oList);
			}
		}
		for(Integer labConceptId : conceptIds) {
			Concept c = Context.getConceptService().getConcept(labConceptId);
			if (c != null && !c.isSet()){
				List<Obs> oList = getObsList(labConceptId);
				labObs.addAll(oList);
			}
		}

		// Keep track of the obs so that an obs is not added twice
		Set<Integer> usedObs = new HashSet<Integer>();

    	List<T> list = new ArrayList<T>();
    	for(Obs obs : labObs) {
    		if(!usedObs.contains(obs.getId())) {
    			addAllGroupIds(usedObs, obs);
    			
    			if(obs.getObsDatetime() != null) {
    				T row;
					try {
						row = (T)clazz.getConstructor(Obs.class).newInstance(obs);
					} catch (Exception e) {
						log.error("Failed to create an instance of type "+clazz, e);
						throw new RuntimeException("Failed to create an instance of type "+clazz, e);
					}
        			if(!row.isBlank() && !list.contains(row))
        				list.add(row);
    			}
    		}
    	}
    	
    	Collections.sort(list);
    	//List<T> list = asSortedList(set);

		return list;
	}

	private List<Obs> getObsList(Integer conceptId) {

			Concept concept = Context.getConceptService().getConcept(conceptId);
			List<Obs> obsList = Context.getObsService().getObservations(Collections.singletonList((Person) this.patient), null, Collections.singletonList(concept), null, null, null, null, null, null, null, null, false);
			
			if(obsList == null)
				return new ArrayList<Obs>();
			
			//filterOutResearchEncounters(obsList);
			
			if (concept.getConceptId().equals(ConceptDictionary.CD4_COUNT))
				filterOutRepeatedCd4Counts(obsList);

			return obsList;
	}

	private static void addAllGroupIds(Set<Integer> obsSet, Obs obs) {
		if(obs == null)
			return;
		
		obsSet.add(obs.getId());
		// Add all the obs this contains
		if(obs.getGroupMembers() != null && obs.getGroupMembers().size() > 0) {
			for(Obs groupObs : obs.getGroupMembers()) {
				addAllGroupIds(obsSet, groupObs);
			}
		}
	}

	private static void filterOutResearchEncounters(List<Obs> obsList) {
		if(obsList == null || obsList.size() == 0)
			return;
		
		// Not critical to synchronize this initialization
		if(researchEncounterTypes == null) {
			// Get the types of research encounters
			String researchEncounters = Context.getAdministrationService().getGlobalProperty("researchencounters.researchEncounters");
			TreeSet<Integer> encounterTypes = new TreeSet<Integer>();
			if (researchEncounters != null) {
				String[] toks = researchEncounters.split(",");
				for (String tok : toks)
					encounterTypes.add(Integer.parseInt(tok));
			}
			researchEncounterTypes = encounterTypes;
		}
		
		// Remove obs that are part of research encounters...
		for(int index = obsList.size() - 1; index >= 0; index--) {
			Obs obs = obsList.get(index);
			if(obs.getEncounter() != null && researchEncounterTypes.contains(obs.getEncounter().getEncounterType().getId()))
				obsList.remove(index);
		}
	}
	
	private void filterOutRepeatedCd4Counts(List<Obs> obsList) {
		if(obsList == null || obsList.size() < 2)
			return;

		// Remove obs that are repeated values within 5 months.  
		// This assumes that the obs are ordered by date with the newest first
		for(int index = obsList.size() - 2; index >=0; index--) {
			Obs currentObs = obsList.get(index);
			Obs previousObs = obsList.get(index + 1);
			if(currentObs.getValueNumeric() != null && previousObs.getValueNumeric() != null && 
					currentObs.getValueNumeric().doubleValue() > 0 && 
					currentObs.getValueNumeric().doubleValue() == previousObs.getValueNumeric().doubleValue()) {
				if(currentObs.getObsDatetime() != null && previousObs.getObsDatetime() != null) {
					long difference = currentObs.getObsDatetime().getTime() - previousObs.getObsDatetime().getTime();
					long cutoff = 1000L * 60L * 60L * 24L * 30L * 5L;
					if(difference > 0 && difference < cutoff) {
						obsList.remove(index);
					}
				}
			}
		}
	}

	private <T extends ObsMapping & Comparable<? super T>> List<List<T>> sortAndGroupObsByDate(Collection<T> labObs) {
		// Group the drugs by start date and end date.  If they are the same
    	// it's one row.
    	Map<String, List<T>> dateToObsMap = new HashMap<String, List<T>>();    	
    	for(T obs : labObs) {
    		if(!obs.isVoided()) {
	    		String key = formatDate("yyyy-MM-dd", obs.getObsDate());
	    		List<T> obsMapOnDate = dateToObsMap.get(key);
	    		if(obsMapOnDate == null) {
	    			obsMapOnDate = new ArrayList<T>(); 
	    			dateToObsMap.put(key, obsMapOnDate);
	    		}
    		
    			obsMapOnDate.add(obs);
    		}
    	}

    	// Make sure to sort the list
    	String[] keys = new String[dateToObsMap.size()];
    	dateToObsMap.keySet().toArray(keys);
    	Arrays.sort(keys);
    	
    	List<List<T>> drugOrdersSorted = new ArrayList<List<T>>(dateToObsMap.size());
    	for(String key : keys) {
    		drugOrdersSorted.add(dateToObsMap.get(key));
    	}
		return drugOrdersSorted;
	}
	
	public int getAge()
	{
		return patient.getAge();
	}

/*	private List<List<LabObs>> sortAndGroupObsByDate(Collection<LabObs> labObs) {
		// Group the drugs by start date and end date.  If they are the same
    	// it's one row.
    	Map<String, List<LabObs>> dateToObsMap = new HashMap<String, List<LabObs>>();    	
    	for(LabObs obs : labObs) {
    		if(!obs.isVoided()) {
	    		String key = formatDate("yyyy-MM-dd", obs.getDate());
	    		List<LabObs> obsMapOnDate = dateToObsMap.get(key);
	    		if(obsMapOnDate == null) {
	    			obsMapOnDate = new ArrayList<LabObs>(); 
	    			dateToObsMap.put(key, obsMapOnDate);
	    		}
    		
    			obsMapOnDate.add(obs);
    		}
    	}
    	
    	// Make sure to sort the list
    	String[] keys = new String[dateToObsMap.size()];
    	dateToObsMap.keySet().toArray(keys);
    	Arrays.sort(keys);
    	
    	List<List<LabObs>> drugOrdersSorted = new ArrayList<List<LabObs>>(dateToObsMap.size());
    	for(String key : keys) {
    		drugOrdersSorted.add(dateToObsMap.get(key));
    	}
		return drugOrdersSorted;
	}
	
    private List<List<VisitObs>> sortAndGroupVisitObsByDate(Collection<VisitObs> labObs) {
		// Group the drugs by start date and end date.  If they are the same
    	// it's one row.
    	Map<String, List<VisitObs>> dateToObsMap = new HashMap<String, List<VisitObs>>();    	
    	for(VisitObs obs : labObs) {
    		if(!obs.isVoided()) {
	    		String key = formatDate("yyyy-MM-dd", obs.getDate());
	    		List<VisitObs> obsMapOnDate = dateToObsMap.get(key);
	    		if(obsMapOnDate == null) {
	    			obsMapOnDate = new ArrayList<VisitObs>(); 
	    			dateToObsMap.put(key, obsMapOnDate);
	    		}
    		
    			obsMapOnDate.add(obs);
    		}
    	}
    	
    	// Make sure to sort the list
    	String[] keys = new String[dateToObsMap.size()];
    	dateToObsMap.keySet().toArray(keys);
    	Arrays.sort(keys);
    	
    	List<List<VisitObs>> drugOrdersSorted = new ArrayList<List<VisitObs>>(dateToObsMap.size());
    	for(String key : keys) {
    		drugOrdersSorted.add(dateToObsMap.get(key));
    	}
		return drugOrdersSorted;
	}
*/

/*	private List<Map<Integer, Obs>> sortAndGroupObsByDate(List<Obs> labObs) {
		// Group the drugs by start date and end date.  If they are the same
    	// it's one row.
    	Map<String, Map<Integer, Obs>> dateToObsMap = new HashMap<String, Map<Integer, Obs>>();    	
    	for(Obs obs : labObs) {
    		String key = formatDate("yyyy-MM-dd", obs.getObsDatetime());
    		Map<Integer, Obs> obsMapOnDate = dateToObsMap.get(key);
    		if(obsMapOnDate == null) {
    			obsMapOnDate = new LinkedHashMap<Integer, Obs>(); 
    			dateToObsMap.put(key, obsMapOnDate);
    		}
    		if(!obs.isVoided()) {
    			if(obsMapOnDate.get(obs.getConcept().getId()) != null && ((obs.getValueAsString(null) != null) && (!obs.getValueAsString(null).equals(obsMapOnDate.get(obs.getConcept().getId()).getValueAsString(null)))))
    				log.warn("Duplicate obs on the same day - "+key);
    			
    			obsMapOnDate.put(obs.getConcept().getId(), obs);
    		}
    	}
    	
    	// Make sure to sort the list
    	String[] keys = new String[dateToObsMap.size()];
    	dateToObsMap.keySet().toArray(keys);
    	Arrays.sort(keys);
    	
    	List<Map<Integer, Obs>> drugOrdersSorted = new ArrayList<Map<Integer, Obs>>(dateToObsMap.size());
    	for(String key : keys) {
    		drugOrdersSorted.add(dateToObsMap.get(key));
    	}
		return drugOrdersSorted;
	}
*/

	/**
	 * Format the given date according to the type ('short', 'long', 'ymd')
	 * 
	 * @param type format to use
	 * @param d Date to format
	 * @return a String with the formatted date
	 */
	private String formatDate(String type, Date d) {
		if (d == null)
			return "";

		DateFormat df = new SimpleDateFormat(type);
		return df.format(d);
	}

/*    public static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
    	List<T> list = new ArrayList<T>(c);
    	Collections.sort(list);
    	return list;
    }
*/
}
