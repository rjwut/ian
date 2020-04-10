package com.walkertribe.ian;

import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.util.Grid;
import com.walkertribe.ian.vesseldata.VesselData;

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
	 * Given the path to an .snt file, returns a Grid object that describes the
	 * node nodes stored in that file. Grids returned by this method are locked.
	 */
	public Grid getGrid(String sntPath);
}
