package com.walkertribe.ian.enums;

/**
 * The four possible ordnance tube states.
 * @author rjwut
 */
public enum TubeState {
	UNLOADED, // the tube is ready to load
	LOADED,   // the tube is ready to fire or unload
	LOADING,  // wait for the tube to finish loading
	UNLOADING // wait for the tube to finish unloading
}