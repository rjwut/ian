package com.walkertribe.ian.vesseldata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
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
	 * A PathResolver is an object that tells IAN where to look for files it
	 * wants to read. A PathResolver must be set before you can invoke get() or
	 * getModel().
	 */
	public static void setPathResolver(PathResolver resolver) {
		pathResolver = resolver;
		models = new HashMap<String, Model>();
		instance = null;
	}

	/**
	 * Returns the VesselData instance. If vesselData.xml has not yet been
	 * parsed when get() is called, it will be parsed at that time. Note that
	 * you must invoke setPathResolver() first, before calling this method.
	 */
	public static VesselData get() {
		if (pathResolver == null) {
			throw new IllegalStateException("Must invoke setPathResolver() first");
		}

		if (instance == null) {
			try {
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser saxParser = spf.newSAXParser();
				XMLReader xmlReader = saxParser.getXMLReader();
				SAXVesselDataHandler handler = new SAXVesselDataHandler();
				xmlReader.setContentHandler(handler);
				xmlReader.parse(new InputSource(pathResolver.get("dat/vesselData.xml")));
				instance = handler.vesselData;
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
	 * Preloads all of the Model objects into memory. If you do not call this
	 * method, then each Model object will be loaded into memory on demand
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
	 * it is cached in memory. Note that you must invoke setPathResolver()
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
