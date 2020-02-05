package com.walkertribe.ian.enums;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.model.Model;

/**
 * The types of creatures. Note: For some reason, wrecks count as creatures.
 * @author rwalker
 */
public enum CreatureType {
	TYPHON(null),
	WHALE("dat/whale.dxs"),
	SHARK("dat/monster-sha.dxs"),
	DRAGON("dat/monster-drag.dxs"),
	PIRANHA("dat/monster-pira.dxs,dat/monster-pira-jaw.dxs"),
	CHARYBDIS("dat/monster-cone.dxs"),
	NSECT("dat/monster-bug.dxs"),
	JELLY("dat/jelly-body.dxs,dat/jelly-crown.dxs"),
	WRECK("dat/derelict.dxs");

	private String modelPaths;

	CreatureType(String modelPaths) {
		this.modelPaths = modelPaths;
	}

	/**
     * Returns the Model object for this creature, using the PathResolver from
     * the given VesselData, or null if it has no model. (The "classic" space
     * monster is made of multiple rotating parts and is not a simple model,
     * thus it will return null.)
     */
    public Model getModel(Context ctx) {
    	return modelPaths != null ? ctx.getModel(modelPaths) : null;
    }
}