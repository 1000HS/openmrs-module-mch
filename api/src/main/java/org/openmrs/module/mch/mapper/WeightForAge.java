package org.openmrs.module.mch.mapper;

public class WeightForAge {
	
	private int ageInMonths;
	
	private double ageInMonthsDecimal;
	
	private int ageInDays;
	
	private int weightInKG;
	
	private double weightInKGDecimal;

	public int getAgeInMonths() {
		return ageInMonths;
	}

	public void setAgeInMonths(int ageInMonths) {
		this.ageInMonths = ageInMonths;
	}

	public int getWeightInKG() {
		return weightInKG;
	}

	public void setWeightInKG(int weightInKG) {
		this.weightInKG = weightInKG;
	}

	public double getWeightInKGDecimal() {
		return weightInKGDecimal;
	}

	public void setWeightInKGDecimal(double weightInKGDecimal) {
		this.weightInKGDecimal = weightInKGDecimal;
	}

	public int getAgeInDays() {
		return ageInDays;
	}

	public void setAgeInDays(int ageInDays) {
		this.ageInDays = ageInDays;
	}

	public double getAgeInMonthsDecimal() {
		return ageInMonthsDecimal;
	}

	public void setAgeInMonthsDecimal(double ageInMonthsDecimal) {
		this.ageInMonthsDecimal = ageInMonthsDecimal;
	}
}
