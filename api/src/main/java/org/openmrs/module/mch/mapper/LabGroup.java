package org.openmrs.module.mch.mapper;

import java.util.Date;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.module.mch.impl.pih.ConceptDictionary;

/**
 * Obs data can be represented in a number of ways.  
 * <ol>
 * <li> A SGOT lab test might entered directly, so, you would there 
 * would be an obs with a concept id = SGOT (653) and value.  Look at the lab 
 * entry for details on this.</li>
 * <li> SGOT lab test might be part of a construct like 2214 (OTHER LAB TEST CONSTRUCT).
 * The question is TESTS ORDERED (1271) and the value would be SGOT (653).  And the test result
 * is OTHER LAB TEST RESULT (2216).  Diabetes for on synctest is an example of this.</li>
 * </ol>
 * <p>
 * This container is meant to make it easier to deal with various ways data can be stored
 * at the UI level.  This class is not efficient and could be designed better.  Refactoring
 * once all the cases are identified should be done.
 * <p>
 * NOTE: This class assumes that any test is not in the object graph more than
 * once.  If there are two representations of SGOT then only the first encountered is used.
 * <p>
 * NOTE: This does not handle obs group embedded within an obs group.  
 */
public class LabGroup {
	
	private List<Lab> labs;
	
	public LabGroup(List<Lab> row) {
		this.labs = row;
	}
	
	public boolean isEmr() {
		return labs.size() > 0;
	}
	
	public Date getDate() {
		if(isEmr())
			return labs.iterator().next().getDate();

		return null;
	}
	
	public Obs getHb() {
		for(Lab lab : labs) {
			Obs obs = lab.getHb();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getHt() {
		for(Lab lab : labs) {
			Obs obs = lab.getHt();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getGb() {
		for(Lab lab : labs) {
			Obs obs = lab.getGb();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getNeutro() {
		for(Lab lab : labs) {
			Obs obs = lab.getNeutro();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getLympho() {
		for(Lab lab : labs) {
			Obs obs = lab.getLympho();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getPlt() {
		for(Lab lab : labs) {
			Obs obs = lab.getPlt();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getSgot() {
		for(Lab lab : labs) {
			Obs obs = lab.getSgot();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getSgpt() {
		for(Lab lab : labs) {
			Obs obs = lab.getSgpt();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getCreat() {
		for(Lab lab : labs) {
			Obs obs = lab.getCreat();
			if(obs != null)
				return obs;
		}
		return null;
	}
	public Obs getCreatClearence() {
		for(Lab lab : labs) {
			Obs obs = lab.getCreatClearence();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getGlucose() {
		for(Lab lab : labs) {
			Obs obs = lab.getGlucose();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getRpr() {
		for(Lab lab : labs) {
			Obs obs = lab.getRpr();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public boolean isRprPositive() {
		Obs obs = getRpr();
		if(obs == null)
			return false;
		
		Concept concept = obs.getValueCoded();
		if(concept != null && concept.getId() == ConceptDictionary.REACTIVE)
			return true;
		
		return false;
	}
	
	public boolean isRprNegative() {
		Obs obs = getRpr();
		if(obs == null)
			return false;
		
		Concept concept = obs.getValueCoded();
		if(concept != null && concept.getId() == ConceptDictionary.NON_REACTIVE)
			return true;
		
		return false;
	}
	
	public boolean isRprNotMade() {
		Obs obs = getRpr();
		if(obs == null)
			return false;
		
		Concept concept = obs.getValueCoded();
		if(concept != null && concept.getId() == ConceptDictionary.NOT_DONE)
			return true;
		
		return false;
	}
	
	public Obs getCd4() {
		for(Lab lab : labs) {
			Obs obs = lab.getCd4();
			if(obs != null)
				return obs;
		}
		return null;
	}

	public Obs getCd4Percentage() {
		for(Lab lab : labs) {
			Obs obs = lab.getCd4Percentage();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public Obs getViralLoad() {
		for(Lab lab : labs) {
			Obs obs = lab.getViralLoad();
			if(obs != null)
				return obs;
		}
		return null;
	}
	
	public String getOtherTests() {
		StringBuilder builder = new StringBuilder();
		for(Lab lab : labs) {
			int index = 0;
			List<Obs> results = lab.getOtherLabTestResult();
			for(Obs obs: lab.getOtherLabTestName())
			{
				boolean nameAdded = false;
				if(obs != null && obs.getValueAsString(null) != null) {
					if(builder.length() > 0)
						builder.append("; ");
					builder.append(obs.getValueAsString(null));
					nameAdded = true;
				}
			
				if(results.size() > index)
				{
					obs = results.get(index);
					if(obs != null && obs.getValueAsString(null) != null && nameAdded) {
						builder.append(" = ");
						builder.append(obs.getValueAsString(null));
					}
				}
				index++;
			}
		}
		return builder.toString();		
	}
	
	public Encounter getEncounter() {
		if(labs == null || labs.size() == 0)
			return null;
		
		for(Lab obs : labs) {
			if(obs.getEncounter() != null)
				return obs.getEncounter();
		}
		
		return null;
	}



}
