package org.openmrs.module.mch.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.ConceptSet;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.module.heightweighttracker.mapper.WHOCalculations;
import org.openmrs.module.mch.impl.pih.ConceptDictionary;
import org.openmrs.module.mch.impl.pih.OIMapping;

public class VisitGroup {
	
	private List<Visit> visits;
	
	private WHOCalculations whoCalculations = new WHOCalculations();
	
	public VisitGroup(List<Visit> row) {
		this.visits = row;
	}
	
	public boolean isEmr() {
		return visits.size() > 0;
	}
	
	public Date getDate() {
		if(isEmr())
			return visits.iterator().next().getDate();

		return null;
	}
	
	public Obs getWeight() {
		for(Visit visit : visits) {
			Obs obs = visit.getWeight();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getHeight() {
		for(Visit visit : visits) {
			Obs obs = visit.getHeight();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getZScoreWeight() {
		for(Visit visit : visits) {
			Obs obs = visit.getZScoreWeight();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getZScoreHeight() {
		for(Visit visit : visits) {
			Obs obs = visit.getZScoreHeight();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getHeightWeightPercentile() {
		for(Visit visit : visits) {
			Obs obs = visit.getHeightWeightPercentile();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getFunctionalAbilityOfThePatient() {
		for(Visit visit : visits) {
			Obs obs = visit.getFunctionalAbilityOfThePatient();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getOI() {
		for(Visit visit : visits) {
			Obs obs = visit.getOI();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public List<Obs> getOIList()
	{
		List<Obs> obs = new ArrayList<Obs>();
		for(Visit visit : visits)
		{
			Obs ob = visit.getOI();
			if(ob != null)
			{
				obs.add(ob);
			}
		}
		
		return obs;
	}
	
	public List<Obs> getSTIList()
	{
		List<Obs> obs = new ArrayList<Obs>();
		for(Visit visit : visits)
		{
			Obs ob = visit.getSTI();
			if(ob != null)
			{
				obs.add(ob);
			}
		}
		
		return obs;
	}
	
	public Obs getSTI() {
		for(Visit visit : visits) {
			Obs obs = visit.getSTI();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getTBScreening() {
		for(Visit visit : visits) {
			Obs obs = visit.getTBScreening();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public boolean isTBScreeningPositive() {
		Obs obs = getTBScreening();
		if(obs == null)
			return false;
		
		Concept concept = obs.getValueCoded();
		if(concept != null && concept.getId() == ConceptDictionary.POSITIVE)
			return true;
		
		return false;
	}
	
	public boolean isTBScreeningNegative() {
		Obs obs = getTBScreening();
		if(obs == null)
			return false;
		
		Concept concept = obs.getValueCoded();
		if(concept != null && concept.getId() == ConceptDictionary.NEGATIVE)
			return true;
		
		return false;
	}
	
	public Obs getFamilyPlanning() {
		for(Visit visit : visits) {
			Obs obs = visit.getFamilyPlanning();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public String getFamilyPlanningString(){
		Obs o = getFamilyPlanning();
		if (o != null && o.getValueCoded() != null){
			if (o.getValueCoded().getConceptId().equals(ConceptDictionary.ABSTINENCE))
				return "A";
			else if (o.getValueCoded().getConceptId().equals(ConceptDictionary.CONDOMS))
				return "C";
			else if (o.getValueCoded().getConceptId().equals(ConceptDictionary.NATURAL_FAMILY_PLANNING))
				return "PF"; 
			else if (o.getValueCoded().getConceptId().equals(ConceptDictionary.NONE))
				return "R"; 
			else
			{
				if(o != null && o.getValueCoded() != null)
				{
					return o.getValueCoded().getDisplayString();
				}
			}
		}
		return "";
	}
	
	public Obs getPregnancyStatus() {
		for(Visit visit : visits) {
			Obs obs = visit.getPregnancyStatus();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getNextVisit()
	{
		for(Visit visit : visits) {
			Obs obs = visit.getNextVisit();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public boolean isPregnancyStatusYes() {
		Obs obs = getPregnancyStatus();
		if(obs == null)
			return false;
		
		Concept concept = obs.getValueCoded();
		if(concept != null && concept.getId() == ConceptDictionary.YES)
			return true;
		
		return false;
	}
	
	public boolean isPregnancyStatusNo() {
		Obs obs = getPregnancyStatus();
		if(obs == null)
			return false;
		
		Concept concept = obs.getValueCoded();
		if(concept != null && concept.getId() == ConceptDictionary.NO)
			return true;
		
		return false;
	}
	
	public Obs getNumberOfMissedDosesInThePastMonth() {
		for(Visit visit : visits) {
			Obs obs = visit.getNumberOfMissedDosesInThePastMonth();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public String getNumberOfMissedDosesInThePastMonthString(){
		Obs o = getNumberOfMissedDosesInThePastMonth();
		if (o != null && o.getValueNumeric() != null){
			if (o.getValueNumeric() <= 3)
				return "Tres bon";
			if (o.getValueNumeric() > 3 && o.getValueNumeric() <=8)
				return "Bon";
			if (o.getValueNumeric() >=9)
				return "Faible";
		}
		return "";
	}
	
	public Obs getCreat() {
		for(Visit visit : visits) {
			Obs obs = visit.getCreat();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getReasonForPoorAdherenceToAntiRetoroviralTherapy() {
		for(Visit visit : visits) {
			Obs obs = visit.getReasonForPoorAdherenceToAntiRetoroviralTherapy();
			if(obs != null)
				return obs;
		}
		return null;
	}
		
	public Encounter getEncounter() {
		if(visits == null || visits.size() == 0)
			return null;
		
		for(Visit obs : visits) {
			if(obs.getEncounter() != null)
				return obs.getEncounter();
		}
		
		return null;
	}
	
	public String getOpportunisticInfectionForDisplay(){
		StringBuilder display = new StringBuilder("");
		List<Obs> obs = getOIList();
		
		for(Obs o: obs)
		{
			if (o != null && o.getValueCoded() != null && OIMapping.getOIs().contains(o.getValueCoded().getConceptId()))
			{
				if(display.length() != 0)
				{
					display.append(", ");
				}
				display.append(o.getValueAsString(Context.getLocale()));
			}
			if (o != null && (o.getConcept().getConceptId().equals(ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED_NON_CODED)  || o.getConcept().getConceptId().equals(ConceptDictionary.OPPORTUNISTIC_INFECTION_NON_CODED))&& o.getValueText() != null && !"".equals(o.getValueText()))
			{
				if(display.length() != 0)
				{
					display.append(", ");
				}
				display.append(o.getValueAsString(Context.getLocale()));
			}
		}
		return display.toString();
	}
	
	public String getStiInfectionForDisplay(){
		StringBuilder display = new StringBuilder("");
		List<Obs> obs = getSTIList();
		
		for(Obs o: obs)
		{
			if(display.length() != 0)
			{
				display.append(", ");
			}
			display.append(o.getValueAsString(Context.getLocale()));
			
		}
		return display.toString();
	}
	
	public boolean isOpportunisticInfectionTrue(){
		boolean isOpportunisitic = false;
		List<Obs> obs = getOIList();
		for(Obs o: obs)
		{
			if (o != null && o.getValueCoded() != null && OIMapping.getOIs().contains(o.getValueCoded().getConceptId()))
			{
				isOpportunisitic = true;
			}
			if (o != null && (o.getConcept().getConceptId().equals(ConceptDictionary.CURRENT_OPPORTUNISTIC_INFECTION_OR_COMORBIDITY_CONFIRMED_OR_PRESUMED_NON_CODED) || o.getConcept().getConceptId().equals(ConceptDictionary.OPPORTUNISTIC_INFECTION_NON_CODED)) && o.getValueText() != null && !"".equals(o.getValueText()))
			{
				isOpportunisitic = true;
			}
		}
		return isOpportunisitic;
	}

	
	public boolean isOpportunisticInfectionFalse(){
		return !isOpportunisticInfectionTrue();
	}
	
	public boolean isStiTrue(){
		boolean isSti = false;
		
		List<Obs> obs = getOIList();
		Obs sti = getSTI();
		if(sti != null)
		{
			obs.add(sti);
		}
		for(Obs o: obs)
		{
			if (o != null && o.getValueCoded() != null && getOIsThatAreSTIs().contains(o.getValueCoded().getConceptId()))
				isSti = true;
		}
		return isSti;
	}
	
	public boolean isStiFalse(){
		return !isStiTrue();
	}
	
	public String getAdherenceLevel()
	{
		if(getNumberOfMissedDosesInThePastMonth() != null)
		{
			Double adherenceObs = getNumberOfMissedDosesInThePastMonth().getValueNumeric();
			
			int adherence = 0;
			if(adherenceObs != null)
			{
				adherence = adherenceObs.intValue();
			}
			
			if(adherence <= 3)
			{
				return "Tresbon";
			}
			
			if(adherence > 3 && adherence <= 8)
			{
				return "Bon";
			}
			
			if(adherence > 8)
			{
				return "Faible";
			}
		}
		
		return null;
		
	}
	
	public String getFunctionalAbilityOfThePatientString() {
		Obs o =  getFunctionalAbilityOfThePatient();
		
		if (o != null && o.getValueCoded() != null){
			if (o.getValueCoded().getConceptId().equals(ConceptDictionary.NORMAL))
				return "A";
			if (o.getValueCoded().getConceptId().equals(ConceptDictionary.BEDRIDDEN))
				return "C";
			if (o.getValueCoded().getConceptId().equals(ConceptDictionary.ASSISTANCE_REQUIRED))
				return "B";
		}
		return "";
	}
	
	public static Set<Integer> getOIsThatAreSTIs(){
		Set<Integer> ret = new HashSet<Integer>();
		Concept c = Context.getConceptService().getConcept(ConceptDictionary.OIS_THAT_ARE_STIS);
		if(c != null)
		{
			for (ConceptSet cs : c.getConceptSets()){
				ret.add(cs.getConcept().getConceptId());
			}
		}
		Concept d = Context.getConceptService().getConcept(ConceptDictionary.STI_SET);
		if(d != null)
		{
			for (ConceptSet cs : d.getConceptSets()){
				ret.add(cs.getConcept().getConceptId());
		}
		}
		return ret;
	}

	public String getCalculatedZScoreHeight() {
		Obs height = getHeight();
		
		return whoCalculations.getCalculatedZScoreHeight(height);
	}
	
	public String getCalculatedZScoreWeight() {
		Obs weight = getWeight();
		
		return whoCalculations.getCalculatedZScoreWeight(weight);
		
	}
	
	public String getCalculatedHeightWeightPercentile()
	{
		Obs weight = getWeight();
		Obs height = getHeight();
		
		return whoCalculations.getCalculatedHeightWeightPercentile(height, weight);
	}

}