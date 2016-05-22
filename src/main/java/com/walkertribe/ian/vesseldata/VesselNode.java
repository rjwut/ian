package com.walkertribe.ian.vesseldata;

import java.io.IOException;
import java.io.InputStream;

import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.util.ByteArrayReader;
import com.walkertribe.ian.util.GridCoord;

public class VesselNode {
	static final int BLOCK_SIZE = 32;
	private static final int EMPTY_NODE_VALUE = -2;
	private static final int HALLWAY_NODE_VALUE = -1;

	private GridCoord coords;
	private float x;
	private float y;
	private float z;
	private boolean accessible;
	private ShipSystem system;

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
	 * Returns the X-coordinate of this node relative to the origin of the ship's model coordinates.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Returns the Y-coordinate of this node relative to the origin of the ship's model coordinates.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Returns the Z-coordinate of this node relative to the origin of the ship's model coordinates.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Returns true if it's possible for DAMCON teams to access this node; false otherwise.
	 */
	public boolean isAccessible() {
		return accessible;
	}

	/**
	 * Returns the ShipSystem found here, or null if there is none.
	 */
	public ShipSystem getSystem() {
		return system;
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
