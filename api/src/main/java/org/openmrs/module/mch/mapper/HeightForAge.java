package org.openmrs.module.mch.mapper;

public class HeightForAge {
	
	private int ageInMonths;
	
	private double ageInMonthsDecimal;
	
	private int ageInDays;
	
	private int heightInCM;
	
	private double heightInCMDecimal;

	public int getAgeInMonths() {
		return ageInMonths;
	}

	public void setAgeInMonths(int ageInMonths) {
		this.ageInMonths = ageInMonths;
	}

	public int getHeightInCM() {
		return heightInCM;
	}

	public void setHeightInCM(int heightInCM) {
		this.heightInCM = heightInCM;
	}

	public double getHeightInCMDecimal() {
		return heightInCMDecimal;
	}

	public void setHeightInCMDecimal(double heightInCMDecimal) {
		this.heightInCMDecimal = heightInCMDecimal;
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
