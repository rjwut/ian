package com.walkertribe.ian.vesseldata;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.util.GridCoord;

/**
 * An object which describes the internal system grid of a Vessel. Note that while the .snt file
 * contains an entry for every coordinate in the internal grid, this object will only contain 
 * VesselNodes for coordinates that correspond to a ship system or a hallway.
 * @author rjwut
 */
public class VesselInternals {
	private static final byte[] RESERVED = new byte[16];

	private VesselNode[] nodes = new VesselNode[GridCoord.LENGTH];
	private byte[] buffer = new byte[VesselNode.BLOCK_SIZE];

	/**
	 * Creates a new, empty VesselInternals object. All VesselNodes will be
	 * pre-populated to be inaccessible and located at (0,0,0) in the physical
	 * coordinate space.
	 */
	public VesselInternals() {
		for (int x = 0; x < GridCoord.MAX_X; x++) {
			for (int y = 0; y < GridCoord.MAX_Y; y++) {
				for (int z = 0; z < GridCoord.MAX_Z; z++) {
					GridCoord coords = GridCoord.get(x, y, z);
					nodes[coords.index()] = new VesselNode(coords);
				}
			}
		}
	}

	/**
	 * Imports an .snt file.
	 */
	public VesselInternals(InputStream in) {
		if (!(in instanceof BufferedInputStream)) {
			in = new BufferedInputStream(in);
		}

		try {
			for (int x = 0; x < GridCoord.MAX_X; x++) {
				for (int y = 0; y < GridCoord.MAX_Y; y++) {
					for (int z = 0; z < GridCoord.MAX_Z; z++) {
						GridCoord coords = GridCoord.get(x, y, z);
						nodes[coords.index()] = new VesselNode(in, coords, buffer);
					}
				}
			}
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Returns the VesselNode located at the given internal grid coordinates.
	 * This method will never return null; if the given coordinates are outside
	 * the system grid, you'll get back a VesselNode where isAccessible()
	 * returns false.
	 */
	public VesselNode get(GridCoord coord) {
		return nodes[coord.index()];
	}

	/**
	 * Iterates all VesselNodes (including those outside the ship).
	 */
	public Iterator<VesselNode> nodeIterator() {
	    return new Iterator<VesselNode>() {
	        private int index = 0;

	        @Override
            public boolean hasNext() {
	            return index < GridCoord.LENGTH;
            }

            @Override
            public VesselNode next() {
                return nodes[index++];
            }
	    };
	}

	/**
	 * Exports this VesselInternals object in .snt format to the given
	 * OutputStream.
	 */
	public void write(OutputStream out) throws IOException {
		for (int x = 0; x < GridCoord.MAX_X; x++) {
			for (int y = 0; y < GridCoord.MAX_X; y++) {
				for (int z = 0; z < GridCoord.MAX_X; z++) {
					VesselNode node = nodes[GridCoord.computeIndex(x, y, z)];
					GridCoord coord = node.getCoord();
					writeFloat(out, coord.x());
					writeFloat(out, coord.y());
					writeFloat(out, coord.z());
					int type;

					if (!node.isAccessible()) {
						type = VesselNode.EMPTY_NODE_VALUE;
					} else {
						ShipSystem system = node.getSystem();

						if (system == null) {
							type = VesselNode.HALLWAY_NODE_VALUE;
						} else {
							type = system.ordinal();
						}
					}

					writeInt(out, type);
					out.write(RESERVED);
				}
			}
		}
	}

	/**
	 * Writes a float value to the given OutputStream.
	 */
	private void writeFloat(OutputStream out, float v) throws IOException {
		writeInt(out, Float.floatToRawIntBits(v));
	}

	/**
	 * Writes an int value to the given OutputStream.
	 */
	private void writeInt(OutputStream out, int v) throws IOException {
		buffer[0] = (byte) (0xff & v);
		buffer[1] = (byte) (0xff & (v >> 8));
		buffer[2] = (byte) (0xff & (v >> 16));
		buffer[3] = (byte) (0xff & (v >> 24));
		out.write(buffer, 0, 4);
	}
}