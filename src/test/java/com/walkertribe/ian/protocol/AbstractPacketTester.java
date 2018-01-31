package com.walkertribe.ian.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.BaseDebugger;
import com.walkertribe.ian.iface.Debugger;
import com.walkertribe.ian.iface.OutputStreamDebugger;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.util.TestUtil;

/**
 * Abstract class that can be extended for testing individual packet types.
 */
public abstract class AbstractPacketTester<T extends ArtemisPacket> {
	protected static final float EPSILON = TestUtil.EPSILON;

	/**
	 * Invoked by AbstractPacketTester when it has successfully parsed the
	 * desired number of packets with no bytes left over. The resulting packets
	 * are passed in; subclasses should evaluate them to ensure that they
	 * contain the expected data and throw an assert if not.
	 */
	protected abstract void testPackets(List<T> packets);

	/**
	 * Loads the test packet file at the indicated path and reads the given
	 * number of packets from it. After building a human-readable description of
	 * each packet, they are then passed to testPackets(); subclasses will
	 * override this to perform type-specific tests for those packets. Finally,
	 * the packets will be written out to a stream, and the resulting bytes
	 * compared to the original file.
	 */
	protected void execute(String resourcePath, ConnectionType type, int packetCount) {
		try {
			Debugger debugger = buildDebugger();
			TestPacketFile file = loadTestPacketFile(resourcePath);
			List<T> list = readPackets(file, type, packetCount, debugger);
			testPackets(list); // delegate to subclass
			ByteArrayOutputStream baos = writePackets(list, debugger);
			Assert.assertTrue(file.matches(baos)); // output matches input
		} catch (ArtemisPacketException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Returns a TestPacketFile containing the data found in the file at the
	 * given resource path.
	 */
	public TestPacketFile loadTestPacketFile(String resourcePath) throws IOException {
		if (TestUtil.DEBUG) {
			System.out.println("\n### " + getClass().getSimpleName());
		}

		URL url = TestPacketFile.class.getResource(resourcePath);
		TestPacketFile file = null;

		try {
			file = new TestPacketFile(url, getContext());
		} catch (NullPointerException ex) {
			// Test packet file now found
			Assert.fail("Test packet file not found: " + resourcePath);
		}

		return file;
	}

	/**
	 * Returns a Context instance to use for this test. The default
	 * implementation returns null.
	 */
	protected Context getContext() {
		return null;
	}

	/**
	 * Given a TestPacketFile and expected ConnectionType, reads the indicated
	 * number of packets from the file and returns them in a List.  
	 */
	protected List<T> readPackets(TestPacketFile file, ConnectionType type,
			int packetCount, Debugger debugger) throws ArtemisPacketException {
		PacketReader reader = buildPacketReader(file, type);
		List<T> list = new ArrayList<T>(packetCount);

		for (int i = 0; i < packetCount; i++) {
			@SuppressWarnings("unchecked")
			T pkt = (T) reader.readPacket(debugger).getPacket();
			Assert.assertNotNull(pkt);
			Assert.assertEquals(type, pkt.getConnectionType());
			Assert.assertFalse(reader.hasMore()); // Any bytes left over?
			list.add(pkt);
			pkt.toString();
		}

		return list;
	}

	/**
	 * Returns a ByteArrayOutputStream to which the given List of packets has
	 * been written.
	 */
	protected ByteArrayOutputStream writePackets(List<T> list, Debugger debugger) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PacketWriter writer = new PacketWriter(baos);

		try {
			for (T pkt : list) {
				pkt.writeTo(writer, debugger);
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		return baos;
	}

	/**
	 * Returns a new Debugger object to use for this test.
	 */
	protected Debugger buildDebugger() {
		return TestUtil.DEBUG ? new OutputStreamDebugger("", System.out, System.err) : new BaseDebugger();
	}

	/**
	 * Returns a PacketReader that reads the contents of the given
	 * TestPacketFile.
	 */
	protected PacketReader buildPacketReader(TestPacketFile file, ConnectionType type) {
		return file.toPacketReader(type, true);
	}
}