package com.arcsoft.irobot.utils;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;

/**
 *
 * Created by yj2595 on 2018/3/7.
 */

public class StringUtils {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes, int off, int len) {
        char[] hexChars = new char[len * 2];
        for ( int i = 0; i < len; i++ ) {
            int v = bytes[i + off] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static String bytesToHex(byte[] bytes) {
        return bytesToHex(bytes, 0, bytes.length);
    }

    public static String bytesToString(byte[] bytes, int off, int len) {
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) bytes[i + off];
        }
        return new String(chars);
    }
    public static String bytesToString(byte[] bytes) {
        return bytesToString(bytes, 0, bytes.length);
    }

    public static String escaped(@NonNull final String s) {
        StringBuilder b = new StringBuilder(s.length()*2);
        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            switch (ch) {
                case '\r':
                    b.append("\\r");
                    break;
                case '\n':
                    b.append("\\n");
                    break;
                default:
                    b.append((char) ch);
            }
        }
        return b.toString();
    }

    public static String escaped(@NonNull final byte[] s) {
        StringBuilder b = new StringBuilder(s.length*2);
        for (int i = 0; i < s.length; i++) {
            int ch = s[i];
            switch (ch) {
                case '\r':
                    b.append("\\r");
                    break;
                case '\n':
                    b.append("\\n");
                    break;
                default:
                    b.append((char) ch);
            }
        }
        return b.toString();
    }

    /**
     * <p>Checks whether the character is ASCII 7 bit printable.</p>
     *
     * <pre>
     *   CharUtils.isAsciiPrintable('a')  = true
     *   CharUtils.isAsciiPrintable('A')  = true
     *   CharUtils.isAsciiPrintable('3')  = true
     *   CharUtils.isAsciiPrintable('-')  = true
     *   CharUtils.isAsciiPrintable('\n') = false
     *   CharUtils.isAsciiPrintable('&copy;') = false
     * </pre>
     *
     * @param ch  the character to check
     * @return true if between 32 and 126 inclusive
     */
    public static boolean isAsciiPrintable(int ch) {
        return ch >= 32 && ch < 127;
    }

    /**
     * <p>Checks if the string contains only ASCII printable characters.</p>
     *
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     *
     * <pre>
     * StringUtils.isAsciiPrintable(null)     = false
     * StringUtils.isAsciiPrintable("")       = true
     * StringUtils.isAsciiPrintable(" ")      = true
     * StringUtils.isAsciiPrintable("Ceki")   = true
     * StringUtils.isAsciiPrintable("ab2c")   = true
     * StringUtils.isAsciiPrintable("!ab-c~") = true
     * StringUtils.isAsciiPrintable("\u0020") = true
     * StringUtils.isAsciiPrintable("\u0021") = true
     * StringUtils.isAsciiPrintable("\u007e") = true
     * StringUtils.isAsciiPrintable("\u007f") = false
     * StringUtils.isAsciiPrintable("Ceki G\u00fclc\u00fc") = false
     * </pre>
     *
     * @param str the string to check, may be null
     * @return <code>true</code> if every character is in the range
     *  32 thru 126
     * @since 2.1
     */
    public static boolean isAsciiPrintable(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!isAsciiPrintable(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String join(final Iterable<?> data, final String separator) {
        StringBuilder builder = new StringBuilder();
        Iterator<?> iterator = data.iterator();
        if (iterator.hasNext()) {
            builder.append(String.valueOf(iterator.next()));
        }
        while (iterator.hasNext()) {
            builder.append(separator);
            builder.append(String.valueOf(iterator.next()));
        }
        return builder.toString();
    }

    public static String join(final Iterable<?> data, final char separator) {
        StringBuilder builder = new StringBuilder();
        Iterator<?> iterator = data.iterator();
        if (iterator.hasNext()) {
            builder.append(String.valueOf(iterator.next()));
        }
        while (iterator.hasNext()) {
            builder.append(separator);
            builder.append(String.valueOf(iterator.next()));
        }
        return builder.toString();
    }

    public static String join(final float[] data, final String separator) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf(data[0]));
        for (int i = 1; i < data.length; i++) {
            builder.append(separator);
            builder.append(String.valueOf(data[i]));
        }
        return builder.toString();
    }

    public static String join(final float[] data, final char separator) {
        return join(data, String.valueOf(separator));
    }

    public static String join(final double[] data, final String separator) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf(data[0]));
        for (int i = 1; i < data.length; i++) {
            builder.append(separator);
            builder.append(String.valueOf(data[i]));
        }
        return builder.toString();
    }

    public static String join(final double[] data, final char separator) {
        return join(data, String.valueOf(separator));
    }

    public static String timestampedFileName() {
        return timestampedFileName("yyyyMMdd-HHmmss");
    }

    public static String timestampedFileName(@NonNull final String format) {
        return new SimpleDateFormat(format, Locale.ENGLISH).format(System.currentTimeMillis());
    }
}
