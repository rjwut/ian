package com.walkertribe.ian.world;

import com.walkertribe.ian.enums.ObjectType;

public class ArtemisNebula extends BaseArtemisObject {
    private boolean hasColor;
    private byte a;
    private byte r;
    private byte g;
    private byte b;

	public ArtemisNebula(int objId) {
        super(objId);
        setName("NEBULA");
    }

	@Override
	public ObjectType getType() {
		return ObjectType.NEBULA;
	}

    public boolean hasColor() {
    	return hasColor;
    }

    /**
     * The color of the nebula. This is specified as an ARGB int value. To
     * specify each channel separately, use the setARGB() method.
     * Unspecified: 0
     */
    public int getColor() {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public byte getAlpha() {
    	return a;
    }

    public byte getRed() {
    	return r;
    }

    public byte getGreen() {
    	return g;
    }

    public byte getBlue() {
    	return b;
    }

    /**
     * Sets the color of the nebula, specifying each channel as a value between
     * 0 and 255.
     */
    public void setARGB(byte a, byte r, byte g, byte b) {
    	this.a = a;
    	this.r = r;
    	this.g = g;
    	this.b = b;
    	hasColor = true;
    }
   
    /**
     * Sets the color that will be used to render this object on sensor views,
     * specifying each channel as a value between 0 and 1.
     */
    public void setARGB(float a, float r, float g, float b) {
        setARGB(
            (byte) (255 * a),
            (byte) (255 * r), 
            (byte) (255 * g), 
            (byte) (255 * b)
        );
    }
}