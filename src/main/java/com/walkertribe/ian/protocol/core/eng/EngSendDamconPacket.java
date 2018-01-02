package com.walkertribe.ian.protocol.core.eng;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.GridCoord;

/**
 * Send a DAMCON team to a grid location.
 * @author dhleong
 */
public class EngSendDamconPacket extends BaseArtemisPacket {
    private static final PacketType TYPE = CorePacketType.VALUE_FOUR_INTS;
    private static final byte SUBTYPE = 0x04;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.CLIENT, TYPE, SUBTYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return EngSendDamconPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new EngSendDamconPacket(reader);
			}
		});
	}

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
        super(ConnectionType.CLIENT, TYPE);

        if (teamNumber < 0) {
        	throw new IllegalArgumentException(
        			"DAMCON team number can't be less than 0"
        	);
        }

        mTeamNumber = teamNumber;
        mCoord = coord;
    }

    private EngSendDamconPacket(PacketReader reader) {
        super(ConnectionType.CLIENT, TYPE);
        reader.skip(4); // subtype
        mTeamNumber = reader.readInt();
        mCoord = GridCoord.getInstance(reader.readInt(), reader.readInt(), reader.readInt());
    }

    public int getTeamNumber() {
    	return mTeamNumber;
    }

    public GridCoord getDestination() {
    	return mCoord;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer	.writeInt(SUBTYPE)
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