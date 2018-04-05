package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.util.BoolState;

/**
 * This is a custom-rendered mesh in the game world. These are typically
 * inserted by scripts for non-interactive objects.
 * @author dhleong
 */
public class ArtemisMesh extends BaseArtemisOrientable {
	private float mRollDelta = Float.NaN;
	private float mPitchDelta = Float.NaN;
	private float mHeadingDelta = Float.NaN;
    private CharSequence mMesh;
    private CharSequence mTex;
    private float mPushRadius = Float.NaN;
    private BoolState mBlockFire = BoolState.UNKNOWN;
    private float mScale = Float.NaN;
    private float mRed = Float.NaN;
    private float mGreen = Float.NaN;
    private float mBlue = Float.NaN;
    private float mShieldsFront = Float.NaN;
    private float mShieldsRear = Float.NaN;

    public ArtemisMesh(int objId) {
        super(objId);
    }

    @Override
    public ObjectType getType() {
        return ObjectType.GENERIC_MESH;
    }

    public float getRollDelta() {
    	return mRollDelta;
    }

    public void setRollDelta(float rollDelta) {
    	mRollDelta = rollDelta;
    }

    public float getPitchDelta() {
    	return mPitchDelta;
    }

    public void setPitchDelta(float pitchDelta) {
    	mPitchDelta = pitchDelta;
    }

    public float getHeadingDelta() {
    	return mHeadingDelta;
    }

    public void setHeadingDelta(float headingDelta) {
    	mHeadingDelta = headingDelta;
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

    public float getPushRadius() {
    	return mPushRadius;
    }

    public void setPushRadius(float pushRadius) {
    	mPushRadius = pushRadius;
    }

    public BoolState getBlockFire() {
    	return mBlockFire;
    }

    public void setBlockFire(BoolState blockFire) {
    	mBlockFire = blockFire;
    }

    public float getScale() {
    	return mScale;
    }

    @Override
    public float getScale(Context ctx) {
    	return Float.isNaN(mScale) ? super.getScale(ctx) : mScale;
    }

    public void setScale(float scale) {
    	mScale = scale;
    }

    /**
     * The red channel value for the color.
     * Unspecified: Float.NaN
     */
    public float getRed() {
    	return mRed;
    }

    public void setRed(float red) {
    	mRed = red;
    }

    /**
     * The green channel value for the color.
     * Unspecified: Float.NaN
     */
    public float getGreen() {
    	return mGreen;
    }

    public void setGreen(float green) {
    	mGreen = green;
    }

    /**
     * The blue channel value for the color.
     * Unspecified: Float.NaN
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
     * Unspecified: Float.NaN
     */
    public float getShieldsFront() {
        return mShieldsFront;
    }
    
    /**
     * Returns the strength of the mesh's aft shields. These are supposedly
     * "fake" shields, since meshes can't actually be targeted.
     * Unspecified: Float.NaN
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
    public void updateFrom(ArtemisObject other) {
        super.updateFrom(other);

        if (other instanceof ArtemisMesh) {
            ArtemisMesh m = (ArtemisMesh) other;

            if (!Float.isNaN(m.mRollDelta)) {
            	mRollDelta = m.mRollDelta;
            }

            if (!Float.isNaN(m.mPitchDelta)) {
            	mPitchDelta = m.mPitchDelta;
            }

            if (!Float.isNaN(m.mHeadingDelta)) {
            	mHeadingDelta = m.mHeadingDelta;
            }

            if (m.mMesh != null) {
            	mMesh = m.mMesh;
            }

            if (m.mTex != null) {
            	mTex = m.mTex;
            }

            if (!Float.isNaN(m.mPushRadius)) {
            	mPushRadius = m.mPushRadius;
            }

            if (BoolState.isKnown(m.mBlockFire)) {
            	mBlockFire = m.mBlockFire;
            }

            if (!Float.isNaN(m.mScale)) {
            	mScale = m.mScale;
            }

            if (!Float.isNaN(m.mRed)) {
            	mRed = m.mRed;
            }

            if (!Float.isNaN(m.mGreen)) {
            	mGreen = m.mGreen;
            }

            if (!Float.isNaN(m.mBlue)) {
            	mBlue = m.mBlue;
            }

            if (!Float.isNaN(m.mShieldsFront)) {
                mShieldsFront = m.mShieldsFront;
            }
            
            if (!Float.isNaN(m.mShieldsRear)) {
                mShieldsRear = m.mShieldsRear;
            }
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props) {
    	super.appendObjectProps(props);
    	putProp(props, "Roll delta", mRollDelta);
    	putProp(props, "Pitch delta", mPitchDelta);
    	putProp(props, "Heading delta", mHeadingDelta);
    	putProp(props, "Mesh", mMesh);
    	putProp(props, "Texture", mTex);
    	putProp(props, "Push radius", mPushRadius);
    	putProp(props, "Block fire", mBlockFire);
    	putProp(props, "Scale", mScale);
    	putProp(props, "Red", mRed);
    	putProp(props, "Green", mGreen);
    	putProp(props, "Blue", mBlue);
    	putProp(props, "Shields: fore", mShieldsFront);
    	putProp(props, "Shields: aft", mShieldsRear);
    }
}