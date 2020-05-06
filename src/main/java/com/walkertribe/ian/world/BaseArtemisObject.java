package com.walkertribe.ian.world;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.model.Model;
import com.walkertribe.ian.util.BoolState;
import com.walkertribe.ian.util.TextUtil;

/**
 * Base implementation for all ArtemisObjects.
 */
public abstract class BaseArtemisObject implements ArtemisObject {
	/**
	 * Puts the given int property into the indicated map, unless its value is
	 * equal to unspecifiedValue.
	 */
	public static void putProp(SortedMap<String, Object> props, String label,
			int value, int unspecifiedValue) {
		if (value == unspecifiedValue) {
			return;
		}

		props.put(label, Integer.valueOf(value));
	}

	/**
	 * Puts the given float property into the indicated map, unless its value
	 * is NaN.
	 */
	public static void putProp(SortedMap<String, Object> props, String label,
			float value) {
		if (Float.isNaN(value)) {
			return;
		}

		props.put(label, Float.valueOf(value));
	}

	/**
	 * Puts the given BoolState property into the indicated map, unless the
	 * given value is null or BoolState.UNKNOWN.
	 */
	public static void putProp(SortedMap<String, Object> props, String label,
			BoolState value) {
		if (!BoolState.isKnown(value)) {
			return;
		}

		props.put(label, value);
	}

	/**
	 * Puts the given Object property into the indicated map, unless the given
	 * value is null.
	 */
	public static void putProp(SortedMap<String, Object> props, String label,
			Object value) {
		if (value == null) {
			return;
		}

		props.put(label, value);
	}

	protected final int mId;
    public CharSequence mName;
    private float mX = Float.NaN;
    private float mY = Float.NaN;
    private float mZ = Float.NaN;
    private CharSequence mRace;
    private CharSequence mArtemisClass;
    private CharSequence mIntelLevel1;
    private CharSequence mIntelLevel2;
    private List<Tag> mTags = new LinkedList<>();
    private SortedMap<String, byte[]> unknownProps;

    public BaseArtemisObject(int objId) {
        mId = objId;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public Model getModel(Context ctx) {
    	ObjectType type = getType();
    	return type != null ? type.getModel(ctx) : null;
    }

    @Override
    public float getScale(Context ctx) {
    	ObjectType type = getType();
    	return type != null ? type.getScale() : 0;
    }

    @Override
    public CharSequence getName() {
        return mName;
    }

    @Override
    public String getNameString() {
        return mName != null ? mName.toString() : null;
    }

    public void setName(CharSequence name) {
    	mName = name;
    }

    @Override
    public float getX() {
        return mX;
    }

    @Override
    public void setX(float mX) {
        this.mX = mX;
    }

    @Override
    public float getY() {
        return mY;
    }

    @Override
    public void setY(float y) {
        mY = y;
    }

    @Override
    public float getZ() {
        return mZ;
    }

    @Override
    public void setZ(float z) {
        mZ = z;
    }

    /**
     * The race of this object, as determined from a science scan.
     * Unspecified: null
     */
    @Override
    public CharSequence getRace() {
    	return mRace;
    }

    public void setRace(CharSequence race) {
    	mRace = race;
    }

    /**
     * The object's class, as determined from a science scan. This property is referred to as
     * "artemisClass" to avoid colliding with Object.getClass().
     * Unspecified: null
     */
    @Override
    public CharSequence getArtemisClass() {
    	return mArtemisClass;
    }

    public void setArtemisClass(CharSequence artemisClass) {
    	mArtemisClass = artemisClass;
    }

    /**
     * Returns the number of times this object can be scanned by one side.
     */
    public int getMaxScans() {
        return 0;
    }

    /**
     * Returns the scan level for this object for the indicated side. In other words, this is the
     * number of times this object has been scanned by that side. Objects which are not scannable
     * always return 1.
     * Unspecified: -1
     */
    public int getScanLevel(int side) {
        return 1;
    }

    /**
     * Sets the scan level for this object for the indicated side.
     */
    public void setScanLevel(int side, int scanLevel) {
        throw new UnsupportedOperationException("Can't scan this object");
    }

    /**
     * The level 1 scan intel for this object.
     * Unspecified: null
     */
    @Override
    public CharSequence getIntelLevel1() {
    	return mIntelLevel1;
    }

    @Override
    public void setIntelLevel1(CharSequence intelLevel1) {
    	mIntelLevel1 = intelLevel1;
    }

    /**
     * The level 2 scan intel for this object.
     * Unspecified: null
     */
    @Override
    public CharSequence getIntelLevel2() {
    	return mIntelLevel2;
    }

    @Override
    public void setIntelLevel2(CharSequence intelLevel2) {
    	mIntelLevel2 = intelLevel2;
    }

    @Override
    public boolean isTagged() {
        return !mTags.isEmpty();
    }

    @Override
    public List<Tag> getTags() {
        return new LinkedList<>(mTags);
    }

    @Override
    public void addTag(Tag tag) {
        mTags.add(tag);
    }

    @Override
    public boolean hasPosition() {
    	return !Float.isNaN(mX) && !Float.isNaN(mZ);
    }

    @Override
    public BoolState getVisibility(int side) {
        return BoolState.TRUE;
    }

    @Override
    public float distance(ArtemisObject obj) {
    	if (!hasPosition() || !obj.hasPosition()) {
    		throw new IllegalStateException("Can't compute distance if both objects don't have a position");
    	}

    	float y0 = obj.getY();
    	y0 = Float.isNaN(y0) ? 0 : y0;
    	float y1 = Float.isNaN(mY) ? 0 : mY;
    	float dX = obj.getX() - mX;
    	float dY = y0 - y1;
    	float dZ = obj.getZ() - mZ;
    	return (float) Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    @Override
    public float distanceIgnoreY(ArtemisObject obj) {
        if (!hasPosition() || !obj.hasPosition()) {
            throw new IllegalStateException("Can't compute distance if both objects don't have a position");
        }

        float dX = obj.getX() - mX;
        float dZ = obj.getZ() - mZ;
        return (float) Math.sqrt(dX * dX + dZ * dZ);
    }

    @Override
    public void updateFrom(ArtemisObject obj) {
		CharSequence name = obj.getName();

		if (name != null) {
            mName = name;
        }

        float x = obj.getX();
        float y = obj.getY();
        float z = obj.getZ();

        if (!Float.isNaN(x)) {
        	mX = x;
        }

        if (!Float.isNaN(y)) {
        	mY = y;
        }

        if (!Float.isNaN(z)) {
        	mZ = z;
        }

        CharSequence str = obj.getRace();

        if (str != null) {
        	setRace(str);
        }

        str = obj.getArtemisClass();

        if (str != null) {
        	setArtemisClass(str);
        }

        str = obj.getIntelLevel1();

        if (str != null) {
        	setIntelLevel1(str);
        }

        str = obj.getIntelLevel2();

        if (str != null) {
        	setIntelLevel2(str);
        }

        BaseArtemisObject cast = (BaseArtemisObject) obj;
        SortedMap<String, byte[]> unknown = cast.getUnknownProps();

        if (unknown != null && !unknown.isEmpty()) {
        	if (unknownProps == null) {
        		unknownProps = new TreeMap<String, byte[]>();
        	}

        	unknownProps.putAll(unknown);
        }
    }

    @Override
    public final SortedMap<String, byte[]> getUnknownProps() {
    	return unknownProps;
    }

    @Override
    public final void setUnknownProps(SortedMap<String, byte[]> unknownProps) {
    	this.unknownProps = unknownProps;
    }

    @Override
    public final byte[] getUnknownProp(int byteNumber, int bitNumber) {
        return unknownProps != null ? unknownProps.get("UNK_" + byteNumber + "_" + bitNumber) : null;
    }

    @Override
    public final void setUnknownProp(int byteNumber, int bitNumber, byte[] bytes) {
        if (unknownProps == null) {
            unknownProps = new TreeMap<>();
        }

        unknownProps.put("UNK_" + byteNumber + "_" + bitNumber, bytes);
    }

    @Override
    public final SortedMap<String, Object> getProps() {
    	SortedMap<String, Object> props = new TreeMap<String, Object>();
    	appendObjectProps(props);
    	return props;
    }

    @Override
    public final String toString() {
    	SortedMap<String, Object> props = getProps();
    	StringBuilder b = new StringBuilder();

    	for (Map.Entry<String, Object> entry : props.entrySet()) {
    		b.append("\n\t").append(entry.getKey()).append(": ");
    		Object value = entry.getValue();

    		if (value instanceof byte[]) { 
    			b.append(TextUtil.byteArrayToHexString((byte[]) value));
    		} else {
    			b.append(value);
    		}
    	}

    	return b.toString();
    }

    /**
     * Appends this object's properties to the given map. Unspecified values
     * should be omitted. Subclasses must always call the superclass's
     * implementation of this method.
     */
	protected void appendObjectProps(SortedMap<String, Object> props) {
    	props.put("ID", Integer.valueOf(mId));
    	putProp(props, "Name", mName);
    	putProp(props, "Object type", getType());
    	putProp(props, "X", mX);
    	putProp(props, "Y", mY);
    	putProp(props, "Z", mZ);
    	putProp(props, "Race", mRace);
    	putProp(props, "Class", mArtemisClass);
    	putProp(props, "Level 1 intel", mIntelLevel1);
    	putProp(props, "Level 2 intel", mIntelLevel2);

    	if (unknownProps != null) {
        	props.putAll(unknownProps);
    	}
    }

	/**
	 * Returns true if this object contains any data.
	 */
	protected boolean hasData() {
		return  mName != null ||
				!Float.isNaN(mX) ||
				!Float.isNaN(mY) ||
				!Float.isNaN(mZ) ||
				mRace != null ||
				mArtemisClass != null ||
				mIntelLevel1 != null ||
				mIntelLevel2 != null ||
				(unknownProps != null && !unknownProps.isEmpty());
	}

    @Override
    public boolean equals(Object other) {
    	if (this == other) {
    		return true;
    	}

    	if (!(other instanceof ArtemisObject)) {
            return false;
        }
        
        return mId == ((ArtemisObject) other).getId();
    }

    @Override
    public int hashCode() {
        return mId;
    }
}