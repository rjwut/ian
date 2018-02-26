package com.walkertribe.ian.protocol.core.helm;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.CorePacketType;
import com.walkertribe.ian.protocol.core.ValueIntPacket;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Request to dock. This can be issued by most player ships to request docking
 * with the nearest base. For single-seat craft, it is a request to dock with
 * the mothership.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.REQUEST_DOCK)
public class HelmRequestDockPacket extends ValueIntPacket {
	public HelmRequestDockPacket() {
        super(0);
    }

	public HelmRequestDockPacket(PacketReader reader) {
        super(reader);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		// do nothing
	}
}