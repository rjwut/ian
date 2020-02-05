package com.walkertribe.ian.vesseldata;

import java.util.HashMap;
import java.util.Map;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.OrdnanceType;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX ContentHandler implementation that can convert vesselData.xml to a
 * VesselData object.
 */
public class SAXVesselDataHandler extends DefaultHandler {
	/**
	 * Interface for classes which handle a specific element type in
	 * vesselData.xml.
	 */
	private interface Parser {
		/**
		 * Invoked when the corresponding element is encountered by the SAX
		 * parser.
		 */
		void parse(Attributes attrs);
	}

	private VesselData vesselData;
	private Context ctx;
	private Map<String, Parser> parsers = new HashMap<String, Parser>();
	private Faction faction;
	private Vessel vessel;

	public SAXVesselDataHandler(Context ctx) {
		this.ctx = ctx;
		parsers.put("art", new ArtParser());
		parsers.put("beam_port", new BeamPortParser());
		parsers.put("carrierload", new CarrierLoadParser());
		parsers.put("drone_port", new DronePortParser());
		parsers.put("engine_port", new EnginePortParser());
		parsers.put("fleet_ai", new FleetAiParser());
		parsers.put("hullRace", new HullRaceParser());
		parsers.put("impulse_point", new ImpulsePointParser());
		parsers.put("internal_data", new InternalDataParser());
		parsers.put("long_desc", new LongDescParser());
		parsers.put("maneuver_point", new ManeuverPointParser());
		parsers.put("performance", new PerformanceParser());
		parsers.put("production", new ProductionParser());
		parsers.put("shields", new ShieldsParser());
		parsers.put("taunt", new TauntParser());
		parsers.put("torpedo_station_port", new TorpedoStationPortParser());
		parsers.put("torpedo_storage", new TorpedoStorageParser());
		parsers.put("torpedo_tube", new TorpedoTubeParser());
		parsers.put("vessel", new VesselParser());
		parsers.put("vessel_data", new VesselDataParser());
	}

	public VesselData getVesselData() {
		return vesselData;
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attrs) throws SAXException {
		Parser parser = parsers.get(qName);

		if (parser != null) {
			parser.parse(attrs);
		} else {
			System.err.println("Unknown element: " + qName);
		}
	}

	/**
	 * Converts an XML attribute to a float value, or 0.0f if the attribute is
	 * not found.
	 */
	private static float parseFloat(Attributes attrs, String name) {
		String value = attrs.getValue(name);
		return value != null ? Float.parseFloat(value) : 0.0f;
	}

	/**
	 * Converts an XML attribute to an int value; or 0 if the attribute is not
	 * found.
	 */
	private static int parseInt(Attributes attrs, String name) {
		String value = attrs.getValue(name);
		return value != null ? Integer.parseInt(value) : 0;
	}

	/**
	 * Extracts the x, y, and z attributes and writes them to the given
	 * VesselPoint object.
	 */
	private static void parseVesselPoint(VesselPoint point, Attributes attrs) {
		point.x = Float.parseFloat(attrs.getValue("x"));
		point.y = Float.parseFloat(attrs.getValue("y"));
		point.z = Float.parseFloat(attrs.getValue("z"));
	}

	/**
	 * Extracts weapon port attributes and writes them to the given WeaponPort
	 * object.
	 */
	public static void parseWeaponPort(WeaponPort port, Attributes attrs) {
		parseVesselPoint(port, attrs);
		port.damage = parseFloat(attrs, "damage");
		port.playerDamage = parseFloat(attrs, "playerdamage");
		port.cycleTime = parseFloat(attrs, "cycletime");
		port.range = parseInt(attrs, "range");
	}

	/**
	 * Parser for <art> elements.
	 */
	private class ArtParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			Art art = new Art(
					attrs.getValue("meshfile"),
					attrs.getValue("diffuseFile"),
					attrs.getValue("glowFile"),
					attrs.getValue("specularFile")
			);
			vessel.artList.add(art);
			vessel.scale = parseFloat(attrs, "scale");
			vessel.pushRadius = parseInt(attrs, "pushRadius");
		}
	}

	/**
	 * Parser for <beam_port> elements.
	 */
	private class BeamPortParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			BeamPort port = new BeamPort();
			parseWeaponPort(port, attrs);
			port.arcWidth = parseFloat(attrs, "arcwidth");
			vessel.beamPorts.add(port);
		}
	}

	/**
	 * Parser for <carrierload> elements.
	 */
	private class CarrierLoadParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			vessel.bayCount = parseInt(attrs, "baycount");
		    vessel.fighterCount = parseInt(attrs, "fighter");
		    vessel.bomberCount = parseInt(attrs, "bomber");
		}
	}

	/**
	 * Parser for <drone_port> elements.
	 */
	private class DronePortParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			WeaponPort port = new WeaponPort();
			parseWeaponPort(port, attrs);
			vessel.dronePorts.add(port);
		}
	}

	/**
	 * Parser for <engine_port> elements.
	 */
	private class EnginePortParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			VesselPoint point = new VesselPoint();
			parseVesselPoint(point, attrs);
			vessel.enginePorts.add(point);
		}
	}

	/**
	 * Parser for <fleet_ai> elements.
	 */
	private class FleetAiParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			vessel.fleetAiCommonality = parseInt(attrs, "commonality");
		}
	}

	/**
	 * Parser for <hullRace> elements.
	 */
	private class HullRaceParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			int id = parseInt(attrs, "ID");
			faction = new Faction(
					id,
					attrs.getValue("name"),
					attrs.getValue("keys")
			);

			while (vesselData.factions.size() <= id) {
				vesselData.factions.add(null);
			}

			vesselData.factions.set(id, faction);
		}
	}

	/**
	 * Parser for <impulse_point> elements.
	 */
	private class ImpulsePointParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			VesselPoint point = new VesselPoint();
			parseVesselPoint(point, attrs);
			vessel.impulsePoints.add(point);
		}
	}

	/**
	 * Parser for <internal_data> elements.
	 */
	private class InternalDataParser implements Parser{
		@Override
		public void parse(Attributes attrs) {
			vessel.internalDataFile = attrs.getValue("file");
		}
	}

	/**
	 * Parser for <long_desc> elements.
	 */
	private class LongDescParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			vessel.description = attrs.getValue("text").replaceAll("\\^", "\n");
		}
	}

	/**
	 * Parser for <maneuver_point> elements.
	 */
	private class ManeuverPointParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			VesselPoint point = new VesselPoint();
			parseVesselPoint(point, attrs);
			vessel.maneuverPoints.add(point);
		}
	}

	/**
	 * Parser for <performance> elements.
	 */
	private class PerformanceParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			vessel.turnRate = parseFloat(attrs, "turnrate");
			vessel.topSpeed = parseFloat(attrs, "topspeed");
			vessel.shipEfficiency = parseFloat(attrs, "shipefficiency");
			vessel.warpEfficiency = parseFloat(attrs, "warpefficiency");
			vessel.jumpEfficiency = parseFloat(attrs, "jumpefficiency");
		}
	}

	/**
	 * Parser for <production> elements.
	 */
	private class ProductionParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			vessel.productionCoeff = parseFloat(attrs, "coeff");
		}
	}

	/**
	 * Parser for <shields> elements.
	 */
	private class ShieldsParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			vessel.foreShields = parseInt(attrs, "front");
			vessel.aftShields = parseInt(attrs, "back");
			String playerShields = attrs.getValue("player");

			if (playerShields != null) {
				vessel.playerShields = Integer.parseInt(playerShields);
			}
		}
	}

	/**
	 * Parser for <taunt> elements.
	 */
	private class TauntParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			faction.taunts.add(new Taunt(
					attrs.getValue("immunity"),
					attrs.getValue("text")
			));
		}
	}

	/**
	 * Parser for <torpedo_station_port> elements.
	 */
	private class TorpedoStationPortParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			WeaponPort port = new WeaponPort();
			parseWeaponPort(port, attrs);
			vessel.baseTorpedoPorts.add(port);
		}
	}

	/**
	 * Parser for <torpedo_storage> elements.
	 */
	private class TorpedoStorageParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			String code = attrs.getValue("type");
			OrdnanceType type = OrdnanceType.fromCode(code);

			if (type == null) {
				throw new IllegalArgumentException("Invalid ordnance type code: " + code);
			}

			Integer amount = Integer.valueOf(attrs.getValue("amount"));
			vessel.torpedoStorage.put(type, amount);
			vessel.totalTorpedoStorage += amount.intValue();
		}
	}

	/**
	 * Parser for <torpedo_tube> elements.
	 */
	private class TorpedoTubeParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			VesselPoint point = new VesselPoint();
			parseVesselPoint(point, attrs);
			vessel.torpedoTubes.add(point);
		}
	}

	/**
	 * Parser for <vessel> elements.
	 */
	private class VesselParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			Integer id = Integer.valueOf(attrs.getValue("uniqueID"));
			vessel = new Vessel(
					ctx,
					id.intValue(),
					parseInt(attrs, "side"),
					attrs.getValue("classname"),
					attrs.getValue("broadType")
			);
			vesselData.vessels.put(id, vessel);
		}
	}

	/**
	 * Parser for the main <vessel_data> element.
	 */
	private class VesselDataParser implements Parser {
		@Override
		public void parse(Attributes attrs) {
			vesselData = new VesselData(ctx, attrs.getValue("version"));
		}
	}
}