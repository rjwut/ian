package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.enums.ObjectType;

/**
 * Nebulae
 * @author rjwut
 */
public class ArtemisNebula extends BaseArtemisObject {
    private float mRed = Float.NaN;
    private float mGreen = Float.NaN;
    private float mBlue = Float.NaN;
    private byte mNebulaType = -1;

	public ArtemisNebula(int objId) {
        super(objId);
    }

	@Override
	public ObjectType getType() {
		return ObjectType.NEBULA;
	}

    /**
     * The red channel value for the color. The color is visible on 3D screens; 2D screens use nebula type.
     * Unspecified: Float.NaN
     */
    public float getRed() {
    	return mRed;
    }

    public void setRed(float red) {
    	mRed = red;
    }

    /**
     * The green channel value for the color. The color is visible on 3D screens; 2D screens use nebula type.
     * Unspecified: Float.NaN
     */
    public float getGreen() {
    	return mGreen;
    }

    public void setGreen(float green) {
    	mGreen = green;
    }

    /**
     * The blue channel value for the color. The color is visible on 3D screens; 2D screens use nebula type.
     * Unspecified: Float.NaN
     */
    public float getBlue() {
    	return mBlue;
    }

    public void setBlue(float blue) {
    	mBlue = blue;
    }

    /**
     * The type of nebula this is. This affects the color of the nebula on 2D screens; 3D screens use RGB. The known
     * nebula types are 1, 2, and 3.
     * Unspecified: -1
     */
    public byte getNebulaType() {
    	return mNebulaType;
    }

    public void setNebulaType(byte nebulaType) {
    	mNebulaType = nebulaType;
    }

    @Override
    public void updateFrom(ArtemisObject other) {
        super.updateFrom(other);

        if (other instanceof ArtemisNebula) {
            ArtemisNebula n = (ArtemisNebula) other;

            if (!Float.isNaN(n.mRed)) {
            	mRed = n.mRed;
            }

            if (!Float.isNaN(n.mGreen)) {
            	mGreen = n.mGreen;
            }

            if (!Float.isNaN(n.mBlue)) {
            	mBlue = n.mBlue;
            }

            if (n.mNebulaType != -1) {
            	mNebulaType = n.mNebulaType;
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);
    	putProp(props, "Red", mRed);
    	putProp(props, "Green", mGreen);
    	putProp(props, "Blue", mBlue);
    	putProp(props, "Nebula type", mNebulaType, -1);
    }
}