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
    private static final int TEAM_NUMBER_OFFSET = 0x0a;
    private static final float PROGRESS_EPSILON = 0.001f;

    private boolean mRequested;
    private List<GridDamage> mDamage = new ArrayList<GridDamage>();
    private List<DamconStatus> mDamconUpdates = new ArrayList<DamconStatus>();

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
            GridCoord coord = GridCoord.getInstance(
                    reader.readByte(), 
                    reader.readByte(), 
                    reader.readByte()
            );
            float damage = reader.readFloat();
            mDamage.add(new GridDamage(coord, damage));
        }

        reader.skip(1); // read the 0xff byte

        while (reader.peekByte() != END_DAMCON_MARKER) {
            byte teamIndicator = reader.readByte();
            int teamNumber = teamIndicator - TEAM_NUMBER_OFFSET;
            int xGoal = reader.readInt();
            int x = reader.readInt();
            int yGoal = reader.readInt();
            int y = reader.readInt();
            int zGoal = reader.readInt();
            int z = reader.readInt();
            float progress = reader.readFloat();
            int members = reader.readInt();
            addDamconUpdate(teamNumber, members, xGoal, yGoal, zGoal, x, y, z,
            		progress);
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
    	mDamage.add(new GridDamage(GridCoord.getInstance(x, y, z), damage));
    }

    /**
     * Adds a DAMCON team update to this packet.
     */
    public void addDamconUpdate(int teamNumber, int members, int xGoal,
    		int yGoal, int zGoal, int x, int y, int z, float progress) {
    	mDamconUpdates.add(new DamconStatus(teamNumber, members, xGoal, yGoal,
    			zGoal, x, y, z, progress));
    }

    /**
     * Returns a List of GridDamage objects that describe the damage data
     * encoded in this packet.
     */
    public List<GridDamage> getDamage() {
        return mDamage;
    }

    /**
     * Returns a List of DamconStatus objects that provide the DAMCON team
     * updates encoded in this packet.
     */
    public List<DamconStatus> getDamcons() {
        return mDamconUpdates;
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeByte((byte) (mRequested ? 1 : 0));

		for (GridDamage damage : mDamage) {
			GridCoord coord = damage.coord;
			writer	.writeByte((byte) coord.getX())
					.writeByte((byte) coord.getY())
					.writeByte((byte) coord.getZ())
					.writeFloat(damage.damage);
		}

		writer.writeByte(END_GRID_MARKER);

		for (DamconStatus update : mDamconUpdates) {
			writer	.writeByte((byte) (update.teamNumber + TEAM_NUMBER_OFFSET))
					.writeInt(update.goal.getX())
					.writeInt(update.pos.getX())
					.writeInt(update.goal.getY())
					.writeInt(update.pos.getY())
					.writeInt(update.goal.getZ())
					.writeInt(update.pos.getZ())
					.writeFloat(update.progress)
					.writeInt(update.members);
		}

		writer.writeByte(END_DAMCON_MARKER);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append("\nDamage updates:");

		if (mDamage.isEmpty()) {
			b.append("\n\tnone");
		} else {
			for (GridDamage damage : mDamage) {
				b.append("\n\t").append(damage);
			}
		}

		b.append("\nDAMCON status updates:");

		if (mDamconUpdates.isEmpty()) {
			b.append("\n\tnone");
		} else {
			for (DamconStatus status : mDamconUpdates) {
				b.append("\n\t").append(status);
			}
		}
	}


	/**
	 * Updates the level of damage to a node in the system grid.
     * @author dhleong
	 */
    public static final class GridDamage {
        private final GridCoord coord;
        private final float damage;

        GridDamage(GridCoord coord, float damage) {
            this.coord = coord;
            this.damage = damage;
        }

        public GridCoord getCoord() {
        	return coord;
        }

        public float getDamage() {
        	return damage;
        }

        @Override
        public boolean equals(Object other) {
        	if (this == other) {
        		return true;
        	}

        	if (!(other instanceof GridDamage)) {
        		return false;
        	}

            GridDamage cast = (GridDamage) other;
            return coord.equals(cast.coord);
        }

		@Override
		public int hashCode() {
			return coord.hashCode();
		}

		@Override
		public String toString() {
			return coord + ": " + damage;
		}
    }
    
    /**
     * Updates the status of a DAMCON team.
     * @author dhleong
     */
    public static final class DamconStatus {
        private int teamNumber, members;
        private GridCoord goal;
        private GridCoord pos;
        private float progress;

        DamconStatus(int teamNumber, int members, int xGoal,
                int yGoal, int zGoal, int x, int y, int z, float progress) {
            this.teamNumber = teamNumber;
            this.members = members;
            goal = GridCoord.getInstance(xGoal, yGoal, zGoal);
            pos = GridCoord.getInstance(x, y, z);
            this.progress = progress;
        }

        /**
         * The number assigned to this DAMCON team.
         */
        public int getTeamNumber() {
            return teamNumber;
        }

        /**
         * The number of people in this DAMCON team that are still alive.
         */
        public int getMembers() {
            return members;
        }

        /**
         * The coordinates of the DAMCON team's current location.
         */
        public GridCoord getPosition() {
        	return pos;
        }

        /**
         * The coordinates of the DAMCON team's goal.
         */
        public GridCoord getGoal() {
        	return goal;
        }

        /**
         * The DAMCON team's progress towards their destination.
         */
        public float getProgress() {
            return progress;
        }

        public void updateFrom(DamconStatus other) {
            this.members = other.members;
            
            if (other.progress < PROGRESS_EPSILON && progress > 0) {
            	this.pos = goal;
            } else {
            	this.pos = other.pos;
            }

            this.goal = other.goal;
            this.progress = other.progress;
        }
     
        @Override
        public String toString() {
        	StringBuilder b = new StringBuilder();
        	b.append("Team #").append(teamNumber)
        	.append(" (").append(members).append("): ")
        	.append(pos)
        	.append(" => ")
        	.append(goal)
        	.append(" (").append(progress).append(")");
        	return b.toString();
        }
    }
}