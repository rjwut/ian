package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.enums.Upgrade;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Sent by a client that wishes to activate an upgrade.
 * @author rjwut
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.ACTIVATE_UPGRADE)
public class ActivateUpgradePacket extends ValueIntPacket {
	public ActivateUpgradePacket(Upgrade upgrade) {
		super(SubType.ACTIVATE_UPGRADE, upgrade != null ? upgrade.getActivationIndex() : -1);

		if (upgrade == null) {
			throw new IllegalArgumentException("You must provide an upgrade");
		}

		if (upgrade.getActivatedBy() == null) {
			throw new IllegalArgumentException("Can't activate " + upgrade);
		}
	}

	public ActivateUpgradePacket(PacketReader reader) {
		super(reader);
	}

	/**
	 * The Upgrade to activate
	 */
	public Upgrade getUpgrade() {
		return Upgrade.fromIndex(mArg);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(getUpgrade());
	}
}
