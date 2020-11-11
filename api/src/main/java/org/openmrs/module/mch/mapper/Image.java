package org.openmrs.module.mch.mapper;

import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Obs;

public interface Image extends BaseObs {

	public abstract List<Obs> getComments();

	public abstract boolean isChestXRay();

	public abstract boolean isCTHead();

	public abstract boolean isOther();

}
