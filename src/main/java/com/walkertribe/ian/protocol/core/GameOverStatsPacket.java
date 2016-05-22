package com.walkertribe.ian.protocol.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.walkertribe.ian.enums.ConnectionType;
import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;
import com.walkertribe.ian.protocol.BaseArtemisPacket;
import com.walkertribe.ian.protocol.UnexpectedTypeException;

public class GameOverStatsPacket extends BaseArtemisPacket implements
		Iterable<GameOverStatsPacket.Row> {
    private static final int TYPE = 0xf754c8fe;
    private static final byte MSG_TYPE = 0x15;
    private static final byte DELIMITER = 0x00;
    private static final byte END_MARKER = (byte) 0xce;

	public static void register(PacketFactoryRegistry registry) {
		registry.register(ConnectionType.SERVER, TYPE, MSG_TYPE,
				new PacketFactory() {
			@Override
			public Class<? extends ArtemisPacket> getFactoryClass() {
				return GameOverStatsPacket.class;
			}

			@Override
			public ArtemisPacket build(PacketReader reader)
					throws ArtemisPacketException {
				return new GameOverStatsPacket(reader);
			}
		});
	}

	private byte columnIndex;
	private List<Row> rows = new LinkedList<Row>();

	private GameOverStatsPacket(PacketReader reader) {
    	super(ConnectionType.SERVER, TYPE);
        int subtype = reader.readInt();

        if (subtype != MSG_TYPE) {
			throw new UnexpectedTypeException(subtype, MSG_TYPE);
        }

        columnIndex = reader.readByte();

        do {
        	if (reader.readByte() == END_MARKER) {
        		break;
        	}

        	int value = reader.readInt();
        	String label = reader.readString();
        	rows.add(new Row(label, value));
        } while (true);
	}

    public GameOverStatsPacket(byte columnIndex) {
    	super(ConnectionType.SERVER, TYPE);
    	this.columnIndex = columnIndex;
    }

    public byte getColumnIndex() {
    	return columnIndex;
    }

    @Override
	public Iterator<Row> iterator() {
		return rows.iterator();
	}

    public void addRow(String label, int value) {
    	rows.add(new Row(label, value));
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		writer.writeInt(MSG_TYPE);
		writer.writeByte(columnIndex);

		for (Row row : rows) {
			writer
				.writeByte(DELIMITER)
				.writeInt(row.getValue())
				.writeString(row.getLabel());
		}

		writer.writeByte(END_MARKER);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		for (Row row : rows) {
			b	.append("\n\t")
				.append(row.getLabel())
				.append(": ")
				.append(row.getValue());
		}
	}


	public static class Row {
		private String label;
		private int value;

		private Row(String label, int value) {
			this.label = label;
			this.value = value;
		}

		public String getLabel() {
			return label;
		}

		public int getValue() {
			return value;
		}
	}
}