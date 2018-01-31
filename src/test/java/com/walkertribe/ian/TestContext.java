package com.walkertribe.ian;

import java.util.HashMap;
import java.util.Map;

import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.vesseldata.VesselData;
import com.walkertribe.ian.vesseldata.VesselInternals;

/**
 * A Context implementation where that can be pre-populated with the specific
 * data needed for a test that depends on a Context object.
 * @author rjwut
 */
public class TestContext implements Context {
	private VesselData vesselData;
	private Map<String, Model> modelMap = new HashMap<String, Model>();
	private Map<String, VesselInternals> internalsMap = new HashMap<String, VesselInternals>();

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
	public VesselInternals getInternals(String sntPath) {
		return internalsMap.get(sntPath);
	}

	public void putInternals(String sntPath, VesselInternals internals) {
		internalsMap.put(sntPath, internals);
	}
}
