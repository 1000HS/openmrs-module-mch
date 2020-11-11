package org.openmrs.module.mch.mapper;

import java.util.Date;
import java.util.List;

import org.openmrs.Obs;

public interface Lab extends BaseObs {

	public abstract Date getDate();

	public abstract Obs getHb();

	public abstract Obs getHt();

	public abstract Obs getGb();

	public abstract Obs getNeutro();

	public abstract Obs getLympho();

	public abstract Obs getPlt();

	public abstract Obs getSgot();

	public abstract Obs getSgpt();

	public abstract Obs getCreat();

	public abstract Obs getCreatClearence();

	public abstract Obs getGlucose();

	public abstract Obs getRpr();

	public abstract Obs getCd4Percentage();

	public abstract List<Obs> getOtherLabTestName();

	public abstract List<Obs> getOtherLabTestResult();

	public abstract Obs getCd4();

	public abstract Obs getViralLoad();

}
