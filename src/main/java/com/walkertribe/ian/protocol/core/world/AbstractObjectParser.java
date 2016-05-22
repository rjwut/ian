package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.enums.ObjectType;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.world.ArtemisObject;

public abstract class AbstractObjectParser implements ObjectParser {
	protected abstract ArtemisObject parseImpl(PacketReader reader);

	protected ObjectType objectType;

	protected AbstractObjectParser(ObjectType objectType) {
		this.objectType = objectType;
	}

	@Override
	public final ArtemisObject parse(PacketReader reader) {
		byte typeId = reader.hasMore() ? reader.readByte() : 0;

		if (typeId == 0) {
			return null; // no more objects to parse
		}

		ObjectType parsedObjectType = ObjectType.fromId(typeId);

		if (objectType != parsedObjectType) {
			throw new IllegalStateException("Expected to parse " + objectType +
					" but received " + parsedObjectType);
		}

		reader.startObject(objectType, getBits());
		ArtemisObject obj = parseImpl(reader);
		obj.setUnknownProps(reader.getUnknownObjectProps());
		return obj;
	}

	@Override
	public void appendDetail(ArtemisObject obj, StringBuilder b) {
		b.append("\nObject #").append(obj.getId()).append(obj);
	}
}