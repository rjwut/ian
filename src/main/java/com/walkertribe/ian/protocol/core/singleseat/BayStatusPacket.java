package com.walkertribe.ian.protocol.core.singleseat;

import java.util.ArrayList;
import java.util.List;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.util.Util;

/**
 * Updates the current status of the single-seat craft bays.
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.CARRIER_RECORD)
public class BayStatusPacket extends BaseArtemisPacket {
    public static class Bay implements Comparable<Bay> {
    	private int id;
    	private int number;
    	private CharSequence name;
    	private CharSequence className;
    	private int refitTime;

    	public Bay(int id, int bayNumber, CharSequence name, CharSequence className, int refitTime) {
    		if (id == 0) {
    			throw new IllegalArgumentException("Bay can't have an ID of 0");
    		}

    		if (Util.isBlank(name)) {
    			throw new IllegalArgumentException("You must provide a name");
    		}

    		if (Util.isBlank(className)) {
    			throw new IllegalArgumentException("You must provide a class name");
    		}

    		if (refitTime < 0) {
    			throw new IllegalArgumentException("Cannot have a negative refit time");
    		}

    		this.id = id;
    		this.number = bayNumber;
    		this.name = name;
    		this.className = className;
    		this.refitTime = refitTime;
    	}

    	/**
    	 * Returns the craft's ID.
    	 */
		public int getId() {
			return id;
		}

		/**
		 * Returns the number of this bay.
		 */
		public int getNumber() {
			return number;
		}

		/**
		 * Returns the craft's name.
		 */
		public CharSequence getName() {
			return name;
		}

		/**
		 * Returns the of the craft's class.
		 */
		public CharSequence getClassName() {
			return className;
		}

		/**
		 * The amount of time (in seconds) remaining before this craft can be
		 * relaunched.
		 */
		public int getRefitTime() {
			return refitTime;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Bay)) {
				return false;
			}

			return id == ((Bay) obj).id;
		}

		@Override
		public int hashCode() {
			return id;
		}

		@Override
		public String toString() {
			return "#" + id + " in bay " + number + ": " + name + " (" + className + "): " + refitTime;
		}

		@Override
		public int compareTo(Bay other) {
			return number - other.number;
		}
    }

    private List<Bay> bays = new ArrayList<Bay>();

    public BayStatusPacket() {
    }

    public BayStatusPacket(PacketReader reader) {
        while (true) {
        	int id = reader.readInt();

        	if (id == 0) {
        		break;
        	}

        	int bayNumber = reader.readInt();
        	CharSequence name = reader.readString();
        	CharSequence className = reader.readString();
        	int refitTime = reader.readInt();
        	bays.add(new Bay(id, bayNumber, name, className, refitTime));
        }
    }

    /**
     * Adds the given Bay to the list of Bays.
     */
    public void addBay(Bay bay) {
    	bays.add(bay);
    }

    /**
     * Returns the number of Bays described in this packet.
     */
    public int getBayCount() {
    	return bays.size();
    }

    /**
     * Returns the List of Bays.
     */
    public List<Bay> getBays() {
    	return bays;
    }

    @Override
	protected void writePayload(PacketWriter writer) {
    	for (Bay bay : bays) {
    		writer
    			.writeInt(bay.id)
    			.writeInt(bay.number)
    			.writeString(bay.name)
    			.writeString(bay.className)
    			.writeInt(bay.refitTime);
    	}

    	writer.writeInt(0);
    }

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		for (Bay bay : bays) {
			b.append("\n\t").append(bay);
		}
	}
}
