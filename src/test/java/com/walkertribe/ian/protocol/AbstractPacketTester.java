package com.walkertribe.ian.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.BaseDebugger;
import com.walkertribe.ian.iface.Debugger;
import com.walkertribe.ian.iface.OutputStreamDebugger;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.iface.ParseResult;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.util.TestUtil;

/**
 * Abstract class that can be extended for testing individual packet types.
 */
public abstract class AbstractPacketTester<T extends ArtemisPacket> {
	protected static final float EPSILON = TestUtil.EPSILON;
	private static final Debugger DEFAULT_DEBUGGER = TestUtil.DEBUG ? new OutputStreamDebugger("", System.out, System.err) : new BaseDebugger();

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
	protected void execute(String resourcePath, Origin origin, int packetCount) {
		try {
			Debugger debugger = getDebugger();
			TestPacketFile file = loadTestPacketFile(resourcePath);
			List<T> list = readPackets(file, origin, packetCount, debugger);
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
			file = new TestPacketFile(url);
		} catch (NullPointerException ex) {
			// Test packet file now found
			Assert.fail("Test packet file not found: " + resourcePath);
		}

		return file;
	}

	/**
	 * Given a TestPacketFile and expected Origin, reads the indicated
	 * number of packets from the file and returns them in a List.  
	 */
	protected List<T> readPackets(TestPacketFile file, Origin origin,
			int packetCount, Debugger debugger) throws ArtemisPacketException {
		PacketReader reader = buildPacketReader(file, origin);
		List<T> list = new ArrayList<T>(packetCount);

		for (int i = 0; i < packetCount; i++) {
			ParseResult result = reader.readPacket(debugger);
			ArtemisPacketException ex = result.getException();

			if (ex != null) {
				ex.printPacketDump();
				ex.printStackTrace();
				Assert.fail("Exception thrown while parsing: " + ex.getMessage());
			}

			@SuppressWarnings("unchecked")
			T pkt = (T) result.getPacket();
			Assert.assertNotNull(pkt);
			Assert.assertEquals(origin, pkt.getOrigin());
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
	 * Returns a Debugger object to use for this test.
	 */
	protected Debugger getDebugger() {
		return DEFAULT_DEBUGGER;
	}

	/**
	 * Returns a PacketReader that reads the contents of the given
	 * TestPacketFile.
	 */
	protected PacketReader buildPacketReader(TestPacketFile file, Origin origin) {
		return file.toPacketReader(origin, true);
	}
}