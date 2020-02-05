package com.walkertribe.ian.util;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class NullTerminatedStringTest {
    private static final byte[] HI_BYTES = new byte[] {
        0x48, 0x00, 0x69, 0x00, 0x21, 0x00, 0x00, 0x00, 0x41, 0x00
    };
    private static final byte[] EMPTY_STRING_BYTES = new byte[] {
        0x00, 0x00
    };
    private static final byte[] NO_NULL_BYTES_1 = new byte[] {
        0x48, 0x00, 0x69, 0x00, 0x21, 0x00
    };
    private static final byte[] NO_NULL_BYTES_2 = new byte[] {
        0x48, 0x00, 0x69, 0x00, 0x21, 0x00, 0x00, 0x01
    };
    private static final byte[] ZERO_BYTES = new byte[] {};

    @Test
    public void testByteArrayConstructor() {
        test(new NullTerminatedString(HI_BYTES), "Hi!", "A");
    }

    @Test
    public void testEmptyStringByteArrayConstructor() {
        test(new NullTerminatedString(EMPTY_STRING_BYTES), "", "");
    }

    @Test
    public void testStringConstructor() {
        test(new NullTerminatedString("Hi!"), "Hi!", "");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNoNull1ByteArrayConstructor() {
        new NullTerminatedString(NO_NULL_BYTES_1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNoNull2ByteArrayConstructor() {
        new NullTerminatedString(NO_NULL_BYTES_2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testZeroLengthByteArrayConstructor() {
        new NullTerminatedString(ZERO_BYTES);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullStringConstructor() {
        new NullTerminatedString((String) null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testZeroLengthStringConstructor() {
        new NullTerminatedString("");
    }

    @Test
    public void testEqualsAndHashcode() {
        NullTerminatedString nts = new NullTerminatedString("Hi!");
        Assert.assertTrue(nts.equals(nts));
        Assert.assertTrue(nts.equals(new NullTerminatedString("Hi!")));
        Assert.assertFalse(nts.equals(new Object()));
        Assert.assertFalse(nts.equals(null));
        Assert.assertEquals("Hi!".hashCode(), nts.hashCode());
    }

    @Test
    public void testToStrings() {
        List<NullTerminatedString> ntStrings = new LinkedList<>();

        for (int i = 0; i < 4; i++) {
            ntStrings.add(i % 2 == 0 ? new NullTerminatedString("Test" + i) : null);
        }

        List<String> strings = NullTerminatedString.toStrings(ntStrings);

        for (int i = 0; i < 4; i++) {
            Assert.assertEquals(i % 2 == 0 ? "Test" + i : null, strings.get(i));
        }
    }

    @Test
    public void testToNTStrings() {
        List<String> strings = new LinkedList<>();

        for (int i = 0; i < 4; i++) {
            strings.add(i % 2 == 0 ? "Test" + i : null);
        }

        List<NullTerminatedString> ntStrings = NullTerminatedString.toNTStrings(strings);

        for (int i = 0; i < 4; i++) {
            Assert.assertEquals(i % 2 == 0 ? new NullTerminatedString("Test" + i) : null,
                ntStrings.get(i));
        }
    }

    private void test(NullTerminatedString str, String text, String garbage) {
        int len = text.length();
        Assert.assertEquals(len, str.length());
        Assert.assertEquals(len + garbage.length() + 1, str.fullLength());
        Assert.assertEquals(text, str.toString());
        Assert.assertEquals(garbage, new String(str.getGarbage(), Util.UTF16LE));

        for (int i = 0; i < len; i++) {
            char curChar = str.charAt(i);
            Assert.assertEquals(text.charAt(i), curChar);
        }

        if (len > 0) {
            testSubAndReplace(str, text);
        }
    }

    private void testSubAndReplace(NullTerminatedString str, String text) {
        int len = text.length();
        char oldChar = str.charAt(0);
        CharSequence replaced = str.replace(oldChar, '~');

        for (int i = 0; i < len; i++) {
            char curChar = str.charAt(i);
            char replacedChar = replaced.charAt(i);
            Assert.assertEquals(curChar == oldChar ? '~' : curChar, replacedChar);
        }

        Assert.assertEquals(text.subSequence(0, len - 1), str.subSequence(0, len - 1));
    }
}
