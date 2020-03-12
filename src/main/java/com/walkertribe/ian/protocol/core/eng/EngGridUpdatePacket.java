package com.walkertribe.ian.protocol.core.eng;

import java.util.ArrayList;
import java.util.List;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.GridCoord;

/**
 * Updates damage to the various system grids on the ship, as well as DAMCON
 * team status/location.
 * @author dhleong
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SHIP_SYSTEM_SYNC)
public class EngGridUpdatePacket extends BaseArtemisPacket {
    private static final byte END_GRID_MARKER = (byte) 0xff;
    private static final byte END_DAMCON_MARKER = (byte) 0xfe;
    private static final byte TEAM_NUMBER_OFFSET = 0x0a;

    private boolean mRequested;
    private List<NodeDamage> mDamage = new ArrayList<>();
    private List<DamconTeam> mDamconUpdates = new ArrayList<>();

    /**
     * Creates a new EngGridUpdatePacket with no updates. Use the
     * addDamageUpdate() and addDamconUpdate() methods to add update information
     * to this packet. The requested parameter indicates whether this packet is
     * being sent in response to a {@link EngRequestGridUpdatePacket} packet.
     */
    public EngGridUpdatePacket(boolean requested) {
        mRequested = requested;
    }

    public EngGridUpdatePacket(PacketReader reader) {
        mRequested = reader.readByte() == 1;

        while (reader.peekByte() != END_GRID_MARKER) {
            GridCoord coord = GridCoord.get(reader.readByte(), reader.readByte(), reader.readByte());
            mDamage.add(new NodeDamage(coord, reader.readFloat()));
        }

        reader.skip(1); // read the 0xff byte

        while (reader.peekByte() != END_DAMCON_MARKER) {
            byte teamNumber = (byte) (reader.readByte() - TEAM_NUMBER_OFFSET);
            int xGoal = reader.readInt();
            int x = reader.readInt();
            int yGoal = reader.readInt();
            int y = reader.readInt();
            int zGoal = reader.readInt();
            int z = reader.readInt();
            float progress = reader.readFloat();
            int members = reader.readInt();
            addDamconUpdate(teamNumber, members, xGoal, yGoal, zGoal, x, y, z, progress);
        }

        reader.skip(1); // read the 0xfe byte
    }

    /**
     * Returns true if this update was requested by the client.
     */
    public boolean isRequested() {
    	return mRequested;
    }

    /**
     * Adds a damage update to this packet.
     */
    public void addDamageUpdate(int x, int y, int z, float damage) {
    	mDamage.add(new NodeDamage(GridCoord.get(x, y, z), damage));
    }

    /**
     * Adds a DAMCON team update to this packet.
     */
    public void addDamconUpdate(byte teamNumber, int members, int xGoal,
            int yGoal, int zGoal, int x, int y, int z, float progress) {
        mDamconUpdates.add(new DamconTeam(
                teamNumber,
                GridCoord.get(x, y, z),
                GridCoord.get(xGoal, yGoal, zGoal),
                progress, members));
    }

    /**
     * Returns a List of DamageEntry objects that describe the damage data
     * encoded in this packet.
     */
    public List<NodeDamage> getDamage() {
        return mDamage;
    }

    /**
     * Returns a List of DamconTeam objects that provide the DAMCON team
     * updates encoded in this packet.
     */
    public List<DamconTeam> getDamcons() {
        return mDamconUpdates;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeByte((byte) (mRequested ? 1 : 0));

		for (NodeDamage damageEntry : mDamage) {
			GridCoord coord = damageEntry.getCoord();
			writer	.writeByte((byte) coord.x())
					.writeByte((byte) coord.y())
					.writeByte((byte) coord.z())
					.writeFloat(damageEntry.getDamage());
		}

		writer.writeByte(END_GRID_MARKER);

		for (DamconTeam update : mDamconUpdates) {
			writer	.writeByte((byte) (update.getId() + TEAM_NUMBER_OFFSET))
					.writeInt(update.getGoal().x())
					.writeInt(update.getLocation().x())
					.writeInt(update.getGoal().y())
					.writeInt(update.getLocation().y())
					.writeInt(update.getGoal().z())
					.writeInt(update.getLocation().z())
					.writeFloat(update.getProgress())
					.writeInt(update.getMembers());
		}

		writer.writeByte(END_DAMCON_MARKER);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("\nDamage updates:");

		if (mDamage.isEmpty()) {
			b.append("\n\tnone");
		} else {
			for (NodeDamage damageEntry : mDamage) {
				b.append("\n\t").append(damageEntry);
			}
		}

		b.append("\nDAMCON status updates:");

		if (mDamconUpdates.isEmpty()) {
			b.append("\n\tnone");
		} else {
			for (DamconTeam status : mDamconUpdates) {
				b.append("\n\t").append(status);
			}
		}
	}
}