package com.walkertribe.ian.enums;

import java.util.LinkedHashSet;
import java.util.Set;

public enum CommFilter {
	ALERT,
	SIDE,
	STATUS,
	PLAYER,
	BASE,
	ENEMY,
	FRIEND;

	/**
	 * Reads a bitmap and returns a corresponding Set of CommFilters
	 */
	public static Set<CommFilter> fromInt(int value) {
		Set<CommFilter> filters = new LinkedHashSet<CommFilter>();

		for (CommFilter filter : values()) {
			if ((value & filter.toInt()) != 0) {
				filters.add(filter);
			}
		}

		return filters;
	}

	/**
	 * Returns a bitmap corresponding to the given Set of CommFilters.
	 */
	public static int toInt(Set<CommFilter> filters) {
		int value = 0;

		for (CommFilter filter : filters) {
			value |= filter.toInt();
		}

		return value;
	}

	/**
	 * Returns the int value of this CommFilter.
	 */
	public int toInt() {
		return 1 << ordinal();
	}
}
