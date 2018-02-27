package com.walkertribe.ian.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses .dxs files.
 * @author rjwut
 */
public class SAXModelHandler extends DefaultHandler {
	private enum ParseMode {
		NONE, VERTICES, POLYS
	}

	private String dxsPath;
	private Map<String, Vertex> vertices = new HashMap<String, Vertex>();
	private List<Poly> polys = new LinkedList<Poly>();
	private ParseMode mode = ParseMode.NONE;
	private List<String> vertexList = new ArrayList<String>();

	public SAXModelHandler(String dxsPath) {
		this.dxsPath = dxsPath;
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attrs) throws SAXException {
		if ("vertices".equals(qName)) {
			mode = ParseMode.VERTICES;
		} else if ("polygons".equals(qName)) {
			mode = ParseMode.POLYS;
		} else if ("vertex".equals(qName)) {
			if (mode == ParseMode.VERTICES) {
				// Y-axis in top-down view is Z-axis in game space
				double x = Double.parseDouble(attrs.getValue("x"));
				double y = Double.parseDouble(attrs.getValue("y"));
				double z = Double.parseDouble(attrs.getValue("z"));
				vertices.put(dxsPath + ":" + attrs.getValue("id"), new Vertex(x, y, z));
			} else if (mode == ParseMode.POLYS) {
				vertexList.add(dxsPath + ":" + attrs.getValue("vid"));
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("vertices".equals(qName) || "polygons".equals(qName)) {
			mode = ParseMode.NONE;
		} else if ("poly".equals(qName)) {
			if (vertexList.size() > 2) {
				polys.add(new Poly(vertexList));
			}

			vertexList.clear();
		}
    }

	/**
	 * Returns a Map containing all the Vertex objects discovered in the .dxs
	 * file.
	 */
	public Map<String, Vertex> getVertices() {
		return vertices;
	};

	/**
	 * Returns a List containing all the Poly objects discovered in the .dxs
	 * file.
	 */
	public List<Poly> getPolys() {
		return polys;
	}
}