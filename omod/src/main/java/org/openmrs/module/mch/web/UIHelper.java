package org.openmrs.module.mch.web;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Obs;
import org.openmrs.module.mch.mapper.Lab;
import org.openmrs.module.mch.web.model.AdultFlowsheetFormData;
import org.openmrs.module.mch.web.model.FlowsheetFormData;

public class UIHelper {
	
	public static String getCheckBoxWidget(boolean checked, String label) {
		StringBuilder builder = new StringBuilder(256);
		
		if(checked)
		{
			builder.append("<span class=\"checkbox-box-ticked\">");
			builder.append("&#x2713;");
		}
		else
		{
			builder.append("<span class=\"checkbox-box\">");
			builder.append("&nbsp;");
		}
		builder.append("</span>");
		builder.append("<span class=\"checkbox-label\">");
		builder.append(label);
		builder.append("</span>");
		return builder.toString();
	}
	
	public static String formatRegimenDisplaySummary(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return "";
		
		StringBuilder builder = new StringBuilder(1024);
		boolean firstDrug = true;
		
		for(DrugOrder drugOrder : drugOrders) {
			if(firstDrug)
				firstDrug = false;
			else
				builder.append("&nbsp;+&nbsp;");
			builder.append(formatRegimenDisplaySummary(drugOrder));
		}
		return builder.toString();
	}
	
	public static String formatRegimenDisplaySummaryPedi(List<DrugOrder> drugOrders) {
		if(drugOrders == null || drugOrders.size() == 0)
			return "";
		
		StringBuilder builder = new StringBuilder(1024);
		boolean firstDrug = true;
		Drug previousDrug = null;
		for(DrugOrder drugOrder : drugOrders) {
			if(drugOrder != null && drugOrder.getDrug() != null)
			{
				if(previousDrug == null || !previousDrug.equals(drugOrder.getDrug()))
				{
					if(firstDrug)
						firstDrug = false;
					else
						builder.append("&nbsp;+&nbsp;");
			
					builder.append(drugOrder.getDrug().getName());
					previousDrug = drugOrder.getDrug();
				}
			}
		}
		return builder.toString();
	}

	public static String formatRegimenDisplaySummary(DrugOrder drugOrder) {
		if(drugOrder == null || drugOrder.getDrug() == null)
			return "";
		
		String drugName = drugOrder.getDrug().getName();
		Double dose = drugOrder.getDose();
		String units = "";
		String frequency = "";
		if(drugOrder.getDoseUnits() != null)
			units = drugOrder.getDoseUnits().getDisplayString().replaceAll("tab\\(s\\)", "");
		if(drugOrder.getFrequency() != null)
			frequency = drugOrder.getConcept().getDisplayString().replaceAll(" x 7 days/week", "").replaceAll("day", "j");
		return drugName + " " + dose + units + " " + frequency;
	}

	public static String getCd4XAxis(FlowsheetFormData data) {
		Collection<Lab> cd4List = data.getObsMapper().getCd4ObsList();
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		
		//we want to start at the first CD4 date, if there is no CD4 
		//start at the current date, unless it is a kid then we
		//don't want to start looking at CD4 counts until they are 5
		Date minDate = data.getPatient().getBirthdate();
		calendar.setTime(minDate);
		calendar.add(Calendar.YEAR, 5);
		minDate = calendar.getTime();
		if(minDate.before(new Date()))
		{
			minDate = new Date();
		}
		if(cd4List != null && cd4List.size() > 0) {
			for(Lab obs : cd4List) {
				if(obs.getDate() != null && minDate.after(obs.getDate())) {
					minDate = obs.getDate();
				}
			}
		}
		calendar.setTime(minDate);
		int minYear = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(minYear, 1, 1);
		long min = calendar.getTimeInMillis();
		calendar.set(currentYear + 5, 1, 1);
		long max = calendar.getTimeInMillis();
		
		StringBuilder builder = new StringBuilder();
		builder.append("xaxis: { ");
		builder.append("mode: \"time\"");
		builder.append(", timeformat: \"%d/%m/%y\"");
		builder.append(", min: ").append(min);
		builder.append(", max: ").append(max);
		builder.append(", tickSize: [6, \"month\"]");
		//	max: 13,
		//	ticks: [
		//			for(int index = 0; index < 14; index++) {
		//				if(index > 0)
		//					out.print(", ");
		//				if(index < cd4List.size())
		//					out.print("["+index+", \""+formData.formatDate(AdultHivFlowsheetFormData.FormatDate_General, cd4List.get(index).getObsDatetime())+"\"]");
		//				else
		//					out.print("["+index+", \""+AdultHivFlowsheetFormData.DateTextPlaceHolder+"\"]");
		//			}
		//		] 
		
		builder.append(" }");
		
		return builder.toString();
	}
	
	public static String getCd4PercentXAxis(FlowsheetFormData data) {
		Calendar calendar = Calendar.getInstance();
		
		Date minDate = data.getPatient().getBirthdate();
		calendar.setTime(minDate);
		long min = calendar.getTimeInMillis();
		calendar.add(Calendar.YEAR, 5);
		long max = calendar.getTimeInMillis();
		
		StringBuilder builder = new StringBuilder();
		builder.append("xaxis: { ");
		builder.append("mode: \"time\"");
		builder.append(", timeformat: \"%d/%m/%y\"");
		builder.append(", min: ").append(min);
		builder.append(", max: ").append(max);
		builder.append(", tickSize: [3, \"month\"]");
		//	max: 13,
		//	ticks: [
		//			for(int index = 0; index < 14; index++) {
		//				if(index > 0)
		//					out.print(", ");
		//				if(index < cd4List.size())
		//					out.print("["+index+", \""+formData.formatDate(AdultHivFlowsheetFormData.FormatDate_General, cd4List.get(index).getObsDatetime())+"\"]");
		//				else
		//					out.print("["+index+", \""+AdultHivFlowsheetFormData.DateTextPlaceHolder+"\"]");
		//			}
		//		] 
		
		builder.append(" }");
		
		return builder.toString();
	}
	
	public static String getBMIXAxis(AdultFlowsheetFormData data) {
		Collection<Obs> weightList = data.getObsMapper().getWeightObsList();
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);
		
		Date minDate = new Date();
		if(weightList != null && weightList.size() > 0) {
			for(Obs obs : weightList) {
				if(obs.getObsDatetime() != null && minDate.after(obs.getObsDatetime())) {
					minDate = obs.getObsDatetime();
				}
			}
		}
		calendar.setTime(minDate);
		int minYear = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(minYear, 1, 1);
		long min = calendar.getTimeInMillis();
		calendar.set(currentYear + 5, 1, 1);
		long max = calendar.getTimeInMillis();
		
		StringBuilder builder = new StringBuilder();
		builder.append("xaxis: { ");
		builder.append("mode: \"time\"");
		builder.append(", timeformat: \"%d/%m/%y\"");
		builder.append(", min: ").append(min);
		builder.append(", max: ").append(max);
		builder.append(", tickSize: [6, \"month\"]");
		//	max: 13,
		//	ticks: [
		//			for(int index = 0; index < 14; index++) {
		//				if(index > 0)
		//					out.print(", ");
		//				if(index < cd4List.size())
		//					out.print("["+index+", \""+formData.formatDate(AdultHivFlowsheetFormData.FormatDate_General, cd4List.get(index).getObsDatetime())+"\"]");
		//				else
		//					out.print("["+index+", \""+AdultHivFlowsheetFormData.DateTextPlaceHolder+"\"]");
		//			}
		//		] 
		
		builder.append(" }");
		
		return builder.toString();
	}

}
