package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.GridCoord;

/**
 * Send a DAMCON team to a grid location.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_FOUR_INTS, subtype = 0x04)
public class EngSendDamconPacket extends BaseArtemisPacket {
    private int mTeamNumber;
    private GridCoord mCoord;

    /**
     * Send the team to grid node at x,y,z.
     * @param teamNumber int [0, TEAMS) where TEAMS is probably 3
     * @param x Destination X-coordinate in the system grid
     * @param y Destination Y-coordinate in the system grid
     * @param z Destination Z-coordinate in the system grid
     */
    public EngSendDamconPacket(int teamNumber, int x, int y, int z) {
    	this(teamNumber, GridCoord.getInstance(x, y, z));
    }

    /**
     * @param teamNumber int [0, TEAMS) where TEAMS is probably 3
     * @param coord Destination coordinates in the system grid
     */
    public EngSendDamconPacket(int teamNumber, GridCoord coord) {
        if (teamNumber < 0) {
        	throw new IllegalArgumentException(
        			"DAMCON team number can't be less than 0"
        	);
        }

        mTeamNumber = teamNumber;
        mCoord = coord;
    }

    public EngSendDamconPacket(PacketReader reader) {
        reader.skip(4); // subtype
        mTeamNumber = reader.readInt();
        mCoord = GridCoord.getInstance(reader.readInt(), reader.readInt(), reader.readInt());
    }

    /**
     * The number identifying this DAMCON team.
     */
    public int getTeamNumber() {
    	return mTeamNumber;
    }

    /**
     * The grid coordinates to which this DAMCON team is travelling. 
     */
    public GridCoord getDestination() {
    	return mCoord;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer	.writeInt(0x04) // subtype
				.writeInt(mTeamNumber)
				.writeInt(mCoord.getX())
				.writeInt(mCoord.getY())
				.writeInt(mCoord.getZ());
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("Team #").append(mTeamNumber).append(" to ").append(mCoord);
	}
}