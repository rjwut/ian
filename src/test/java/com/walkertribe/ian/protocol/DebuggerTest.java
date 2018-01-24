package com.walkertribe.ian.protocol;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.Test;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.BaseDebugger;
import com.walkertribe.ian.iface.Debugger;
import com.walkertribe.ian.iface.OutputStreamDebugger;
import com.walkertribe.ian.protocol.core.PausePacket;

public class DebuggerTest {
	@Test
	public void testBaseDebugger() {
		exerciseDebugger(new BaseDebugger());
	}

	@Test
	public void testOutputStreamDebugger() {
		OutputStream out = new ByteArrayOutputStream();
		exerciseDebugger(new OutputStreamDebugger("", out, out));
		PrintStream ps = new PrintStream(out);
		exerciseDebugger(new OutputStreamDebugger("test", ps, ps));
	}

	private void exerciseDebugger(Debugger debugger) {
		debugger.onPacketParseException(new ArtemisPacketException("test"));
		debugger.onPacketParseException(new ArtemisPacketException(new RuntimeException("test"), ConnectionType.SERVER, 0, new byte[] {}));
		debugger.onPacketWriteException(new PausePacket(true), new RuntimeException("test"));
		debugger.onRecvPacketBytes(ConnectionType.SERVER, 0, new byte[] {});
		debugger.onRecvParsedPacket(new PausePacket(true));
		debugger.onRecvUnparsedPacket(new UnknownPacket(ConnectionType.SERVER, 0, new byte[] {}));
		debugger.onSendPacket(new PausePacket(true));
		debugger.onSendPacketBytes(ConnectionType.SERVER, 0, new byte[] {});
		debugger.warn("test");
	}
}
