package com.walkertribe.ian.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Stores Artemis's UTF-16 null-terminated strings, preserving any "garbage" data that may follow
 * the null. This ensures that string reading and writing is symmetrical; in other words, that the
 * bytes written always exactly match the bytes read.
 * @author rjwut
 */
public class NullTerminatedString implements CharSequence {
	/**
	 * Converts a List of NullTerminatedStrings to a List of Strings.
	 */
	public static List<String> toStrings(List<NullTerminatedString> ntsList) {
		List<String> strings = new LinkedList<String>();

		for (NullTerminatedString nts : ntsList) {
			strings.add(nts.toString());
		}

		return strings;
	}

	/**
	 * Converts a List of Strings to a List of NullTerminatedStrings.
	 */
	public static List<NullTerminatedString> toNTStrings(List<String> strings) {
		List<NullTerminatedString> ntsList = new LinkedList<NullTerminatedString>();

		for (String str : strings) {
			ntsList.add(new NullTerminatedString(str));
		}

		return ntsList;
	}

	private String str;
	private byte[] garbage;

	/**
	 * Reads a string from the given byte array.
	 */
	public NullTerminatedString(byte[] bytes) {
		int i = 0;

		// find the null
		for ( ; i < bytes.length; i += 2) {
			if (bytes[i] == 0 && bytes[i + 1] == 0) {
				break;
			}
		}

		if (i == bytes.length) {
			throw new IllegalArgumentException("No null found for null-terminated string");
		}

		str = new String(Arrays.copyOfRange(bytes, 0, i), Util.UTF16LE);

		if (i < bytes.length - 2) {
			garbage = Arrays.copyOfRange(bytes, i, bytes.length); // null was early
		} else {
			garbage = new byte[0];
		}
	}

	/**
	 * Converts a regular String into a NullTerminatedString.
	 */
	public NullTerminatedString(String str) {
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException("NullTerminatedString must have at least one character");
		}

		this.str = str;
		garbage = new byte[0];
	}

	/**
	 * Returns the "garbage" bytes. This returns an empty array if there were no garbage bytes.
	 */
	public byte[] getGarbage() {
		return garbage;
	}

	@Override
	public String toString() {
		return str;
	}

	/**
	 * Returns the length of this NullTerminatedString in characters, excluding the null and any
	 * garbage data.
	 */
	@Override
	public int length() {
		return str.length();
	}

	/**
	 * Returns the length of this string in double-byte characters, including the null and any
	 * garbage data.
	 */
	public int fullLength() {
		return str.length() + garbage.length / 2 + 1;
	}

	@Override
	public char charAt(int index) {
		return str.charAt(index);
	}

	@Override
	public CharSequence subSequence(int beginIndex, int endIndex) {
		return str.subSequence(beginIndex, endIndex);
	}

	public CharSequence replace(char oldChar, char newChar) {
		NullTerminatedString newString = new NullTerminatedString(str.replace(oldChar, newChar));
		newString.garbage = garbage;
		return newString;
	}
}
