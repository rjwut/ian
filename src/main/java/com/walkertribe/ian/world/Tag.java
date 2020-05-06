package com.walkertribe.ian.world;

import com.walkertribe.ian.iface.PacketReader;
import com.walkertribe.ian.iface.PacketWriter;
import com.walkertribe.ian.util.Util;

/**
 * A tag that can be attached to objects.
 * @author rjwut
 */
public class Tag {
    private static final byte[] UNKNOWN = new byte[] { 0, 0, 0, 0 };

    private byte[] mUnknown;
    private CharSequence mTagger;
    private CharSequence mDate;

    /**
     * Creates a new Tag with the given tagger and date.
     */
    public Tag(CharSequence tagger, CharSequence date) {
        if (Util.isBlank(tagger)) {
            throw new IllegalArgumentException("You must provide a tagger name");
        }

        if (Util.isBlank(date)) {
            throw new IllegalArgumentException("You must provide a date");
        }

        mUnknown = UNKNOWN;
        mTagger = tagger;
        mDate = date;
    }

    /**
     * Reads a Tag from a PacketReader.
     */
    public Tag(PacketReader reader) {
        mUnknown = reader.readBytes(4);
        mTagger = reader.readString();
        mDate = reader.readString();
    }

    /**
     * Returns the name of the ship that tagged the object.
     */
    public CharSequence getTagger() {
        return mTagger;
    }

    /**
     * Returns the date that the object was tagged.
     */
    public CharSequence getDate() {
        return mDate;
    }

    /**
     * Writes this Tag to the given PacketWriter.
     */
    public void write(PacketWriter writer) {
        writer.writeBytes(mUnknown).writeString(mTagger).writeString(mDate);
    }

    @Override
    public String toString() {
        return "Tagged by " + mTagger + " on " + mDate;
    }
}
