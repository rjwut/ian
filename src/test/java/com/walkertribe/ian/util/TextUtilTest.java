package com.walkertribe.ian.util;

import org.junit.Assert;
import org.junit.Test;

public class TextUtilTest {
	private static final byte[] EMPTY_ARRAY = new byte[] {};
	private static final byte[] ONE_BYTE_ARRAY = new byte[] { 0 };
	private static final String EMPTY_STRING = "";
	private static final String ONE_BYTE_STRING = "00";
	private static final byte[] DEADBEEF_ARRAY = new byte[] { (byte) 0xde, (byte) 0xad, (byte) 0xbe, (byte) 0xef };
	private static final String DEADBEEF_STRING = "deadbeef";

	@Test
	public void testByteToHex() {
		Assert.assertEquals("00", TextUtil.byteToHex((byte) 0));
		Assert.assertEquals("ff", TextUtil.byteToHex((byte) 255));
	}

	@Test
	public void testByteArrayToHexString() {
		Assert.assertEquals(EMPTY_STRING, TextUtil.byteArrayToHexString(EMPTY_ARRAY));
		Assert.assertEquals(ONE_BYTE_STRING, TextUtil.byteArrayToHexString(ONE_BYTE_ARRAY));
		Assert.assertEquals(DEADBEEF_STRING, TextUtil.byteArrayToHexString(DEADBEEF_ARRAY));
	}

	@Test(expected = NullPointerException.class)
	public void testByteArrayToHexStringNullArray() {
		TextUtil.byteArrayToHexString(null);
	}

	@Test
	public void testByteArrayToHexStringOffset() {
		Assert.assertEquals("", TextUtil.byteArrayToHexString(DEADBEEF_ARRAY, 0, 0));
		Assert.assertEquals("de", TextUtil.byteArrayToHexString(DEADBEEF_ARRAY, 0, 1));
		Assert.assertEquals("adbeef", TextUtil.byteArrayToHexString(DEADBEEF_ARRAY, 1, 3));
	}

	@Test(expected = NullPointerException.class)
	public void testByteArrayToHexStringOffsetNullArray() {
		TextUtil.byteArrayToHexString(null, 0, 1);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testByteArrayToHexStringNegativeOffset() {
		TextUtil.byteArrayToHexString(DEADBEEF_ARRAY, -1, 1);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testByteArrayToHexStringOverflowOffset() {
		TextUtil.byteArrayToHexString(DEADBEEF_ARRAY, 4, 1);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testByteArrayToHexStringOffsetPastEndOfarray() {
		TextUtil.byteArrayToHexString(DEADBEEF_ARRAY, 1, 4);
	}

	@Test
	public void testIntToHex() {
		Assert.assertEquals("00000000", TextUtil.intToHex(0));
		Assert.assertEquals("80000000", TextUtil.intToHex(Integer.MIN_VALUE));
		Assert.assertEquals("7fffffff", TextUtil.intToHex(Integer.MAX_VALUE));
		Assert.assertEquals(DEADBEEF_STRING, TextUtil.intToHex(0xdeadbeef));
	}

	@Test
	public void testIntToHexLe() {
		Assert.assertEquals("00000000", TextUtil.intToHexLE(0));
		Assert.assertEquals("00000080", TextUtil.intToHexLE(Integer.MIN_VALUE));
		Assert.assertEquals("ffffff7f", TextUtil.intToHexLE(Integer.MAX_VALUE));
		Assert.assertEquals(DEADBEEF_STRING, TextUtil.intToHexLE(0xefbeadde));
	}

	@Test
	public void testHexToByteArray() {
		Assert.assertArrayEquals(EMPTY_ARRAY, TextUtil.hexToByteArray(EMPTY_STRING));
		Assert.assertArrayEquals(ONE_BYTE_ARRAY, TextUtil.hexToByteArray(ONE_BYTE_STRING));
		Assert.assertArrayEquals(DEADBEEF_ARRAY, TextUtil.hexToByteArray(DEADBEEF_STRING));
	}

	@Test(expected = NullPointerException.class)
	public void testHexToByteArrayNullString() {
		TextUtil.hexToByteArray(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHexToByteArrayOddLength() {
		TextUtil.hexToByteArray("deadbee");
	}

	@Test(expected = NumberFormatException.class)
	public void testHexToByteArrayInvalidCharacter() {
		TextUtil.hexToByteArray("deadbeet");
	}

	@Test
	public void makeEclEmmaHappy() {
		TestUtil.coverPrivateConstructor(TextUtil.class);
	}
}
