package com.walkertribe.ian.protocol.core.world;

import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.world.ArtemisObject;

public interface ObjectParser {
	public Enum<?>[] getBits();
	public ArtemisObject parse(PacketReader reader);
	public void write(ArtemisObject obj, PacketWriter writer);
	public void appendDetail(ArtemisObject obj, StringBuilder b);
}
