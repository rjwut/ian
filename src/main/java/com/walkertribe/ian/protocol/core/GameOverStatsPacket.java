package com.walkertribe.ian.protocol.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.walkertribe.ian.iface.PacketFactory;
import com.walkertribe.ian.iface.PacketFactoryRegistry;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.ArtemisPacket;
import com.walkertribe.ian.protocol.ArtemisPacketException;

public class GameOverStatsPacket extends SimpleEventPacket implements
		Iterable<GameOverStatsPacket.Row> {
    private static final byte DELIMITER = 0x00;
    private static final byte END_MARKER = (byte) 0xce;

	public static void register(PacketFactoryRegistry registry) {
		register(registry, SubType.GAME_OVER_STATS, new PacketFactory() {
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
    	super(SubType.GAME_OVER_STATS, reader);
        columnIndex = reader.readByte();

        do {
        	if (reader.readByte() == END_MARKER) {
        		break;
        	}

        	int value = reader.readInt();
        	CharSequence label = reader.readString();
        	rows.add(new Row(label, value));
        } while (true);
	}

    public GameOverStatsPacket(byte columnIndex) {
    	super(SubType.GAME_OVER_STATS);
    	this.columnIndex = columnIndex;
    }

    public byte getColumnIndex() {
    	return columnIndex;
    }

    public int getRowCount() {
    	return rows.size();
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
		super.writePayload(writer);
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
		private CharSequence label;
		private int value;

		private Row(CharSequence label, int value) {
			if (label == null || label.length() == 0) {
				throw new IllegalArgumentException("You must provide a label");
			}

			this.label = label;
			this.value = value;
		}

		public CharSequence getLabel() {
			return label;
		}

		public int getValue() {
			return value;
		}
	}
}