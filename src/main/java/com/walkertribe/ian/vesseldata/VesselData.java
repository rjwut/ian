package com.walkertribe.ian.vesseldata;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.walkertribe.ian.enums.CreatureType;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.util.Version;

/**
 * Contains all the information extracted from the vesselData.xml file.
 * @author rjwut
 */
public class VesselData {
	public static PathResolver pathResolver;
	private static VesselData instance;
	private static Map<String, Model> models;

	/**
	 * Sets the location of the Artemis installation directory. This will delete
	 * the existing VesselData instance; another will be generated based on this
	 * new installation path the next time it is required.
	 */
	public static void setArtemisInstallPath(File artemisInstallPath) {
		if (artemisInstallPath == null) {
			throw new IllegalArgumentException("The Artemis install path is required");
		}

		if (!artemisInstallPath.exists()) {
			throw new IllegalArgumentException("Path does not exist");
		}

		if (!artemisInstallPath.isDirectory()) {
			throw new IllegalArgumentException("Path is not a directory");
		}

		pathResolver = new FilePathResolver(artemisInstallPath);
		models = new HashMap<String, Model>();
		instance = null;
	}

	/**
	 * Returns the VesselData instance. If vesselData.xml has not yet been
	 * parsed when get() is called, it will be parsed at that time. Note that
	 * you must invoke setArtemisInstallPath() first, before calling this
	 * method.
	 */
	public static VesselData get() {
		if (pathResolver == null) {
			throw new IllegalStateException("Must invoke setArtemisInstallPath() first");
		}

		if (instance == null) {
			try {
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser saxParser = spf.newSAXParser();
				XMLReader xmlReader = saxParser.getXMLReader();
				SAXVesselDataHandler handler = new SAXVesselDataHandler();
				xmlReader.setContentHandler(handler);
				xmlReader.parse(pathResolver.get("dat/vesselData.xml").toString());
				instance = handler.vesselData;
			} catch (URISyntaxException ex) { // shouldn't happen
				throw new VesselDataException(ex);
			} catch (SAXException ex) {
				throw new VesselDataException(ex);
			} catch (ParserConfigurationException ex) {
				throw new VesselDataException(ex);
			} catch (IOException ex) {
				throw new VesselDataException(ex);
			}
		}

		return instance;
	}

	/**
	 * Preloads all of the Model2D objects into memory. If you do not call this
	 * method, then each Model2D object will be loaded into memory on demand
	 * instead.
	 */
	public static void preloadModels() {
		for (Vessel vessel : get().vessels.values()) {
			vessel.getModel();
		}

		for (ObjectType objectType : ObjectType.values()) {
			objectType.getModel();
		}

		for (CreatureType creatureType : CreatureType.values()) {
			creatureType.getModel();
		}
	}

	/**
	 * Preloads all of the VesselInternals objects into memory. If you do not
	 * call this method, then each VesselInternals object will be loaded into
	 * memory on demand instead.
	 */
	public static void preloadInternals() {
		for (Vessel vessel : get().vessels.values()) {
			vessel.getInternals();
		}
	}

	/**
	 * Returns the Model2D object corresponding to the given comma-delimited
	 * list of .dxs file paths. The model will only be loaded once; after that
	 * it is cached in memory. Note that you must invoke setArtemisInstallPath()
	 * first, before calling this method.
	 */
	public static Model getModel(String dxsPaths) {
		Model model = models.get(dxsPaths);

		if (model == null) {
			model = Model.build(dxsPaths);
			models.put(dxsPaths, model);
		}

		return model;
	}


	Version version;
	List<Faction> factions = new ArrayList<Faction>();
	Map<Integer, Vessel> vessels = new LinkedHashMap<Integer, Vessel>();

	VesselData(String version) {
		this.version = new Version(version);
	}

	/**
	 * Returns the version of Artemis reported by vesselData.xml. Note that this
	 * does not neccessarily match the version reported by the protocol; the
	 * version in vesselData.xml is known to lag behind the actual version
	 * number.
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * Returns the Faction represented by the given ID.
	 */
	public Faction getFaction(int id) {
		return factions.get(id);
	}

	/**
	 * Returns the Vessel represented by the given ID.
	 */
	public Vessel getVessel(int id) {
		return vessels.get(Integer.valueOf(id));
	}

	/**
	 * Iterates all the Factions in this object.
	 */
	public Iterator<Faction> factionIterator() {
		return factions.iterator();
	}

	/**
	 * Iterates all the Vessels in this object.
	 */
	public Iterator<Vessel> vesselIterator() {
		return vessels.values().iterator();
	}
}
