package com.walkertribe.ian.protocol.core.singleseat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

/**
 * Updates the current status of the single-seat craft bays.
 * @author rjwut
 */
public class BayStatusPacket extends BaseArtemisPacket implements Iterable<BayStatusPacket.Bay> {
    private static final PacketType TYPE = CorePacketType.CARRIER_RECORD;

    public static class Bay {
    	private int id;
    	private CharSequence name;
    	private CharSequence className;
    	private int refitTime;

    	public Bay(int id, CharSequence name, CharSequence className, int refitTime) {
    		if (id == 0) {
    			throw new IllegalArgumentException("Bay can't have an ID of 0");
    		}

    		if (name == null || name.length() == 0) {
    			throw new IllegalArgumentException("You must provide a name");
    		}

    		if (className == null || className.length() == 0) {
    			throw new IllegalArgumentException("You must provide a class name");
    		}

    		if (refitTime < 0) {
    			throw new IllegalArgumentException("Cannot have a negative refit time");
    		}

    		this.id = id;
    		this.name = name;
    		this.className = className;
    		this.refitTime = refitTime;
    	}

		public int getId() {
			return id;
		}

		public CharSequence getName() {
			return name;
		}

		public CharSequence getClassName() {
			return className;
		}

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
			return "#" + id + ": " + name + " (" + className + "): " + refitTime;
		}
    }

    public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return BayStatusPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new BayStatusPacket(reader);
			}
		});
	}

    private List<Bay> bays = new ArrayList<Bay>();

    private BayStatusPacket(PacketReader reader) {
        super(ConnectionType.SERVER, TYPE);

        while (true) {
        	int id = reader.readInt();

        	if (id == 0) {
        		break;
        	}

        	CharSequence name = reader.readString();
        	CharSequence className = reader.readString();
        	int refitTime = reader.readInt();
        	bays.add(new Bay(id, name, className, refitTime));
        }
    }

    public BayStatusPacket() {
    	super(ConnectionType.SERVER, TYPE);
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

    @Override
	public Iterator<BayStatusPacket.Bay> iterator() {
		return bays.iterator();
	}

    @Override
	protected void writePayload(PacketWriter writer) {
    	for (Bay bay : bays) {
    		writer
    			.writeInt(bay.id)
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
