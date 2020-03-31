package com.walkertribe.ian.protocol.core.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.world.ArtemisObject;
import com.walkertribe.ian.world.ArtemisPlayer;

/**
 * <p>
 * A packet which contains updates for world objects.
 * </p>
 * <p>
 * While the ObjectUpdatePacket supports payloads with heterogeneous object type, in practice the
 * Artemis server only sends packets with homogeneous types; in other words, object updates of
 * different types are sent in separate packets. Initial tests seem to indicate that the stock
 * Artemis client can handle heterogeneous update types in a single packet, but this is not yet
 * considered 100% confirmed. If you wish to ensure that you completely emulate the behavior of an
 * Artemis server, send separate packets for separate object types. You can do this easily by
 * invoking segregate().
 * </p>
 * <p>
 * The ArtemisPlayer object is actually expressed in four different update types, depending on the
 * data that it contains:
 * </p>
 * <ul>
 * <li>
 *   <code>ObjectType.PLAYER</code>: Data not included in the other three types
 * </li>
 * <li>
 *   <code>ObjectType.WEAPONS_CONSOLE</code>: Data about ordnance counts and tube status 
 * </li>
 * <li>
 *   <code>ObjectType.ENGINEERING_CONSOLE</code>: Data about system status (energy, heat, coolant)
 * </li>
 * <li>
 *   <code>ObjectType.UPGRADES</code>: Data about upgrade status
 * </li>
 * </ul>
 * <p>
 * Under most circumstances, every object stored in the ObjectUpdatePacket object will produce one
 * update in the packet itself when it's written out. But ArtemisPlayer objects will produce one
 * update <strong>per update type</strong> listed above for each type that has any data in the
 * object. You can determine whether an ArtemisPlayer object contains data for a particular
 * ObjectType by calling ArtemisPlayer.hasDataForType(ObjectType). The
 * segregate() method takes this into consideration.
 * </p>
 *
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.OBJECT_BIT_STREAM)
public class ObjectUpdatePacket extends BaseArtemisPacket {
	private static final Map<ObjectType, ObjectParser> PARSERS;
	private static final ObjectType[] PLAYER_OBJECT_TYPES = {
			ObjectType.PLAYER_SHIP,
			ObjectType.WEAPONS_CONSOLE,
			ObjectType.ENGINEERING_CONSOLE,
			ObjectType.UPGRADES
	};

	static {
		PARSERS = new HashMap<ObjectType, ObjectParser>();
		PARSERS.put(ObjectType.PLAYER_SHIP, new PlayerShipParser());
		PARSERS.put(ObjectType.WEAPONS_CONSOLE, new WeapParser());
		PARSERS.put(ObjectType.ENGINEERING_CONSOLE, new EngParser());
		PARSERS.put(ObjectType.UPGRADES, new UpgradesParser());
		PARSERS.put(ObjectType.BASE, new BaseParser());
		PARSERS.put(ObjectType.NPC_SHIP, new NpcShipParser());
		PARSERS.put(ObjectType.MINE, new MineParser());
		PARSERS.put(ObjectType.ANOMALY, new AnomalyParser());
		PARSERS.put(ObjectType.NEBULA, new NebulaParser());
		PARSERS.put(ObjectType.TORPEDO, new TorpedoParser());
		PARSERS.put(ObjectType.BLACK_HOLE, new BlackHoleParser());
		PARSERS.put(ObjectType.ASTEROID, new AsteroidParser());
		PARSERS.put(ObjectType.GENERIC_MESH, new GenericMeshParser());
		PARSERS.put(ObjectType.CREATURE, new CreatureParser());
		PARSERS.put(ObjectType.DRONE, new DroneParser());
	}

	/**
	 * Accepts a Collection of ArtemisObjects and produces a list of ObjectUpdatePackets, with each
	 * packet containing updates of only one ObjectType. This emulates the behavior of the Artemis
	 * server, which does not transmit ObjectUpdatePackets with heterogeneous ObjectTypes. However,
	 * as initial tests seem to indicate that the stock Artemis client handles heterogeneous
	 * ObjectTypes in ObjectUpdatePackets without issue, it may not be necessary to do this unless
	 * you wish to ensure that your code exactly emulates Artemis server behavior.
	 */
	public static List<ObjectUpdatePacket> segregate(Collection<ArtemisObject> objs) {
		// Too bad we're stuck in Java 6 land to support old Android devices, because I would
		// totally use Collection streams for this!
		// TODO Change to Collection stream API when we can use Java 8.
		Map<ObjectType, List<ArtemisObject>> map = new HashMap<ObjectType, List<ArtemisObject>>();

		for (ArtemisObject obj : objs) {
			ObjectType type = obj.getType();

			if (type == ObjectType.PLAYER_SHIP) {
				ArtemisPlayer player = (ArtemisPlayer) obj;
				Map<ObjectType, ArtemisPlayer> players = player.split();

				for (Map.Entry<ObjectType, ArtemisPlayer> entry : players.entrySet()) {
					updateSegregationMap(map, entry.getKey(), entry.getValue());
				}
			} else {
				updateSegregationMap(map, type, obj);
			}
		}

		List<ObjectUpdatePacket> list = new ArrayList<ObjectUpdatePacket>();

		for (List<ArtemisObject> typeObjs : map.values()) {
			ObjectUpdatePacket pkt = new ObjectUpdatePacket();
			pkt.addObjects(typeObjs);
			list.add(pkt);
		}

		return list;
	}

	/**
	 * Internal method used by segregate() to handle uninitialized map entries.
	 */
	private static void updateSegregationMap(Map<ObjectType, List<ArtemisObject>> map, ObjectType type,
			ArtemisObject obj) {
		List<ArtemisObject> list = map.get(type);

		if (list == null) {
			list = new ArrayList<ArtemisObject>();
			map.put(type, list);
		}

		list.add(obj);
	}

	private List<ArtemisObject> objects = new LinkedList<ArtemisObject>();

	public ObjectUpdatePacket() {
	}

	public ObjectUpdatePacket(PacketReader reader) {
		do {
			ObjectType objectType = ObjectType.fromId(reader.peekByte());

			if (objectType == null) {
				break;
			}

			ObjectParser parser = PARSERS.get(objectType);
			objects.add(parser.parse(reader));
		} while (true);

		reader.skip(4);
	}

	/**
	 * Add a new object to be updated.
	 */
	public void addObject(ArtemisObject obj) {
		objects.add(obj);
	}

	/**
	 * Add a Collection of objects to be updated.
	 */
	public void addObjects(Collection<ArtemisObject> objs) {
		objects.addAll(objs);
	}

	/**
	 * Returns the updated objects.
	 */
	public List<ArtemisObject> getObjects() {
		return new LinkedList<ArtemisObject>(objects);
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		for (ArtemisObject obj : objects) {
			ArtemisPlayer player = obj instanceof ArtemisPlayer ? (ArtemisPlayer) obj : null;
			ObjectType type = player != null ? player.getDeclaredType() : obj.getType();

			if (type != null) {
				// We have a specific type for this object; use it
				writePayloadForObject(writer, obj, type);
			} else {
				// This is an ArtemisPlayer that doesn't have a specifically declared ObjectType;
				// detect what ObjectTypes have data in this object and write objects for them.
				// This means we may write more than one object.
				boolean foundData = false;
				player = (ArtemisPlayer) obj;

				for (ObjectType playerType : PLAYER_OBJECT_TYPES) {
					if (player.hasDataForType(playerType)) {
						foundData = true;
						writePayloadForObject(writer, player, playerType);
					}
				}

				if (!foundData) {
					// write an empty packet
					writePayloadForObject(writer, player, ObjectType.PLAYER_SHIP);
				}
			}
		}

		writer.writeInt(0);
	}

	/**
	 * Writes the payload data for an individual object, using the specified ObjectType.
	 */
	private static void writePayloadForObject(PacketWriter writer, ArtemisObject obj, ObjectType type) {
		ObjectParser parser = PARSERS.get(type);
		writer.startObject(obj, type, parser.getBitCount());
		parser.write(obj, writer);
		writer.endObject();
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		for (ArtemisObject obj : objects) {
			b.append("\nObject #").append(obj.getId()).append(obj);
		}
	}
}