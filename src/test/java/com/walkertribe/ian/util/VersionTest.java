package com.walkertribe.ian.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.BaseDebugger;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.core.CorePacketType;

public class VersionTest {
	private static final Version V1_0 = new Version("1.0");
	private static final Version V2_0 = new Version("2.0");
	private static final Version V2_1 = new Version("2.1");
	private static final Version V2_1_1 = new Version("2.1.1");
	private static final Map<Version, String> VERSION_PACKETS = new HashMap<Version, String>();

	static {
		VERSION_PACKETS.put(V1_0, "efbeadde1c0000000200000000000000080000004ae748e50000803f");
		VERSION_PACKETS.put(V2_0, "efbeadde1c0000000200000000000000080000004ae748e500000040");
		VERSION_PACKETS.put(V2_1, "efbeadde280000000200000000000000140000004ae748e500000040020000000100000000000000");
		VERSION_PACKETS.put(V2_1_1, "efbeadde280000000200000000000000140000004ae748e500000040020000000100000001000000");
	}

	@Test
	public void testIntegersConstructor() {
		Assert.assertEquals("1.0", new Version(1, 0).toString());
		Assert.assertEquals("3.0.1", new Version(3, 0, 1).toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIntegersConstructorNullArg() {
		new Version((int[]) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIntegersConstructorZeroParts() {
		new Version();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIntegersConstructorNegativeParts() {
		new Version(2, -3);
	}

	@Test
	public void testFloatConstructor() {
		Assert.assertEquals("1.0", new Version(1.0f).toString());
		Assert.assertEquals("1.70", new Version(1.7f).toString());
		Assert.assertEquals("2.0", new Version(2.0f).toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFloatConstructorNegativeVersion() {
		new Version(-1.0f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFloatConstructorModernVersion() {
		new Version(2.1f);
	}

	@Test
	public void testStringConstructor() {
		Assert.assertEquals("1.0", new Version("1").toString());
		Assert.assertEquals("2.0", V2_0.toString());
		Assert.assertEquals("2.1", V2_1.toString());
		Assert.assertEquals("2.1.1", V2_1_1.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testStringConstructorNullArg() {
		new Version((String) null);
	}

	@Test(expected = NumberFormatException.class)
	public void testStringConstructorEmptyArg() {
		new Version("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testStringConstructorNegativePart() {
		new Version("-1.0");
	}

	@Test(expected = NumberFormatException.class)
	public void testStringConstructorInvalidCharacter() {
		new Version("2.3.q");
	}

	@Test
	public void testIsLegacy() {
		Assert.assertTrue(V1_0.isLegacy());
		Assert.assertTrue(V2_0.isLegacy());
		Assert.assertFalse(V2_1.isLegacy());
	}

	@Test
	public void testLt() {
		Assert.assertTrue(V1_0.lt(V2_0));
		Assert.assertFalse(V2_0.lt(V1_0));
		Assert.assertFalse(V2_1.lt(V2_1));
		Assert.assertTrue(V2_1.lt(V2_1_1));
		Assert.assertFalse(V2_1_1.lt(V2_1));
	}

	@Test(expected = NullPointerException.class)
	public void testLtNullArg() {
		V1_0.lt(null);
	}

	@Test
	public void testGt() {
		Assert.assertFalse(V1_0.gt(V2_0));
		Assert.assertTrue(V2_0.gt(V1_0));
		Assert.assertFalse(V2_1.gt(V2_1));
		Assert.assertFalse(V2_1.gt(V2_1_1));
		Assert.assertTrue(V2_1_1.gt(V2_1));
	}

	@Test(expected = NullPointerException.class)
	public void testGtNullArg() {
		V1_0.gt(null);
	}

	@Test
	public void testLe() {
		Assert.assertTrue(V1_0.le(V2_0));
		Assert.assertFalse(V2_0.le(V1_0));
		Assert.assertTrue(V2_1.le(V2_1));
		Assert.assertTrue(V2_1.le(V2_1_1));
		Assert.assertFalse(V2_1_1.le(V2_1));
	}

	@Test(expected = NullPointerException.class)
	public void testLeNullArg() {
		V1_0.le(null);
	}

	@Test
	public void testGe() {
		Assert.assertFalse(V1_0.ge(V2_0));
		Assert.assertTrue(V2_0.ge(V1_0));
		Assert.assertTrue(V2_1.ge(V2_1));
		Assert.assertFalse(V2_1.ge(V2_1_1));
		Assert.assertTrue(V2_1_1.ge(V2_1));
	}

	@Test(expected = NullPointerException.class)
	public void testGeNullArg() {
		V1_0.ge(null);
	}

	@Test
	public void testWriteTo() throws IOException {
		for (Map.Entry<Version, String> entry : VERSION_PACKETS.entrySet()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PacketWriter writer = new PacketWriter(baos);
			writer.start(Origin.CLIENT, JamCrc.compute(CorePacketType.CONNECTED));
			entry.getKey().writeTo(writer);
			writer.flush(new BaseDebugger());
			Assert.assertEquals(entry.getValue(), TextUtil.byteArrayToHexString(baos.toByteArray()));
		}
	}

	@Test
	public void testEqualsAndHashCode() {
		TestUtil.testEqualsAndHashCode(V1_0, new Version("1.0"), V2_0);
	}
}
