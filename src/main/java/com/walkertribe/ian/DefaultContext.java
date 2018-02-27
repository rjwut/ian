package com.walkertribe.ian;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.model.SAXModelHandler;
import com.walkertribe.ian.vesseldata.SAXVesselDataHandler;
import com.walkertribe.ian.vesseldata.VesselData;
import com.walkertribe.ian.vesseldata.VesselInternals;

/**
 * Context implementation that loads resources from a path using a provided
 * PathResolver, and caches the results.
 * @author rjwut
 */
public class DefaultContext implements Context {
	private PathResolver pathResolver;
	private VesselData vesselData;
	private Map<String, Model> modelMap = new HashMap<String, Model>();
	private Map<String, VesselInternals> internalsMap = new HashMap<String, VesselInternals>();

	/**
	 * Creates a new Context using the given PathResolver.
	 */
	public DefaultContext(PathResolver pathResolver) {
		if (pathResolver == null) {
			throw new IllegalArgumentException("You must provide a PathResolver");
		}

		this.pathResolver = pathResolver;
	}

	/**
	 * Returns the PathResolver for this Context.
	 */
	public PathResolver getPathResolver() {
		return pathResolver;
	}

	/**
	 * Returns a VesselData object describing all the information in
	 * vesselData.xml. The first time this method is invoked for this object,
	 * vesselData.xml will be loaded and parsed, and the result will be cached
	 * in this object for later re-use.
	 */
	public VesselData getVesselData() {
		if (vesselData == null) {
			vesselData = loadVesselData();
		}

		return vesselData;
	}

	/**
	 * Given a comma-delimited list of dxsPaths, returns a Model object that
	 * describes the union of the 3D models stored in those files. The Model
	 * will be cached in this object, and the cached Model will be used for
	 * subsequent requests for the same .dxs file(s).
	 */
	public Model getModel(String dxsPaths) {
		Model model = modelMap.get(dxsPaths);

		if (model == null) {
			model = loadModel(dxsPaths);
			modelMap.put(dxsPaths, model);
		}

		return model;
	}

	/**
	 * Given the path to an .snt file, returns a VesselInternals object that
	 * describes the node grid stored in that file. The VesselInternals will be
	 * cached in this object, and the cached VesselInternals will be used for
	 * subsequent requests for the same .snt file.
	 */
	public VesselInternals getInternals(String sntPath) {
		VesselInternals internals = internalsMap.get(sntPath);

		if (internals == null) {
			internals = loadInternals(sntPath);
			internalsMap.put(sntPath, internals);
		}

		return internals;
	}

	/**
	 * Loads the vesselData.xml file using the given PathResolver and returns the resulting
	 * VesselData object.
	 */
	private VesselData loadVesselData() {
		SAXVesselDataHandler handler = new SAXVesselDataHandler(this);
		parseXml("dat/vesselData.xml", handler);
		return handler.getVesselData();
	}

	/**
	 * Constructs a Model described by the given .dxs files.
	 */
	private Model loadModel(String dxsPaths) {
		Model model = new Model(dxsPaths);
		String[] pathsArr = dxsPaths.split(",");

		for (String path : pathsArr) {
			SAXModelHandler handler = new SAXModelHandler(path);
			parseXml(path, handler);
			model.add(handler.getVertices(), handler.getPolys());
		}

		return model;
	}

	/**
	 * Creates and returns a VesselInternals object loaded from the indicated
	 * .snt file.
	 */
	private VesselInternals loadInternals(String sntPath) {
		InputStream in = null;

		try {
			return new VesselInternals(pathResolver.get(sntPath));
		} catch (MalformedURLException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					// don't care
				}
			}
		}

	}

	/**
	 * Parses the XML file located at the indicated path using the given
	 * DefaultHandler.
	 */
	private void parseXml(String path, DefaultHandler handler) {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(handler);
			xmlReader.parse(new InputSource(pathResolver.get(path)));
		} catch (SAXException ex) {
			throw new RuntimeException(ex);
		} catch (ParserConfigurationException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
