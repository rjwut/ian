package com.walkertribe.ian.world;

/**
 * An interface for objects which have an orientation: heading, pitch and roll.
 * Technically, all objects have these properties, but only objects which
 * implement this interface have them exposed in the Artemis protocol.
 * @author rjwut
 */
public interface ArtemisOrientable extends ArtemisObject {
    /**
     * The direction the object is facing on the X-Z plane. This is expressed as
     * a value from negative pi to pi. A value of pi corresponds to a heading of
     * 0 degrees. The ship turns to port as the value decreases. A value of 0
     * corresponds to a heading of 180 degrees.
	 * Unspecified: Float.NaN
     */
    public abstract float getHeading();
    public abstract void setHeading(float heading);

    /**
     * The angle between the X-Z plane and the front of the object, in degrees.
     * A positive value means the object is pointed "down", while a negative
     * value means the object is pointed "up".
	 * Unspecified: Float.NaN
     */
    public abstract float getPitch();
    public abstract void setPitch(float pitch);

    /**
     * The angle that the object has rolled towards the port (positive) or
     * starboard (negative) side, in degrees.
	 * Unspecified: Float.NaN
     */
    public abstract float getRoll();
    public abstract void setRoll(float roll);

}