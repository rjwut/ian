package com.walkertribe.ian.vesseldata;

/**
 * Represents a connection between two VesselNodes.
 * @author rjwut
 */
public class VesselNodeConnection {
	private VesselNode node1;
	private VesselNode node2;

	VesselNodeConnection(VesselNode node1, VesselNode node2) {
		this.node1 = node1;
		this.node2 = node2;
	}

	public VesselNode getNode1() {
		return node1;
	}

	public VesselNode getNode2() {
		return node2;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof VesselNodeConnection)) {
			return false;
		}

		VesselNodeConnection that = (VesselNodeConnection) obj;
		return node1.equals(that.node1) && node2.equals(that.node2);
	}

	@Override
	public int hashCode() {
		int hash1 = node1.hashCode();
		int hash2 = node2.hashCode();
		return 31 * (hash1 ^ (hash1 >>> 32)) + (hash2 ^ (hash2 >>> 32));
	}

	@Override
	public String toString() {
		return node1 + " <=> " + node2;
	}
}