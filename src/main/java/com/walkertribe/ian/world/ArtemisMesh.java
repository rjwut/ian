package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.vesseldata.VesselData;

/**
 * This is a custom-rendered mesh in the game world. These are typically
 * inserted by scripts for non-interactive objects.
 * @author dhleong
 */
public class ArtemisMesh extends BaseArtemisObject {
    private String mMesh;
    private String mTex;
    private boolean hasColor;
    private int mColor;
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
    public String getMesh() {
        return mMesh;
    }

    /**
     * Returns the Model2D object corresponding to the mesh stored in the .dxs
     * file named in the mesh property, or null if the property is unspecified.
     * Note that you must invoke VesselData.setArtemisInstallPath() first,
     * before calling this method.
     */
    @Override
    public Model getModel() {
    	return VesselData.getModel(mMesh);
    }

    public void setMesh(String path) {
        mMesh = path;
    }

    /**
     * The texture filename
     * Unspecified: null
     */
    public String getTexture() {
        return mTex;
    }
    
    public void setTexture(String path) {
        mTex = path;
    }

    public boolean hasColor() {
    	return hasColor;
    }

    /**
     * The color that will be used to render this object on sensor views. This
     * is specified as an ARGB int value. To specify each channel separately,
     * use the setARGB() method.
     * Unspecified: 0
     */
    public int getColor() {
        return mColor;
    }

    public int getAlpha() {
    	return (mColor >>> 24) & 0xff;
    }

    public int getRed() {
    	return (mColor >>> 16) & 0xff;
    }

    public int getGreen() {
    	return (mColor >>> 8) & 0xff;
    }

    public int getBlue() {
    	return mColor & 0xff;
    }

    /**
     * Sets the color that will be used to render this object on sensor views,
     * specifying each channel as a value between 0 and 255.
     */
    public void setARGB(int a, int r, int g, int b) {
        mColor = 0;
        mColor |= ((a & 0xff) << 24);
        mColor |= ((r & 0xff) << 16);
        mColor |= ((g & 0xff) << 8);
        mColor |= (b & 0xff);
    	hasColor = true;
    }
    
    /**
     * Sets the color that will be used to render this object on sensor views,
     * specifying each channel as a value between 0 and 1.
     */
    public void setARGB(float a, float r, float g, float b) {
        setARGB(
            (int)(255 * a),
            (int)(255 * r), 
            (int)(255 * g), 
            (int)(255 * b)
        );
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
    public void updateFrom(ArtemisObject other) {
        super.updateFrom(other);
        
        ArtemisMesh m = (ArtemisMesh) other;
        if (m.mShieldsFront != Float.MIN_VALUE) {
            mShieldsFront = m.mShieldsFront;
        }
        
        if (m.mShieldsRear != Float.MIN_VALUE) {
            mShieldsRear = m.mShieldsRear;
        }
    }

    @Override
	public void appendObjectProps(SortedMap<String, Object> props, boolean includeUnspecified) {
    	super.appendObjectProps(props, includeUnspecified);
    	putProp(props, "Mesh", mMesh, includeUnspecified);
    	putProp(props, "Texture", mTex, includeUnspecified);
    	putProp(props, "Color", mColor, 0, includeUnspecified);
    	putProp(props, "Shields: fore", mShieldsFront, Float.MIN_VALUE, includeUnspecified);
    	putProp(props, "Shields: aft", mShieldsRear, Float.MIN_VALUE, includeUnspecified);
    }
}