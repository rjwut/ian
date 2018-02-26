package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.MainScreenView;
import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.ValueIntPacket.SubType;

/**
 * Set what to show on the main screen.
 * @author dhleong
 */
@Packet(origin = Origin.CLIENT, type = CorePacketType.VALUE_INT, subtype = SubType.MAIN_SCREEN)
public class SetMainScreenPacket extends ValueIntPacket {
	/**
	 * @param screen The enum value representing the desired view
	 */
    public SetMainScreenPacket(MainScreenView screen) {
        super(screen != null ? screen.ordinal() : -1);

        if (screen == null) {
        	throw new IllegalArgumentException("You must specify a view");
        }
    }

    public SetMainScreenPacket(PacketReader reader) {
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