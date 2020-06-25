package com.walkertribe.ian;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import com.walkertribe.ian.enums.ShipSystem;
import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.model.SAXModelHandler;
import com.walkertribe.ian.util.ByteArrayReader;
import com.walkertribe.ian.util.Grid;
import com.walkertribe.ian.util.GridCoord;
import com.walkertribe.ian.util.GridNode;
import com.walkertribe.ian.vesseldata.SAXVesselDataHandler;
import com.walkertribe.ian.vesseldata.VesselData;

/**
 * Context implementation that loads resources from a path using a provided
 * PathResolver, and caches the results.
 * @author rjwut
 */
public class DefaultContext implements Context {
    private static final int EMPTY_NODE_VALUE = -2;
    private static final int HALLWAY_NODE_VALUE = -1;
    private static final int BLOCK_SIZE = 32;
    private static final byte[] RESERVED = new byte[16];

	private PathResolver pathResolver;
	private VesselData vesselData;
	private Map<String, Model> modelMap = new HashMap<String, Model>();
	private Map<String, Grid> gridMap = new HashMap<String, Grid>();

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
	 * Given the path to an .snt file, returns a Grid object that describes the node nodes stored in
	 * that file. The Grid will be cached in this object, and the cached Grid will be used for
	 * subsequent requests for the same .snt file.
	 */
	public Grid getGrid(String sntPath) {
		Grid grid = gridMap.get(sntPath);

		if (grid == null) {
			grid = readSnt(sntPath);
			gridMap.put(sntPath, grid);
		}

		return grid;
	}

	/**
	 * Outputs the given Grid as an .snt file. An IllegalStateException will be thrown if any node
	 * is encountered which doesn't contain the required information.
	 */
	public void writeSnt(Grid grid, OutputStream out) throws IOException {
	    final byte[] buffer = new byte[4];

	    for (int x = 0; x < GridCoord.MAX_X; x++) {
            for (int y = 0; y < GridCoord.MAX_X; y++) {
                for (int z = 0; z < GridCoord.MAX_X; z++) {
                    GridCoord coord = GridCoord.get(x, y, z);
                    writeFloat(buffer, out, coord.x());
                    writeFloat(buffer, out, coord.y());
                    writeFloat(buffer, out, coord.z());
                    GridNode node = grid.getNode(coord);
                    int type;

                    if (node == null || !node.isAccessible()) {
                        type = EMPTY_NODE_VALUE;
                    } else {
                        ShipSystem system = node.getSystem();

                        if (system == null) {
                            type = HALLWAY_NODE_VALUE;
                        } else {
                            type = system.ordinal();
                        }
                    }

                    writeInt(buffer, out, type);
                    out.write(RESERVED);
                }
            }
        }
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
	 * Creates and returns a Grid object loaded from the indicated .snt file.
	 */
	private Grid readSnt(String sntPath) {
		InputStream in = null;

		try {
		    in = pathResolver.get(sntPath);

		    if (!(in instanceof BufferedInputStream)) {
	            in = new BufferedInputStream(in);
	        }

		    byte[] buffer = new byte[BLOCK_SIZE];
		    final ShipSystem[] systems = ShipSystem.values();
		    Grid grid = new Grid();

	        try {
	            for (int x = 0; x < GridCoord.MAX_X; x++) {
	                for (int y = 0; y < GridCoord.MAX_Y; y++) {
	                    for (int z = 0; z < GridCoord.MAX_Z; z++) {
	                        GridCoord coords = GridCoord.get(x, y, z);
	                        ByteArrayReader.readBytes(in, BLOCK_SIZE, buffer);
	                        ByteArrayReader reader = new ByteArrayReader(buffer);
	                        float shipX = reader.readFloat();
	                        float shipY = reader.readFloat();
	                        float shipZ = reader.readFloat();
	                        int type = reader.readInt();
	                        GridNode node;

	                        if (type == EMPTY_NODE_VALUE) {
	                            node = new GridNode(grid, coords, shipX, shipY, shipZ, false);
	                        } else if (type == HALLWAY_NODE_VALUE) {
                                node = new GridNode(grid, coords, shipX, shipY, shipZ, true);
	                        } else {
                                node = new GridNode(grid, coords, shipX, shipY, shipZ, systems[type]);
	                        }

	                        grid.setNode(node);
	                    }
	                }
	            }

	            grid.lock();
	            return grid;
	        } catch (InterruptedException ex) {
	            throw new RuntimeException(ex);
	        } catch (IOException ex) {
	            throw new RuntimeException(ex);
	        }
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

    /**
     * Writes a float value to the given OutputStream.
     */
    private static void writeFloat(byte[] buffer, OutputStream out, float v) throws IOException {
        writeInt(buffer, out, Float.floatToRawIntBits(v));
    }

    /**
     * Writes an int value to the given OutputStream.
     */
    private static void writeInt(byte[] buffer, OutputStream out, int v) throws IOException {
        buffer[0] = (byte) (0xff & v);
        buffer[1] = (byte) (0xff & (v >> 8));
        buffer[2] = (byte) (0xff & (v >> 16));
        buffer[3] = (byte) (0xff & (v >> 24));
        out.write(buffer, 0, 4);
    }
}
