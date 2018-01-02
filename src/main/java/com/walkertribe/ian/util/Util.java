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
}
