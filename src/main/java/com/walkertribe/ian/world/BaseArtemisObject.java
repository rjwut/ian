package com.walkertribe.ian.world;

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
	private static final String UNKNOWN = "UNKNOWN";

	/**
	 * Puts the given int property into the indicated map. If the given value is
	 * equal to unspecifiedValue, then the includeUnspecified value dictates the
	 * behavor of this method. If it is true, the property is recorded in the
	 * map as "UNKNOWN". Otherwise, it is omitted.
	 */
	public static void putProp(SortedMap<String, Object> props, String label,
			int value, int unspecifiedValue, boolean includeUnspecified) {
		if (!includeUnspecified && value == unspecifiedValue) {
			return;
		}

		props.put(label, value != unspecifiedValue ? Integer.valueOf(value) : UNKNOWN);
	}

	/**
	 * Puts the given float property into the indicated map. If the given value
	 * is equal to unspecifiedValue, then the includeUnspecified value dictates
	 * the behavor of this method. If it is true, the property is recorded in
	 * the map as "UNKNOWN". Otherwise, it is omitted.
	 */
	public static void putProp(SortedMap<String, Object> props, String label,
			float value, float unspecifiedValue, boolean includeUnspecified) {
		if (!includeUnspecified && value == unspecifiedValue) {
			return;
		}

		props.put(label, value != unspecifiedValue ? Float.valueOf(value) : UNKNOWN);
	}

	/**
	 * Puts the given BoolState property into the indicated map. If the given
	 * value is null or BoolState.UNKNOWN, then the includeUnspecified value
	 * dictates the behavor of this method. If it is true, the property is
	 * recorded in the map as-is. Otherwise, it is omitted.
	 */
	public static void putProp(SortedMap<String, Object> props, String label,
			BoolState value, boolean includeUnspecified) {
		if (!includeUnspecified && !BoolState.isKnown(value)) {
			return;
		}

		props.put(label, value != null ? value : BoolState.UNKNOWN);
	}

	/**
	 * Puts the given Object property into the indicated map. If the given value
	 * is null, then the includeUnspecified value dictates the behavior of this
	 * method. If it is true, the property is recorded in the map as "UNKNOWN".
	 * Otherwise, it is omitted.
	 */
	public static void putProp(SortedMap<String, Object> props, String label,
			Object value, boolean includeUnspecified) {
		if (!includeUnspecified && value == null) {
			return;
		}

		props.put(label, value != null ? value : UNKNOWN);
	}

	protected final int mId;
    public CharSequence mName;
    private float mX = Float.MIN_VALUE;
    private float mY = Float.MIN_VALUE;
    private float mZ = Float.MIN_VALUE;
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

	@Override
    public void updateFrom(ArtemisObject obj, Context ctx) {
		CharSequence name = obj.getName();

		if (name != null) {
            mName = name;
        }

        float x = obj.getX();
        float y = obj.getY();
        float z = obj.getZ();

        if (x != Float.MIN_VALUE) {
        	mX = x;
        }

        if (y != Float.MIN_VALUE) {
        	mY = y;
        }

        if (z != Float.MIN_VALUE) {
        	mZ = z;
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
    public final SortedMap<String, Object> getProps(boolean includeUnspecified) {
    	SortedMap<String, Object> props = new TreeMap<String, Object>();
    	appendObjectProps(props, includeUnspecified);
    	return props;
    }

    @Override
    public final String toString() {
    	SortedMap<String, Object> props = getProps(false);
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
     * Appends this object's properties to the given map. If includeUnspecified
     * is true, unspecified properties area also included (unless they are also
     * unknown properties). Subclasses must always call the superclass's
     * implementation of this method.
     */
	protected void appendObjectProps(SortedMap<String, Object> props,
			boolean includeUnspecified) {
    	props.put("ID", Integer.valueOf(mId));
    	putProp(props, "Name", mName, includeUnspecified);
    	putProp(props, "Object type", getType(), includeUnspecified);
    	putProp(props, "X", mX, Float.MIN_VALUE, includeUnspecified);
    	putProp(props, "Y", mY, Float.MIN_VALUE, includeUnspecified);
    	putProp(props, "Z", mZ, Float.MIN_VALUE, includeUnspecified);

    	if (unknownProps != null) {
        	props.putAll(unknownProps);
    	}
    }

	/**
	 * Returns true if this object contains any data.
	 */
	protected boolean hasData() {
		return  mName != null ||
				mX != Float.MIN_VALUE ||
				mY != Float.MIN_VALUE ||
				mZ != Float.MIN_VALUE;
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