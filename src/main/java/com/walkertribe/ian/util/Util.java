package com.walkertribe.ian.util;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Set;

/**
 * Miscellaneous unloved stuff that doesn't have a home right now. But don't
 * worry, we love you and will find you a home.
 */
public final class Util {
	public static final Charset US_ASCII = Charset.forName("US-ASCII");
	public static final Charset UTF16LE = Charset.forName("UTF-16LE");
	public static final Charset UTF8 = Charset.forName("UTF-8");

	private Util() {
		// prevent instantiation
	}

	/**
	 * Returns a space-delimited list of the names of the enum values found in
	 * the given Set.
	 */
	public static String enumSetToString(Set<? extends Enum<?>> set) {
    	if (set.isEmpty()) {
    		return "";
    	}

    	StringBuilder b = new StringBuilder();

    	for (Enum<?> val : set) {
    		if (b.length() != 0) {
    			b.append(' ');
    		}

    		b.append(val);
    	}

    	return b.toString();
    }

	/**
	 * Returns true if the given collection contains any of the indicated
	 * objects; false otherwise.
	 */
	public static boolean containsAny(Collection<?> collection,
			Object... objs) {
		for (Object obj : objs) {
			if (collection.contains(obj)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Converts carats (which Artemis uses for line breaks) to newline characters.
	 */
	public static CharSequence caratToNewline(CharSequence str) {
		return replace(str, '^', '\n');
	}

	/**
	 * Converts newline characters to carats (which Artemis uses for line breaks).
	 */
	public static CharSequence newlineToCarat(CharSequence str) {
		return replace(str, '\n', '^');
	}

	/**
	 * Replaces all instances of one character with another in the given CharSequence. This utility
	 * method is provided because replace() doesn't exist on CharSequence, and we need this in
	 * order to properly handle NullTerminatedStrings.
	 */
	private static CharSequence replace(CharSequence str, char oldChar, char newChar) {
		if (str instanceof String) {
			return ((String) str).replace(oldChar, newChar);
		}

		if (str instanceof NullTerminatedString) {
			return ((NullTerminatedString) str).replace(oldChar, newChar);
		}

		// Somebody tossed a different kind of CharSequence our way, so do it manually
		int len = str.length();
		StringBuilder b = new StringBuilder();

		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			b.append(c == oldChar ? newChar : c);
		}

		return b;
	}
}
