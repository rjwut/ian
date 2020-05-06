package com.walkertribe.ian.enums;

/**
 * The two beam targeting modes that can be used by the weapons officer. Auto
 * targeting means that beam weapons will fire at the targeted vessel as often
 * as possible. Manual targeting means that the weapons officer fires by
 * clicking specific points on the targeted vessel via the gun camera.
 * @author rjwut
 */
public enum TargetingMode {
    MANUAL,
	AUTO;

    /**
     * Returns AUTO if isAuto is true, MANUAL otherwise.
     */
    public static TargetingMode get(boolean isAuto) {
        return isAuto ? AUTO : MANUAL;
    }
}