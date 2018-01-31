package com.walkertribe.ian.protocol.core.world;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.PacketType;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.world.ArtemisObject;

public class ObjectUpdatePacket extends BaseArtemisPacket {
	public static final PacketType TYPE = CorePacketType.OBJECT_BIT_STREAM;

	private static final Map<ObjectType, ObjectParser> PARSERS;

	static {
		PARSERS = new HashMap<ObjectType, ObjectParser>();
		PARSERS.put(ObjectType.PLAYER_SHIP, new PlayerShipParser());
		PARSERS.put(ObjectType.WEAPONS_CONSOLE, new WeapParser());
		PARSERS.put(ObjectType.ENGINEERING_CONSOLE, new EngParser());
		PARSERS.put(ObjectType.UPGRADES, new UpgradesParser());
		PARSERS.put(ObjectType.BASE, new BaseParser());
		PARSERS.put(ObjectType.NPC_SHIP, new NpcShipParser());
		PARSERS.put(ObjectType.MINE, new OtherParser(ObjectType.MINE));
		PARSERS.put(ObjectType.ANOMALY, new AnomalyParser());
		PARSERS.put(ObjectType.NEBULA, new NebulaParser());
		PARSERS.put(ObjectType.TORPEDO, new TorpedoParser());
		PARSERS.put(ObjectType.BLACK_HOLE, new OtherParser(ObjectType.BLACK_HOLE));
		PARSERS.put(ObjectType.ASTEROID, new OtherParser(ObjectType.ASTEROID));
		PARSERS.put(ObjectType.GENERIC_MESH, new GenericMeshParser());
		PARSERS.put(ObjectType.CREATURE, new CreatureParser());
		PARSERS.put(ObjectType.DRONE, new DroneParser());
	}

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return ObjectUpdatePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new ObjectUpdatePacket(reader);
			}
		});
	}

	private ObjectType objectType;
	private ObjectParser parser;
	private List<ArtemisObject> objects = new LinkedList<ArtemisObject>();

	private ObjectUpdatePacket(PacketReader reader) {
		this(ObjectType.fromId(reader.peekByte()));

		if (parser == null) {
			return;
		}

		do {
			ArtemisObject obj = parser.parse(reader);

			if (obj == null) {
				break;
			}

			objects.add(obj);
		} while (true);

		reader.skip(4);
	}

	public ObjectUpdatePacket(ObjectType type) {
		super(ConnectionType.SERVER, TYPE);
		objectType = type;
		parser = PARSERS.get(objectType);
	}

	public void addObject(ArtemisObject obj) {
		if (!objectType.isCompatible(obj)) {
			throw new IllegalArgumentException("Object is of type " +
					obj.getType() + "; expected " + objectType);
		}

		objects.add(obj);
	}

	public List<ArtemisObject> getObjects() {
		return new LinkedList<ArtemisObject>(objects);
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		for (ArtemisObject obj : objects) {
			writer.startObject(obj, objectType, parser.getBitCount());
			parser.write(obj, writer);
			writer.endObject();
		}

		writer.writeInt(0);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		for (ArtemisObject obj : objects) {
			parser.appendDetail(obj, b);
		}
	}
}