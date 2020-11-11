package org.openmrs.module.mch.mapper;

import java.util.List;

import org.openmrs.Concept;
import org.openmrs.Obs;

public interface ImagePedi extends BaseObs {

	public abstract List<Obs> getComments();

	public abstract boolean isChestXRay();

	public abstract boolean isAbdominalUltrasound();

	public abstract boolean isOther();

}
