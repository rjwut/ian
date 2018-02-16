package com.walkertribe.ian.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class BitFieldTest {
	private enum ZeroBits { }
	private enum OneBit { V0 }
	private enum EightBits { V0, V1, V2, V3, V4, V5, V6, V7 }
	private enum NineBits { V0, V1, V2, V3, V4, V5, V6, V7, V8 }

	private static final ZeroBits[] ZERO_BITS_VALUES = ZeroBits.values();
	private static final OneBit[] ONE_BIT_VALUES = OneBit.values();
	private static final EightBits[] EIGHT_BITS_VALUES = EightBits.values();
	private static final NineBits[] NINE_BITS_VALUES = NineBits.values();

	@Test
	public void testZeroBits() throws IOException {
		BitField field = new BitField(ZERO_BITS_VALUES.length);

		for (ZeroBits v : ZERO_BITS_VALUES) {
			Assert.assertFalse(field.get(v.ordinal()));
		}

		Assert.assertEquals(1, field.getByteCount());
		Assert.assertEquals("", field.listActiveBits(ZERO_BITS_VALUES));
		assertFieldBytes(new byte[] { (byte) 0 }, field);
	}

	@Test
	public void testOneBit() throws IOException {
		BitField field = new BitField(ONE_BIT_VALUES.length);
		Assert.assertFalse(field.get(OneBit.V0.ordinal()));
		Assert.assertEquals(1, field.getByteCount());
		Assert.assertEquals("", field.listActiveBits(ONE_BIT_VALUES));
		assertFieldBytes(new byte[] { (byte) 0 }, field);
		Assert.assertEquals("00", field.toString());
		field.set(OneBit.V0.ordinal(), true);
		Assert.assertTrue(field.get(OneBit.V0.ordinal()));
		Assert.assertEquals(1, field.getByteCount());
		Assert.assertEquals("V0", field.listActiveBits(ONE_BIT_VALUES));
		assertFieldBytes(new byte[] { (byte) 1 }, field);
		Assert.assertEquals("01", field.toString());
		field.set(OneBit.V0.ordinal(), false);
		Assert.assertFalse(field.get(OneBit.V0.ordinal()));
		Assert.assertEquals(1, field.getByteCount());
		Assert.assertEquals("", field.listActiveBits(ONE_BIT_VALUES));
		assertFieldBytes(new byte[] { (byte) 0 }, field);
		Assert.assertEquals("00", field.toString());
	}

	@Test
	public void testEightBits() throws IOException {
		BitField field = new BitField(EIGHT_BITS_VALUES.length);

		for (EightBits v : EIGHT_BITS_VALUES) {
			Assert.assertFalse(field.get(v.ordinal()));
		}

		Assert.assertEquals(2, field.getByteCount());
		Assert.assertEquals("", field.listActiveBits(EIGHT_BITS_VALUES));
		assertFieldBytes(new byte[] { (byte) 0, (byte) 0 }, field);

		for (EightBits v : EIGHT_BITS_VALUES) {
			field.set(v.ordinal(), true);
			Assert.assertTrue(field.get(v.ordinal()));
		}

		Assert.assertEquals(2, field.getByteCount());
		Assert.assertEquals("V0 V1 V2 V3 V4 V5 V6 V7", field.listActiveBits(EIGHT_BITS_VALUES));
		assertFieldBytes(new byte[] { (byte) 255, (byte) 0 }, field);
	}

	@Test
	public void testNineBits() throws IOException {
		BitField field = new BitField(NINE_BITS_VALUES.length);

		for (NineBits v : NINE_BITS_VALUES) {
			Assert.assertFalse(field.get(v.ordinal()));
		}

		Assert.assertEquals(2, field.getByteCount());
		Assert.assertEquals("", field.listActiveBits(NINE_BITS_VALUES));
		assertFieldBytes(new byte[] { (byte) 0, (byte) 0 }, field);

		for (NineBits v : NINE_BITS_VALUES) {
			field.set(v.ordinal(), true);
			Assert.assertTrue(field.get(v.ordinal()));
		}

		Assert.assertEquals(2, field.getByteCount());
		Assert.assertEquals("V0 V1 V2 V3 V4 V5 V6 V7 V8", field.listActiveBits(NINE_BITS_VALUES));
		assertFieldBytes(new byte[] { (byte) 255, (byte) 1 }, field);
	}

	/**
	 * Asserts that the given BitField, when written to an OutputStream, will
	 * yield the indicated bytes.
	 */
	private static void assertFieldBytes(byte[] expected, BitField field) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(field.getByteCount());
		field.write(baos);
		Assert.assertArrayEquals(expected, baos.toByteArray());
	}
}
