package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.Upgrade;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

/**
 * Sent by a client that wishes to activate an upgrade.
 * @author rjwut
 */
public class ActivateUpgradePacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.ACTIVATE_UPGRADE, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return ActivateUpgradePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new ActivateUpgradePacket(reader);
			}
		});
	}

	public ActivateUpgradePacket(Upgrade upgrade) {
		super(SubType.ACTIVATE_UPGRADE, upgrade != null ? upgrade.getActivationIndex() : -1);

		if (upgrade == null) {
			throw new IllegalArgumentException("You must provide an upgrade");
		}

		if (upgrade.getActivatedBy() == null) {
			throw new IllegalArgumentException("Can't activate " + upgrade);
		}
	}

	/**
	 * The Upgrade to activate
	 */
	public Upgrade getUpgrade() {
		return Upgrade.fromActivationIndex(mArg);
	}

	private ActivateUpgradePacket(PacketReader reader) {
		super(reader);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(getUpgrade());
	}
}
