package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.enums.ObjectType;

/**
 * Nebulae
 * @author rjwut
 */
public class ArtemisNebula extends BaseArtemisObject {
    private float mRed = -1;
    private float mGreen = -1;
    private float mBlue = -1;
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
     * Unspecified: -1
     */
    public float getRed() {
    	return mRed;
    }

    public void setRed(float red) {
    	mRed = red;
    }

    /**
     * The green channel value for the color. The color is visible on 3D screens; 2D screens use nebula type.
     * Unspecified: -1
     */
    public float getGreen() {
    	return mGreen;
    }

    public void setGreen(float green) {
    	mGreen = green;
    }

    /**
     * The blue channel value for the color. The color is visible on 3D screens; 2D screens use nebula type.
     * Unspecified: -1
     */
    public float getBlue() {
    	return mBlue;
    }

    public void setBlue(float blue) {
    	mBlue = blue;
    }

    /**
     * The type of nebula this is (1, 2, or 3). This affects the color of the nebula on 2D screens; 3D screens use RGB.
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

            if (n.mRed != -1) {
            	mRed = n.mRed;
            }

            if (n.mGreen != -1) {
            	mGreen = n.mGreen;
            }

            if (n.mBlue != -1) {
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
    	putProp(props, "Red", mRed, -1);
    	putProp(props, "Green", mGreen, -1);
    	putProp(props, "Blue", mBlue, -1);
    	putProp(props, "Nebula type", mNebulaType, -1);
    }
}