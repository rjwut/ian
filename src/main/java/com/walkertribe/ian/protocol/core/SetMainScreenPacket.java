package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.MainScreenView;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

/**
 * Set what to show on the main screen.
 * @author dhleong
 */
public class SetMainScreenPacket extends ValueIntPacket {
	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.MAIN_SCREEN, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return SetMainScreenPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new SetMainScreenPacket(reader);
			}
		});
	}

	/**
	 * @param screen The enum value representing the desired view
	 */
    public SetMainScreenPacket(MainScreenView screen) {
        super(SubType.MAIN_SCREEN, screen != null ? screen.ordinal() : -1);

        if (screen == null) {
        	throw new IllegalArgumentException("You must specify a view");
        }
    }

    private SetMainScreenPacket(PacketReader reader) {
    	super(reader);
    }

    /**
     * Returns the requested MainScreenView.
     */
    public MainScreenView getView() {
    	return MainScreenView.values()[mArg];
    }

    @Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(MainScreenView.values()[mArg]);
	}
}