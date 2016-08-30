package com.walkertribe.ian.vesseldata;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.walkertribe.ian.util.GridCoord;

/**
 * An object which describes the internal system grid of a Vessel. Note that while the .snt file
 * contains an entry for every coordinate in the internal grid, this object will only contain 
 * VesselNodes for coordinates that correspond to a ship system or a hallway.
 * @author rjwut
 */
public class VesselInternals {
	public static final int GRID_SIZE_X = 5;
	public static final int GRID_SIZE_Y = 5;
	public static final int GRID_SIZE_Z = 10;

	private Map<GridCoord, VesselNode> map = new LinkedHashMap<GridCoord, VesselNode>();
	private Set<VesselNodeConnection> connections = new HashSet<VesselNodeConnection>();
	private byte[] buffer = new byte[VesselNode.BLOCK_SIZE];

	public VesselInternals(PathResolver pathResolver, String sntPath) {
		InputStream in = null;

		try {
			in = pathResolver.get(sntPath);
			load(new BufferedInputStream(in));
		} catch (MalformedURLException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					// don't care
				}
			}
		}
	}

	/**
	 * Builds a VesselInternals object from the .snt file read from the given InputStream.
	 */
	private void load(InputStream in) throws InterruptedException, IOException {
		for (int x = 0; x < GRID_SIZE_X; x++) {
			for (int y = 0; y < GRID_SIZE_Y; y++) {
				for (int z = 0; z < GRID_SIZE_Z; z++) {
					GridCoord coords = GridCoord.getInstance(x, y, z); 
					VesselNode node = new VesselNode(in, coords, buffer);

					if (node.isAccessible()) {
						map.put(coords, node);
						buildAdjacency(node, x - 1, y, z);
						buildAdjacency(node, x, y - 1, z);
						buildAdjacency(node, x, y, z - 1);
					}
				}
			}
		}
	}

	private void buildAdjacency(VesselNode node, int x, int y, int z) {
		VesselNode adjacentNode = map.get(GridCoord.getInstance(x, y, z));

		if (adjacentNode != null) {
			connections.add(new VesselNodeConnection(adjacentNode, node));
		}
	}

	/**
	 * Returns the VesselNode located at the given internal grid coordinates, or null if there is
	 * no VesselNode at that location.
	 */
	public VesselNode get(int x, int y, int z) {
		return map.get(GridCoord.getInstance(x, y, z));
	}

	/**
	 * Iterates all VesselNodes.
	 */
	public Iterator<VesselNode> nodeIterator() {
		return map.values().iterator();
	}

	/**
	 * Iterates all VesselNodeConnections.
	 */
	public Iterator<VesselNodeConnection> connectionIterator() {
		return connections.iterator();
	}
}