package com.walkertribe.ian;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.BaseDebugger;
import com.walkertribe.ian.iface.Debugger;
import com.walkertribe.ian.iface.Listener;
import com.walkertribe.ian.iface.ListenerRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.ParseResult;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.RawPacket;
import com.walkertribe.ian.protocol.core.CoreArtemisProtocol;
import com.walkertribe.ian.util.TextUtil;

public class ParseRawBinaryPackets {
	private static final String ARTEMIS_PATH = "C:\\Users\\rjwalker\\Desktop\\stuff\\artemis";
	private static final String PACKET_PATH = ARTEMIS_PATH + "\\packet";
	private static final String[] SERVER = new String[] {
			"artemis-2016-09-30.01s",
			"artemis-2017-11-12.1940.01s",
			"artemis-2017-11-12.2023.01s",
			"artemis-2017-11-18.2053.01s",
			"artemis-2018-01-27.2333.5ship.01s"
	};
	private static final String[] CLIENT = new String[] {
			"artemis-2016-09-30.01c",
			"artemis-2017-11-12.1940.01c",
			"artemis-2017-11-12.2023.01c",
			"artemis-2017-11-18.2053.01c"
	};

	public static void main(String[] args) {
		int count = 0;

		for (String filename : SERVER) {
			count += new ParseRawBinaryPackets(filename, Origin.SERVER).getPacketCount();
		}

		for (String filename : CLIENT) {
			count += new ParseRawBinaryPackets(filename, Origin.CLIENT).getPacketCount();
		}

		System.out.println("Packets read: " + count);
	}

	private int packetCount = 0;

	public ParseRawBinaryPackets(String filename, Origin type) {
		FileInputStream fis = null;
		Context ctx = new DefaultContext(new FilePathResolver(ARTEMIS_PATH));
		ListenerRegistry listenerRegistry = new ListenerRegistry();
		listenerRegistry.register(this);
		Debugger debugger = new BaseDebugger();

		try {
			fis = new FileInputStream(PACKET_PATH + File.separator + filename);
			PacketReader reader = new PacketReader(ctx, type, fis, new CoreArtemisProtocol(), listenerRegistry);

			while (true) {
				ParseResult result = reader.readPacket(debugger);
				packetCount++;
				result.fireListeners();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ArtemisPacketException ex) {
			if (ex.getCause() instanceof EOFException) {
				System.out.println("### END OF STREAM ###");
			} else {
				ex.printStackTrace();
				System.err.println("PAYLOAD: " + TextUtil.byteArrayToHexString(ex.getPayload()));
			}
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private int getPacketCount() {
		return packetCount;
	}

	@Listener
	public void onPacket(ArtemisPacket pkt) {
		if (pkt instanceof RawPacket) {
			System.out.println(pkt);
		}
	}
}
