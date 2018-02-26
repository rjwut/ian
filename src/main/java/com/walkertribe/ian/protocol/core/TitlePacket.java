package com.walkertribe.ian.protocol.core;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;

/**
 * Displays a title message on the main screen. This is transmitted in response
 * to a <code>&lt;big_message&gt;</code> tag in a scripted mission.
 * @author rjwut
 */
public class TitlePacket extends BaseArtemisPacket {
	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, CorePacketType.BIG_MESS, new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return TitlePacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new TitlePacket(reader);
			}
		});
	}

	private CharSequence mTitle;
	private CharSequence mSubtitle1;
	private CharSequence mSubtitle2;

	public TitlePacket(CharSequence title, CharSequence subtitle1, CharSequence subtitle2) {
		super(ConnectionType.SERVER, CorePacketType.BIG_MESS);
		mTitle = title;
		mSubtitle1 = subtitle1;
		mSubtitle2 = subtitle2;
	}

	public TitlePacket(PacketReader reader) {
		super(ConnectionType.SERVER, CorePacketType.BIG_MESS);
		mTitle = reader.readString();
		mSubtitle1 = reader.readString();
		mSubtitle2 = reader.readString();
	}

	public CharSequence getTitle() {
		return mTitle;
	}

	public CharSequence getSubtitle1() {
		return mSubtitle1;
	}

	public CharSequence getSubtitle2() {
		return mSubtitle2;
	}

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeString(mTitle);
		writer.writeString(mSubtitle1);
		writer.writeString(mSubtitle2);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		b.append(mTitle).append('\n').append(mSubtitle1).append('\n').append(mSubtitle2);
	}

}
