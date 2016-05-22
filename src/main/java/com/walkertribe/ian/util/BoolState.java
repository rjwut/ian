package com.walkertribe.ian.util;

/**
 * A tri-state enumeration representing the values TRUE, FALSE and UNKNOWN.
 * @author rjwut
 */
public enum BoolState {
    TRUE, FALSE, UNKNOWN;

    /**
     * Returns true if this BoolState is .TRUE; false otherwise.
     */
    public boolean getBooleanValue() {
        return ordinal() == 0;
    }

    /**
     * Returns .TRUE if the given boolean is true; .FALSE otherwise.
     */
    public static BoolState from(boolean isTrue) {
        return isTrue ? TRUE : FALSE;
    }

    /**
     * Returns .FALSE if state is null or .UNKNOWN; .TRUE otherwise.
     */
    public static boolean isKnown(BoolState state) {
        return state == TRUE || state == FALSE;
    }

    /**
     * Returns true if the given value is .TRUE; otherwise.
     */
    public static boolean safeValue(BoolState value) {
        return value == TRUE;
    }
}