package com.walkertribe.ian;

import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.vesseldata.VesselData;
import com.walkertribe.ian.vesseldata.VesselInternals;

/**
 * Interface for classes which can return Artemis resources.
 * @author rjwut
 */
public interface Context {
	/**
	 * Returns a VesselData object describing all the information in
	 * vesselData.xml.
	 */
	public VesselData getVesselData();

	/**
	 * Given a comma-delimited list of dxsPaths, returns a Model object that
	 * describes the union of the 3D models stored in those files.
	 */
	public Model getModel(String dxsPaths);

	/**
	 * Given the path to an .snt file, returns a VesselInternals object that
	 * describes the node grid stored in that file.
	 */
	public VesselInternals getInternals(String sntPath);
}
