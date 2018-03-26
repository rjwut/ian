package com.walkertribe.ian.protocol;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.BaseDebugger;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.iface.ListenerRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.core.CoreArtemisProtocol;
import com.walkertribe.ian.util.TestUtil;
import com.walkertribe.ian.util.TextUtil;

/**
 * Class that can read and write test packet files, and perform various
 * operations useful for testing.
 *
 * The test files don't contain the raw binary data. Instead, they contain hex-
 * encoded bytes for easier reading by humans. You can add white space for
 * greater legibility. You can also add comments with double slashes (//); the
 * slashes and all characters after them until the next newline character will
 * be ignored.
 * 
 * Example:
 * // ToggleRedAlertPacket
 * efbeadde          // header
 * 20000000          // packet length = 32
 * 02000000          // origin = client
 * 00000000          // padding
 * 0c000000          // remaining length = 12
 * 3c1d824c 0a000000 // ToggleRedAlertPacket
 * 00000000          // unused
 *
 * @author rjwut
 */
public class TestPacketFile {
	public enum Mode {
		READ, WRITE
	}

	private static final Charset UTF_8 = Charset.forName("UTF-8");

	/**
	 * Reads in a test file, parses it, re-writes the packets, and confirms that
	 * the resulting output bytes exactly match the bytes that were originally
	 * read. Requires three arguments:
	 * - Path to Artemis install
	 * - Path to test file
	 * - Connection type for the packets in the file (SERVER or CLIENT)
	 */
	public static void main(String[] args) {
		String fileName = args[1];
		Origin origin = Origin.valueOf(args[2]);

		try {
			new TestPacketFile(new File(fileName), Mode.READ).test(origin);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private Mode mode;                  // READ or WRITE
	private byte[] bytes;               // bytes read in on read mode
	private OutputStream os;            // destination stream for write mode
	private ByteArrayOutputStream baos; // buffer for write mode

	/**
	 * Opens the named File (read or write).
	 */
	public TestPacketFile(File file, Mode mode) throws IOException {
		if (mode == Mode.READ) {
			initRead(new FileInputStream(file));
		} else if (mode == Mode.WRITE) {
			initWrite(new FileOutputStream(file));
		}
	}

	/**
	 * Reads test packet data from the given URL.
	 */
	public TestPacketFile(URL url) throws IOException {
		initRead(url.openStream());
	}

	/**
	 * Reads test packet data from the given InputStream.
	 */
	public TestPacketFile(InputStream is) throws IOException {
		initRead(is);
	}

	/**
	 * Reads the test packet data in the given byte array.
	 */
	public TestPacketFile(Origin origin, int pktType, byte[] payload) throws IOException {
		ByteArrayOutputStream out = null;

		try {
			out = new ByteArrayOutputStream();
			PacketWriter writer = new PacketWriter(out);
			writer.start(origin, pktType);
			writer.writeBytes(payload);
			initRead(new ByteArrayInputStream(out.toByteArray()));
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException ex) {
					// don't care
				}
			}
		}
		
	}

	/**
	 * Creates a TestPacketFile that will be written to the given OutputStream.
	 */
	public TestPacketFile(OutputStream os) {
		initWrite(os);
	}

	/**
	 * Reads the data from the given InputStream and stores it in a byte array
	 * for testing.
	 */
	private void initRead(InputStream is) throws IOException {
		mode = Mode.READ;
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, UTF_8));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String line;

		while ((line = reader.readLine()) != null) {
			// strip out comments and whitespace
			int commentIndex = line.indexOf("//");

			if (commentIndex != -1) {
				line = line.substring(0, commentIndex);
			}

			line = line.replaceAll("\\s+", "");

			if (line.isEmpty()) {
				continue;	// no actual data on this line
			}

			out.write(TextUtil.hexToByteArray(line));
		}

		reader.close();
		bytes = out.toByteArray();

		if (TestUtil.DEBUG) {
			System.out.println("Length: " + bytes.length + " bytes");
		}
	}

	/**
	 * Prepares an output buffer for writing to the given OutputStream.
	 */
	private void initWrite(OutputStream outputStream) {
		mode = Mode.WRITE;
		this.os = outputStream;
		baos = new ByteArrayOutputStream();
	}

	/**
	 * Given an Origin for the data in this file, attempts to parse and
	 * re-write the data. If an error occurs, a stack trace will be written out
	 * to System.err.
	 */
	public void test(Origin origin) {
		if (mode != Mode.READ) {
			throw new IllegalStateException("test() only valid for read mode");
		}

		// Set up PacketReader
		ListenerRegistry listeners = new ListenerRegistry();
		listeners.register(this);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		PacketReader reader = new PacketReader(
				origin,
				bais,
				new CoreArtemisProtocol(),
				listeners
		);
		PacketTestDebugger debugger = new PacketTestDebugger();

		// Read until there's no more data
		while (bais.available() > 0) {
			try {
				// Parse a packet, then get the bytes from the Debugger
				ArtemisPacket pkt = reader.readPacket(debugger).getPacket();
				System.out.println(pkt.getClass().getSimpleName());
				byte[] in = debugger.in;

				// Write the bytes out to a ByteArrayOutputStream
				ByteArrayOutputStream baosOut = new ByteArrayOutputStream();
				PacketWriter writer = new PacketWriter(baosOut);
				pkt.writeTo(writer, debugger);
				byte[] out = baosOut.toByteArray();

				// Compare against original bytes
				if (!diff(pkt, in, out)) {
					break; // diff already wrote out an error
				}
			} catch (ArtemisPacketException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Returns a PacketReader of the given origin that will consume the bytes
	 * from this file. If parse is true, TestPacketFile will register an
	 * ArtemisPacket listener to the ListenerRegistry it creates for the
	 * PacketReader, causing all known packets received to be parsed. Otherwise,
	 * no listener will be registered, and all known packets received will be
	 * emitted as UnparsedPackets.
	 */
	public PacketReader toPacketReader(Origin origin, boolean parse) {
		if (mode != Mode.READ) {
			throw new IllegalStateException("toPacketReader() only valid for read mode");
		}

		ListenerRegistry listeners = new ListenerRegistry();

		if (parse) {
			listeners.register(this);
		}

		return new PacketReader(
				origin,
				new ByteArrayInputStream(bytes),
				new CoreArtemisProtocol(),
				listeners
		);
	}

	/**
	 * Returns a PacketWriter of the given type to write packets to the OutputStream.
	 */
	public PacketWriter toPacketWriter(Origin type) {
		if (mode != Mode.WRITE) {
			throw new IllegalStateException("toPacketWriter() only valid for write mode");
		}

		return new PacketWriter(baos);
	}

	/**
	 * Grabs the bytes that have been written to the output buffer and writes
	 * them to the OutputStream, then closes it.
	 */
	public void close() throws IOException {
		if (mode != Mode.WRITE) {
			throw new IllegalStateException("close() only valid for read mode");
		}

		BufferedOutputStream bos = new BufferedOutputStream(os);
		OutputStreamWriter writer = new OutputStreamWriter(bos);
		writer.append(TextUtil.byteArrayToHexString(baos.toByteArray()));
		writer.close();
	}

	/**
	 * Do nothing upon parsing a packet; this simply forces all packets to be
	 * read when registered to the ListenerRegistry; the actual test work is
	 * handled by the PacketTestDebugger class.
	 */
	@Listener
	public void onPacket(ArtemisPacket pkt) {
		// do nothing
	}

	/**
	 * Returns true if the bytes in the given ByteArrayOutputStream match those
	 * from this file; false otherwise.
	 */
	public boolean matches(ByteArrayOutputStream baosOut) {
		if (mode != Mode.READ) {
			throw new IllegalStateException("matches() only valid for read mode");
		}

		byte[] bytes2 = baosOut.toByteArray();

		if (bytes.length != bytes2.length) {
			return false;
		}

		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] != bytes2[i]) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		return TextUtil.byteArrayToHexString(mode == Mode.READ ? bytes : baos.toByteArray());
	}


	/**
	 * Used by TestPacketFile to capture the raw bytes from the file. This is
	 * used by test() method. It also ensures that any parse warnings get output
	 * to the console.
	 */
	private class PacketTestDebugger extends BaseDebugger {
		private byte[] in;

		/**
		 * Captures the raw bytes of a received packet.
		 */
		@Override
		public void onRecvPacketBytes(Origin origin, int pktType,
				byte[] payload) {
			int packetLength = payload.length + 24;
			in = new byte[packetLength];
			int offset = 0;
			offset = writeInt(ArtemisPacket.HEADER, in, offset);
			offset = writeInt(packetLength, in, offset);
			offset = writeInt(origin.toInt(), in, offset);
			offset = writeInt(0, in, offset);
			offset = writeInt(packetLength - 20, in, offset);
			offset = writeInt(pktType, in, offset);
			System.arraycopy(payload, 0, in, 24, payload.length);
		}

		@Override
		public void onPacketParseException(ArtemisPacketException ex) {
			ex.printPacketDump();
			ex.printStackTrace();
		}

		@Override
		public void onPacketWriteException(ArtemisPacket pkt, Exception ex) {
			System.err.println(pkt);
			ex.printStackTrace();
		}

		@Override
		public void warn(String msg) {
			System.out.println("WARNING: " + msg);
		}
	}

	/**
	 * Writes an int to the given byte array at the indicated offset.
	 */
	private static int writeInt(int v, byte[] bytes, int offset) {
		bytes[offset++] = (byte) (v & 0xff);
		bytes[offset++] = (byte) ((v >> 8) & 0xff);
		bytes[offset++] = (byte) ((v >> 16) & 0xff);
		bytes[offset++] = (byte) ((v >> 24) & 0xff);
		return offset;
	}

	/**
	 * Returns true if the given byte arrays match. Otherwise, it prints out a
	 * description of the packet and the input and output byte arrays and
	 * returns false.
	 */
	private static boolean diff(ArtemisPacket pkt, byte[] in, byte[] out) {
		boolean ok = in.length == out.length;

		for (int i = 0; ok && i < in.length; i++) {
			ok = in[i] == out[i];
		}

		if (!ok) {
			System.out.println(pkt);
			System.out.println("   IN: " + TextUtil.byteArrayToHexString(in));
			System.out.println("  OUT: " + TextUtil.byteArrayToHexString(out));
		}

		return ok;
	}
}