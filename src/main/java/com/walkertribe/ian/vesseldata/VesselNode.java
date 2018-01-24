package com.walkertribe.ian.vesseldata;

import java.io.IOException;
import java.io.InputStream;

import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.util.ByteArrayReader;
import com.walkertribe.ian.util.GridCoord;

/**
 * <p>
 * Represents a single node in the VesselInternals.
 * </p>
 * <p>
 * Every node has two sets of coordinates: a grid coordinate (within the
 * 5 x 5 x 10 ship system grid) and a physical coordinate (within the
 * coordinate space of the vessels 3D model).
 * </p>
 * <p>
 * There are three types of nodes:
 * </p>
 * <ul>
 * <li>System nodes: Nodes where a ship system is located. These are
 * represented by large dots on the engineering console in the stock client.
 * Each system node has a corresponding ship system.</li>
 * <li>Hallway nodes: Nodes which are within the ship, but which don't contain
 * a ship system.</li>
 * <li>Empty nodes: Nodes which are outside the ship. These are inaccessible to
 * DAMCON teams.</li>
 * </ul>
 * @author rjwut
 */
public class VesselNode {
	static final int BLOCK_SIZE = 32;
	static final int EMPTY_NODE_VALUE = -2;
	static final int HALLWAY_NODE_VALUE = -1;

	private GridCoord coords;
	private float x;
	private float y;
	private float z;
	private boolean accessible;
	private ShipSystem system;

	/**
	 * Creates a new VesselNode at the given grid coordinates. This node will
	 * be located at (0,0,0) in the physical coordinate space and will be
	 * inaccessible.
	 */
	VesselNode (GridCoord coords) {
		this.coords = coords;
	}

	/**
	 * Reads the VesselNode at the indicated grid coordinates from the given
	 * InputStream. The buffer is a byte array with a size equal to
	 * VesselNode.BLOCK_SIZE.
	 */
	VesselNode (InputStream in, GridCoord coords, byte[] buffer) throws InterruptedException, IOException {
		ByteArrayReader.readBytes(in, BLOCK_SIZE, buffer);
		ByteArrayReader reader = new ByteArrayReader(buffer);
		this.coords = coords;
		x = reader.readFloat();
		y = reader.readFloat();
		z = reader.readFloat();
		int typeValue = reader.readInt();
		accessible = typeValue != EMPTY_NODE_VALUE;

		if (accessible && typeValue != HALLWAY_NODE_VALUE) {
			system = ShipSystem.values()[typeValue];
		}
	}

	/**
	 * Returns the GridCoord for this node's location in the system grid.
	 */
	public GridCoord getGridCoord() {
		return coords;
	}

	/**
	 * Returns the X-coordinate of this node relative to the origin of the
	 * ship's model coordinates.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the X-coordinate of this node relative to the origin of the ship's
	 * model coordinates.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the Y-coordinate of this node relative to the origin of the
	 * ship's model coordinates.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Sets the Y-coordinate of this node relative to the origin of the ship's
	 * model coordinates.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Returns the Z-coordinate of this node relative to the origin of the
	 * ship's model coordinates.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets the Z-coordinate of this node relative to the origin of the ship's
	 * model coordinates.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Returns true if this node represents a location within the system grid;
	 * false if it's outside the ship.
	 */
	public boolean isAccessible() {
		return accessible;
	}

	/**
	 * Sets whether this node is accessible or not. Note that if you set this
	 * to false, the system property will be set to null as well.
	 */
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;

		if (!accessible) {
			system = null;
		}
	}

	/**
	 * Returns the ShipSystem found here, or null if there is none.
	 */
	public ShipSystem getSystem() {
		return system;
	}

	/**
	 * Sets the ship system located at this node. Note that if you set this to
	 * a non-null value, the accessible property will be set to true as well.
	 */
	public void setSystem(ShipSystem system) {
		this.system = system;

		if (system != null) {
			accessible = true;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof VesselNode)) {
			return false;
		}

		VesselNode that = (VesselNode) obj;
		return coords.equals(that.coords);
	}

	@Override
	public int hashCode() {
		return coords.hashCode();
	}

	@Override
	public String toString() {
		return coords.toString() + '=' + (accessible ? (system != null ? system : "hallway") : "empty");
	}
}
