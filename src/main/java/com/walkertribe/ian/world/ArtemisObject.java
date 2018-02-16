package com.walkertribe.ian.world;

import java.util.SortedMap;

import com.walkertribe.ian.Context;
import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.model.Model;

/**
 * <p>
 * This interface represents information about an object in the game world. It
 * may contain all the information known about that object, or just updates.
 * Every object has the following properties:
 * </p>
 * <ul>
 * <li>an ID</li>
 * <li>a type</li>
 * <li>a position (x, y, z)</li>
 * </ul>
 * <p>
 * Many objects also have a name, but not all of them do, and the name is not
 * guaranteed to be unique. However, any one update is only guaranteed to
 * specify the ID.
 * </p>
 * <h2>Unspecified properties vs. unknown properties</h2>
 * <p>
 * A property is unspecified if no value has been given for it. Since object
 * update packets typically contain values for properties which have changed,
 * other properties will be unspecified. To avoid instantiating a lot of
 * objects, special values are used to indicate whether a primitive property is
 * unspecified. The documentation for each property's accessor method will tell
 * you what that value is. The "unspecified" value depends on the property's
 * type and what its permissible values are:
 * </p>
 * <dl>
 * <dt>BoolState</dt>
 * <dd>BoolState.UNKNOWN</dd>
 * <dt>Other Objects</dt>
 * <dd>null</dd>
 * <dt>Numeric primitives</dt>
 * <dd>-1, or the type's MIN_VALUE if -1 is a permissible value
 * 		for that property</dd>
 * </dl>
 * <p>
 * An unknown property is one whose purpose is currently unknown. It may have a
 * specified value, but we don't know what that value means. IAN is capable of
 * tracking unknown property values, but this capability is really only useful
 * for people who are trying to determine what these properties mean.
 * </p>
 * <h2>Updating objects</h2>
 * <p>
 * The ObjectUpdatePacket produces objects which implement this interface.
 * These objects will contain only the property values that were updated by
 * that packet; all other values will be unspecified. You can use the
 * updateFrom() method to transfer all specified properties from one object to
 * another; this allows you to keep around a single instance that always has the
 * latest known state for that world object.
 * </p>
 * <h2>Object positions</h2>
 * <p>
 * A sector is a three-dimensional rectangular prism. From the perspective of a
 * ship with a heading of 0 degrees, the X axis runs from port to starboard, the
 * Y axis runs up and down, and the Z axis runs bow to stern. The boundaries of
 * the sector are (0, 500, 0) [top northeast corner] to (100000, -500, 100000)
 * [bottom southwest corner]. However, some objects, such as asteroids and
 * nebulae, may lie outside these bounds.
 * </p>
 * @author dhleong
 */
public interface ArtemisObject {
	/**
	 * The object's unique identifier. This property should always be specified.
	 */
    public int getId();

    /**
     * The object's type.
     * Unspecified: null
     */
    public ObjectType getType();

    /**
     * The object's name.
     * Unspecified: null
     */
    public CharSequence getName();

    /**
	 * The object's position along the X-axis.
	 * Unspecified: Float.MIN_VALUE
	 */
    public abstract float getX();
    public abstract void setX(float x);

    /**
	 * The object's position along the Y-axis
	 * Unspecified: Float.MIN_VALUE
	 */
    public abstract float getY();
    public abstract void setY(float y);

    /**
	 * The object's position along the Z-axis
	 * Unspecified: Float.MIN_VALUE
	 */
    public abstract float getZ();
    public abstract void setZ(float z);

    /**
     * Returns the Model object for this ArtemisObject, using the given
     * Context, or null if one cannot be provided.
     */
    public abstract Model getModel(Context ctx);

    /**
     * Returns the base scale factor for the model representing this object (as
     * returned by getModel()), or 0.0 if one cannot be provided.
     */
    public abstract float getScale(Context ctx);

    /**
     * Returns a SortedMap containing the values for properties whose purpose is
     * currently unknown. This is useful for debugging.
     */
    public SortedMap<String, byte[]> getUnknownProps();
    public void setUnknownProps(SortedMap<String, byte[]> unknownProps);

    /**
     * Updates this object's properties to match any updates provided by the
     * given object. If any property of the given object is unspecified, this
     * object's corresponding property will not be updated.
     */
    public void updateFrom(ArtemisObject other, Context ctx);

    /**
     * Returns a SortedMap containing this object's properties. If
     * includeUnspecified is true, all properties will be included in the map,
     * even if they're unspecified. Otherwise, only specified properties will be
     * included. Note that unknown properties that have not been specified will
     * never be included, even if includeUnspecified is true.
     */
    public SortedMap<String, Object> getProps(boolean includeUnspecified);
}