package com.walkertribe.ian.util;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class ByteArrayReaderTest {
	private static final byte[] DEADBEEF = new byte[] { (byte) 0xef, (byte) 0xbe, (byte) 0xad, (byte) 0xde };
	private static final float DEADBEEF_AS_FLOAT = -6.259853398707798E18f;
	private static final byte[] BOOLEAN_TEST = new byte[] { 0, 0, 1, 1, 2, 0 };
	private static final SevenBits[] SEVEN_BITS = SevenBits.values();
	private static final EightBits[] EIGHT_BITS = EightBits.values();
	private static final byte[] US_ASCII_STRING = new byte[] { 3, 0, 0, 0, 'H', 'i', '!' };
	private static final byte[] US_ASCII_STRING_PAST_END = new byte[] { 3, 0, 0, 0, 'H', 'i' };
	private static final byte[] UTF_16_STRING = new byte[] { 4, 0, 0, 0, 'H', 0, 'i', 0, '!', 0, 0, 0 };
	private static final byte[] UTF_16_STRING_PAST_END = new byte[] { 4, 0, 0, 0, 'H', 0, 'i', 0, '!', 0, 0 };
	private static final byte[] UTF_16_STRING_EARLY_NULL = new byte[] { 4, 0, 0, 0, 'H', 0, 0, 0, 'i', 0, '!', 0 };

	private enum SevenBits {
		V0, V1, V2, V3, V4, V5, V6
	}

	private enum EightBits {
		V0, V1, V2, V3, V4, V5, V6, V7
	}
	
	@Test
	public void testStaticReadBytes() throws InterruptedException, IOException {
		// read all at once
		byte[] buffer = new byte[DEADBEEF.length];
		ByteArrayReader.readBytes(new ByteArrayInputStream(DEADBEEF), DEADBEEF.length, buffer);
		Assert.assertArrayEquals(DEADBEEF, buffer);
		// read byte-by-byte
		buffer = new byte[1];
		ByteArrayInputStream bais = new ByteArrayInputStream(DEADBEEF);

		for (int i = 0; i < DEADBEEF.length; i++) {
			ByteArrayReader.readBytes(bais, 1, buffer);
			Assert.assertEquals(DEADBEEF[i], buffer[0]);
		}
	}

	@Test(expected = NullPointerException.class)
	public void testStaticReadBytesNullInputStream() throws InterruptedException, IOException {
		ByteArrayReader.readBytes(null, 1, new byte[1]);
	}

	@Test(expected = NullPointerException.class)
	public void testStaticReadBytesNullBuffer() throws InterruptedException, IOException {
		ByteArrayReader.readBytes(new ByteArrayInputStream(DEADBEEF), 1, null);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testStaticReadNegativeBytes() throws InterruptedException, IOException {
		ByteArrayReader.readBytes(new ByteArrayInputStream(DEADBEEF), -1, new byte[DEADBEEF.length]);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testStaticReadBytesBufferTooSmall() throws InterruptedException, IOException {
		byte[] buffer = new byte[DEADBEEF.length - 1];
		ByteArrayReader.readBytes(new ByteArrayInputStream(DEADBEEF), DEADBEEF.length, buffer);
	}

	@Test
	public void testStaticReadZeroBytes() throws InterruptedException, IOException {
		byte[] buffer = new byte[DEADBEEF.length];
		ByteArrayReader.readBytes(new ByteArrayInputStream(DEADBEEF), 0, buffer);
		Assert.assertEquals((byte) 0, buffer[0]);
	}

	@Test(expected = EOFException.class)
	public void testStaticReadBytesPastEndOfStream() throws InterruptedException, IOException {
		ByteArrayInputStream baos = new ByteArrayInputStream(DEADBEEF);
		byte[] buffer = new byte[DEADBEEF.length + 1];
		ByteArrayReader.readBytes(baos, buffer.length, buffer);
	}

	@Test
	public void testStaticReadShort() {
		Assert.assertEquals(0xbeef, ByteArrayReader.readShort(DEADBEEF, 0));
		Assert.assertEquals(0xadbe, ByteArrayReader.readShort(DEADBEEF, 1));
		Assert.assertEquals(0xdead, ByteArrayReader.readShort(DEADBEEF, 2));
	}

	@Test(expected = NullPointerException.class)
	public void testStaticReadShortNullArray() {
		ByteArrayReader.readShort(null, 0);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testStaticReadShortNegativeOutOfBounds() {
		ByteArrayReader.readShort(DEADBEEF, -1);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testStaticReadShortPositiveOutOfBounds() {
		ByteArrayReader.readShort(DEADBEEF, 3);
	}

	@Test
	public void testStaticReadInt() {
		Assert.assertEquals(0xdeadbeef, ByteArrayReader.readInt(DEADBEEF, 0));
	}

	@Test(expected = NullPointerException.class)
	public void testStaticReadIntNullArray() {
		ByteArrayReader.readInt(null, 0);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testStaticReadIntNegativeOutOfBounds() {
		ByteArrayReader.readInt(DEADBEEF, -1);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testStaticReadIntPositiveOutOfBounds() {
		ByteArrayReader.readInt(DEADBEEF, 1);
	}

	@Test
	public void testStaticReadFloat() {
		Assert.assertEquals(DEADBEEF_AS_FLOAT, ByteArrayReader.readFloat(DEADBEEF, 0), TestUtil.EPSILON);
	}

	@Test(expected = NullPointerException.class)
	public void testStaticReadFloatNullArray() {
		ByteArrayReader.readFloat(null, 0);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testStaticReadFloatNegativeOutOfBounds() {
		ByteArrayReader.readFloat(DEADBEEF, -1);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testStaticReadFloatPositiveOutOfBounds() {
		ByteArrayReader.readFloat(DEADBEEF, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNullByteArray() {
		new ByteArrayReader(null);
	}

	@Test
	public void testPeekSkipAndGetBytesLeft() {
		ByteArrayReader reader = new ByteArrayReader(DEADBEEF);
		Assert.assertEquals((byte) 0xef, reader.peek());
		Assert.assertEquals(4, reader.getBytesLeft());
		reader.skip(0);
		Assert.assertEquals((byte) 0xef, reader.peek());
		Assert.assertEquals(4, reader.getBytesLeft());
		reader.skip(1);
		Assert.assertEquals((byte) 0xbe, reader.peek());
		Assert.assertEquals(3, reader.getBytesLeft());
		reader.skip(2);
		Assert.assertEquals((byte) 0xde, reader.peek());
		Assert.assertEquals(1, reader.getBytesLeft());
		reader.skip(1);
		Assert.assertEquals(0, reader.getBytesLeft());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSkipNegative() {
		new ByteArrayReader(DEADBEEF).skip(-1);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testSkipPastEndOfArray() {
		new ByteArrayReader(DEADBEEF).skip(5);
	}

	@Test
	public void testReadByte() {
		ByteArrayReader reader = new ByteArrayReader(DEADBEEF);

		for (int i = 0; i < DEADBEEF.length; i++) {
			Assert.assertEquals(DEADBEEF[i], reader.readByte());
		}

		Assert.assertEquals(0, reader.getBytesLeft());
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testReadBytePastEndOfArray() {
		ByteArrayReader reader = new ByteArrayReader(DEADBEEF);
		reader.skip(DEADBEEF.length);
		reader.readByte();
	}

	@Test
	public void testReadBytes() {
		ByteArrayReader reader = new ByteArrayReader(DEADBEEF);
		Assert.assertArrayEquals(Arrays.copyOf(DEADBEEF, 1), reader.readBytes(1));
		Assert.assertArrayEquals(Arrays.copyOfRange(DEADBEEF, 1, DEADBEEF.length), reader.readBytes(DEADBEEF.length - 1));
		Assert.assertEquals(0, reader.getBytesLeft());
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testReadBytesPastEndOfArray() {
		new ByteArrayReader(DEADBEEF).readBytes(DEADBEEF.length + 1);
	}

	@Test
	public void testReadBoolState() {
		ByteArrayReader reader = new ByteArrayReader(BOOLEAN_TEST);
		Assert.assertEquals(BoolState.FALSE, reader.readBoolState(1));
		Assert.assertEquals(BoolState.TRUE, reader.readBoolState(2));
		Assert.assertEquals(BoolState.TRUE, reader.readBoolState(1));
		Assert.assertEquals(BoolState.TRUE, reader.readBoolState(2));
		Assert.assertEquals(0, reader.getBytesLeft());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReadBoolStateNegativeBytes() {
		new ByteArrayReader(DEADBEEF).readBoolState(-1);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testReadBoolStatePastEndOfArray() {
		new ByteArrayReader(DEADBEEF).readBoolState(DEADBEEF.length + 1);
	}

	@Test
	public void testReadShort() {
		ByteArrayReader reader = new ByteArrayReader(DEADBEEF);
		Assert.assertEquals(0xbeef, reader.readShort());
		Assert.assertEquals(0xdead, reader.readShort());
		Assert.assertEquals(0, reader.getBytesLeft());
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testReadShortPastEndOfArray() {
		ByteArrayReader reader = new ByteArrayReader(DEADBEEF);
		reader.skip(DEADBEEF.length - 1);
		reader.readShort();
	}

	@Test
	public void testReadInt() {
		ByteArrayReader reader = new ByteArrayReader(DEADBEEF);
		Assert.assertEquals(0xdeadbeef, reader.readInt());
		Assert.assertEquals(0, reader.getBytesLeft());
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testReadIntPastEndOfArray() {
		ByteArrayReader reader = new ByteArrayReader(DEADBEEF);
		reader.skip(1);
		reader.readInt();
	}

	@Test
	public void testReadFloat() {
		ByteArrayReader reader = new ByteArrayReader(DEADBEEF);
		Assert.assertEquals(DEADBEEF_AS_FLOAT, reader.readFloat(), TestUtil.EPSILON);
		Assert.assertEquals(0, reader.getBytesLeft());
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testReadFloatPastEndOfArray() {
		ByteArrayReader reader = new ByteArrayReader(DEADBEEF);
		reader.skip(1);
		reader.readFloat();
	}

	@Test
	public void testReadBitField() {
		ByteArrayReader reader = new ByteArrayReader(DEADBEEF);
		reader.skip(DEADBEEF.length - 1);
		BitField field = reader.readBitField(SEVEN_BITS.length);
		Assert.assertEquals("V1 V2 V3 V4 V6", field.listActiveBits(SEVEN_BITS));
		Assert.assertEquals(0, reader.getBytesLeft());
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testReadBitFieldPastEndOfArray() {
		ByteArrayReader reader = new ByteArrayReader(DEADBEEF);
		reader.skip(DEADBEEF.length - 1);
		reader.readBitField(EIGHT_BITS.length);
	}

	@Test
	public void testReadUsAsciiString() {
		ByteArrayReader reader = new ByteArrayReader(US_ASCII_STRING);
		Assert.assertEquals("Hi!", reader.readUsAsciiString());
		Assert.assertEquals(0, reader.getBytesLeft());
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testReadUsAsciiStringPastEndOfArray() {
		new ByteArrayReader(US_ASCII_STRING_PAST_END).readUsAsciiString();
	}

	@Test
	public void testReadUtf16LeString() {
		ByteArrayReader reader = new ByteArrayReader(UTF_16_STRING);
		TestUtil.assertToStringEquals("Hi!", reader.readUtf16LeString());
		Assert.assertEquals(0, reader.getBytesLeft());
	}

	@Test
	public void testReadUtf16LeStringEarlyNull() {
		ByteArrayReader reader = new ByteArrayReader(UTF_16_STRING_EARLY_NULL);
		TestUtil.assertToStringEquals("H", reader.readUtf16LeString());
		Assert.assertEquals(0, reader.getBytesLeft());
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testReadUtf16LeStringPastEndOfArray() {
		new ByteArrayReader(UTF_16_STRING_PAST_END).readUtf16LeString();
	}
}
