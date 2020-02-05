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
	private static final int HEADER_VALUE = -12345;
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
        	byte unknown = reader.readByte();

        	if (unknown == END_MARKER) {
        		break;
        	}

        	int value = reader.readInt();
        	CharSequence label = reader.readString();
        	Row row;

        	if (value == HEADER_VALUE) {
        		row = new Row(unknown, label);
        	} else {
        		row = new Row(unknown, label, value);
        	}

        	rows.add(row);
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
     * Adds a header to this column
     */
    public void addHeader(String label) {
    	rows.add(new Row((byte) 0, label));
    }

    /**
     * Adds a row of data to this column.
     */
    public void addRow(String label, int value) {
    	rows.add(new Row((byte) 0, label, value));
    }

	@Override
	protected void writePayload(PacketWriter writer) {
		super.writePayload(writer);
		writer.writeByte(columnIndex);

		for (Row row : rows) {
			writer
				.writeByte(row.getUnknown())
				.writeInt(row.isHeader() ? HEADER_VALUE : row.getValue())
				.writeString(row.getLabel());
		}

		writer.writeByte(END_MARKER);
	}

	@Override
	protected void appendPacketDetail(StringBuilder b) {
		for (Row row : rows) {
			b.append("\n\t").append(row.getLabel());

			if (!row.isHeader()) {
				b.append(": ").append(row.getValue());
			}
		}
	}

	/**
	 * Represents a single row of data to display in the column.
	 * @author rjwut
	 */
	public static class Row {
		private byte unknown;
		private CharSequence label;
		private boolean header = true;
		private int value;

		/**
		 * Creates a stat row
		 */
		private Row(byte unknown, CharSequence label, int value) {
			this(unknown, label);
			this.value = value;
			this.header = false;
		}

		/**
		 * Creates a header
		 */
		public Row(byte unknown, CharSequence label) {
			if (Util.isBlank(label)) {
				throw new IllegalArgumentException("You must provide a label");
			}

			this.unknown = unknown;
			this.label = label;
		}

		private byte getUnknown() {
			return unknown;
		}

		/**
		 * The label for this header or stat row
		 */
		public CharSequence getLabel() {
			return label;
		}

		/**
		 * Returns true if this object represents a header.
		 */
		public boolean isHeader() {
			return header;
		}

		/**
		 * The value for this stat row
		 */
		public int getValue() {
			return value;
		}

		@Override
		public String toString() {
			return label + ": " + value;
		}
	}
}