package com.walkertribe.ian;

import java.util.HashMap;
import java.util.Map;

import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.util.Grid;
import com.walkertribe.ian.vesseldata.VesselData;

/**
 * A Context implementation where that can be pre-populated with the specific
 * data needed for a test that depends on a Context object.
 * @author rjwut
 */
public class TestContext implements Context {
	private VesselData vesselData;
	private Map<String, Model> modelMap = new HashMap<String, Model>();
	private Map<String, Grid> gridMap = new HashMap<String, Grid>();

	@Override
	public VesselData getVesselData() {
		return vesselData;
	}

	public void setVesselData(VesselData vesselData) {
		this.vesselData = vesselData;
	}

	@Override
	public Model getModel(String dxsPaths) {
		return modelMap.get(dxsPaths);
	}

	public void putModel(String dxsPaths, Model model) {
		modelMap.put(dxsPaths, model);
	}

	@Override
	public Grid getGrid(String sntPath) {
		return gridMap.get(sntPath);
	}

	public void putGrid(String sntPath, Grid grid) {
		gridMap.put(sntPath, grid);
	}
}
