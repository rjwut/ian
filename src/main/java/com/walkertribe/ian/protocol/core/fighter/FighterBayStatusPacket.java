package com.walkertribe.ian.protocol.core.fighter;

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
 * Updates the current status of the fighter bays.
 * @author rjwut
 */
public class FighterBayStatusPacket extends BaseArtemisPacket implements Iterable<FighterBayStatusPacket.Bay> {
    private static final PacketType TYPE = CorePacketType.CARRIER_RECORD;

    public static class Bay {
    	private int id;
    	private String name;
    	private String className;
    	private int refitTime;

    	public Bay(int id, String name, String className, int refitTime) {
    		if (id == 0) {
    			throw new IllegalArgumentException("Fighter bay can't have an ID of 0");
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

		public String getName() {
			return name;
		}

		public String getClassName() {
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
				return FighterBayStatusPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new FighterBayStatusPacket(reader);
			}
		});
	}

    private List<Bay> bays = new ArrayList<Bay>();

    private FighterBayStatusPacket(PacketReader reader) {
        super(ConnectionType.SERVER, TYPE);

        while (true) {
        	int id = reader.readInt();

        	if (id == 0) {
        		break;
        	}

        	String name = reader.readString();
        	String className = reader.readString();
        	int refitTime = reader.readInt();
        	bays.add(new Bay(id, name, className, refitTime));
        }
    }

    public FighterBayStatusPacket() {
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
	public Iterator<FighterBayStatusPacket.Bay> iterator() {
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
