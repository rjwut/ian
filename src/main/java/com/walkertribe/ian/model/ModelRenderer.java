package com.walkertribe.ian.model;

import java.awt.Graphics2D;

import com.walkertribe.ian.vesseldata.Vessel;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Interface for classes which are capable of rendering Models to a Graphics2D
 * context. The generic <T> should be the class of the parameters object which
 * is passed into the render() methods.
 * 
 * Implementations may wish to take advantage of the Matrix class for performing
 * matrix transformations.
 * 
 * @author rjwut
 */
public interface ModelRenderer<T> {
	/**
	 * Renders the given Model.
	 */
	public void render(Graphics2D g, Model model, T params);

	/**
	 * Renders the given Vessel. Implementations may support rendering of system
	 * nodes (if the given Vessel has them).
	 */
	public void render(Graphics2D g, Vessel vessel, T params);

	/**
	 * Renders the given ArtemisObject, if it has a Model; otherwise, nothing
	 * happens. Implementations may support rendering of system nodes (if the
	 * given ArtemisObject has them), including their current damage state, and
	 * may also render DAMCON team locations.
	 */
	public void render(Graphics2D g, ArtemisObject object, T params);
}