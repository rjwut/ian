package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.model.Model;

/**
 * This is a custom-rendered mesh in the game world. These are typically
 * inserted by scripts for non-interactive objects.
 * @author dhleong
 */
public class ArtemisMesh extends BaseArtemisObject {
    private CharSequence mMesh;
    private CharSequence mTex;
    private float mRed = -1;
    private float mGreen = -1;
    private float mBlue = -1;
    private float mShieldsFront = Float.MIN_VALUE;
    private float mShieldsRear = Float.MIN_VALUE;

    public ArtemisMesh(int objId) {
        super(objId);
    }

    @Override
    public ObjectType getType() {
        return ObjectType.GENERIC_MESH;
    }

    /**
     * The 3D mesh filename
     * Unspecified: null
     */
    public CharSequence getMesh() {
        return mMesh;
    }

    /**
     * Returns the Model object corresponding to the mesh stored in the .dxs
     * file named in the mesh property, or null if the property is unspecified.
     */
    @Override
    public Model getModel(Context ctx) {
    	return ctx.getModel(mMesh.toString());
    }

    public void setMesh(CharSequence path) {
        mMesh = path;
    }

    /**
     * The texture filename
     * Unspecified: null
     */
    public CharSequence getTexture() {
        return mTex;
    }
    
    public void setTexture(CharSequence path) {
        mTex = path;
    }

    /**
     * The red channel value for the color.
     * Unspecified: -1
     */
    public float getRed() {
    	return mRed;
    }

    public void setRed(float red) {
    	mRed = red;
    }

    /**
     * The green channel value for the color.
     * Unspecified: -1
     */
    public float getGreen() {
    	return mGreen;
    }

    public void setGreen(float green) {
    	mGreen = green;
    }

    /**
     * The blue channel value for the color.
     * Unspecified: -1
     */
    public float getBlue() {
    	return mBlue;
    }

    public void setBlue(float blue) {
    	mBlue = blue;
    }

    /**
     * Returns the strength of the mesh's forward shields. These are supposedly
     * "fake" shields, since meshes can't actually be targeted.
     * Unspecified: Float.MIN_VALUE
     */
    public float getShieldsFront() {
        return mShieldsFront;
    }
    
    /**
     * Returns the strength of the mesh's aft shields. These are supposedly
     * "fake" shields, since meshes can't actually be targeted.
     * Unspecified: Float.MIN_VALUE
     */
    public float getShieldsRear() {
        return mShieldsRear;
    }

    /**
     * Sets the strength of the front and aft shields. These are supposedly
     * "fake" shields, since meshes can't actually be targeted.
     */
    public void setFakeShields(float shieldsFront, float shieldsRear) {
        mShieldsFront = shieldsFront;
        mShieldsRear = shieldsRear;
    }

    @Override
    public void updateFrom(ArtemisObject other, Context ctx) {
        super.updateFrom(other, ctx);

        if (other instanceof ArtemisMesh) {
            ArtemisMesh m = (ArtemisMesh) other;

            if (m.mMesh != null) {
            	mMesh = m.mMesh;
            }

            if (m.mTex != null) {
            	mTex = m.mTex;
            }

            if (m.mRed != -1) {
            	mRed = m.mRed;
            }

            if (m.mGreen != -1) {
            	mGreen = m.mGreen;
            }

            if (m.mBlue != -1) {
            	mBlue = m.mBlue;
            }

            if (m.mShieldsFront != Float.MIN_VALUE) {
                mShieldsFront = m.mShieldsFront;
            }
            
            if (m.mShieldsRear != Float.MIN_VALUE) {
                mShieldsRear = m.mShieldsRear;
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props, boolean includeUnspecified) {
    	super.appendObjectProps(props, includeUnspecified);
    	putProp(props, "Mesh", mMesh, includeUnspecified);
    	putProp(props, "Texture", mTex, includeUnspecified);
    	putProp(props, "Red", mRed, -1, includeUnspecified);
    	putProp(props, "Green", mGreen, -1, includeUnspecified);
    	putProp(props, "Blue", mBlue, -1, includeUnspecified);
    	putProp(props, "Shields: fore", mShieldsFront, Float.MIN_VALUE, includeUnspecified);
    	putProp(props, "Shields: aft", mShieldsRear, Float.MIN_VALUE, includeUnspecified);
    }
}