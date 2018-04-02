package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.world.ArtemisObject;

/**
 * Abstract implementation of ObjectParser interface. Provides the common
 * object parsing behavior and delegates to the subclass's parseImpl() method
 * to read individual properties.
 * @author rjwut
 */
public abstract class AbstractObjectParser implements ObjectParser {
	protected abstract ArtemisObject parseImpl(PacketReader reader);

	protected ObjectType objectType;

	protected AbstractObjectParser(ObjectType objectType) {
		this.objectType = objectType;
	}

	@Override
	public final ArtemisObject parse(PacketReader reader) {
		reader.skip(1); // type
		reader.startObject(objectType, getBitCount());
		ArtemisObject obj = parseImpl(reader);
		obj.setUnknownProps(reader.getUnknownObjectProps());
		return obj;
	}
}