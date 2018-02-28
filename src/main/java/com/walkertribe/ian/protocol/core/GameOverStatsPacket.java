package com.walkertribe.ian.protocol.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.walkertribe.ian.enums.Origin;
import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.protocol.Packet;
import com.walkertribe.ian.protocol.core.SimpleEventPacket.SubType;
import com.walkertribe.ian.util.Util;

/**
 * Provides a column of endgame statistics. 
 * @author rjwut
 */
@Packet(origin = Origin.SERVER, type = CorePacketType.SIMPLE_EVENT, subtype = SubType.GAME_OVER_STATS)
public class GameOverStatsPacket extends SimpleEventPacket implements
		Iterable<GameOverStatsPacket.Row> {
    private static final byte DELIMITER = 0x00;
    private static final byte END_MARKER = (byte) 0xce;

	private byte columnIndex;
	private List<Row> rows = new LinkedList<Row>();

    public GameOverStatsPacket(byte columnIndex) {
    	this.columnIndex = columnIndex;
    }

	public GameOverStatsPacket(PacketReader reader) {
    	super(reader);
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

	/**
	 * The index of the column (0-based).
	 */
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

    /**
     * Adds a row of data to this column.
     */
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

	/**
	 * Represents a single row of data to display in the column.
	 * @author rjwut
	 */
	public static class Row {
		private CharSequence label;
		private int value;

		private Row(CharSequence label, int value) {
			if (Util.isBlank(label)) {
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