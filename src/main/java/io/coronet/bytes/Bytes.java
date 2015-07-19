package io.coronet.bytes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An immutable byte array.
 */
public abstract class Bytes implements Iterable<Byte>, Comparable<Bytes> {

    private static final Bytes EMPTY = Bytes.wrap(new byte[0]);

    /**
     * @return an empty array of bytes
     */
    public static Bytes empty() {
        return EMPTY;
    }

    /**
     * Creates a new byte array containing the UTF-8 representation of a String.
     *
     * @param string the string
     * @return the UTF-8 representation of the input string
     * @throws NullPointerException if string is null
     */
    public static Bytes from(String string) {
        if (string == null) {
            throw new NullPointerException("string");
        }

        byte[] array = string.getBytes(StandardCharsets.UTF_8);
        return new Slice(array, 0, array.length);
    }

    /**
     * Creates an immutable view of the given byte array without copying the
     * underlying bytes.
     *
     * @param array the array to wrap
     * @return an immutable view of the given array
     * @throws NullPointerException if array is null
     */
    public static Bytes wrap(byte[] array) {
        return new Slice(array, 0, array.length);
    }

    /**
     * Creates an immutable view of a portion of the given byte array without
     * copying the underlying bytes.
     *
     * @param array the array to wrap
     * @param offset the offset of the first relevant byte within the array
     * @param length the number of relevant bytes to wrap
     * @return an immutable slice of the given array
     * @throws NullPointerException if array is null
     * @throws IndexOutOfBoundsException
     *             if offset &lt; 0, length &lt; 0, or offset + length &gt;
     *             array.length
     */
    public static Bytes wrap(byte[] array, int offset, int length) {
        return new Slice(array, offset, length);
    }

    /**
     * @return the length of this array, in bytes
     */
    public abstract int length();

    /**
     * Reads a single element of this byte array.
     *
     * @param index the index of the element to read
     * @return the value of the given element
     * @throws IndexOutOfBoundsException if i &lt; 0 or i &gt;= size()
     */
    public abstract byte get(int index);

    /**
     * Creates a slice of this array.
     *
     * @param offset the offset of the slice (relative to this array)
     * @param length the length of the slice
     * @return a slice of this array
     * @throws IndexOutOfBoundsException
     *             if offset &lt; 0, length &lt; 0, or offset + length &gt;
     *             this.length
     */
    public abstract Bytes slice(int offset, int length);

    /**
     * Adapts this immutable array to look like an immutable {@code ByteBuffer}.
     *
     * @return a new ByteBuffer wrapping this array
     */
    public abstract ByteBuffer asByteBuffer();

    /**
     * Adapts this immutable array to look like an {@code InputStream}.
     *
     * @return a new InputStream wrapping this array
     */
    public abstract InputStream asInputStream();

    /**
     * Writes this byte array to the given {@code OutputStream}.
     *
     * @param stream the stream to write to
     * @throws IOException on error writing to the stream
     */
    public abstract void writeTo(OutputStream stream) throws IOException;

    @Override
    public Iterator<Byte> iterator() {
        return new Iterator<Byte>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                return (i < length());
            }

            @Override
            public Byte next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return get(i++);
            }
        };
    }

    @Override
    public final String toString() {
        return StandardCharsets.UTF_8.decode(this.asByteBuffer()).toString();
    }

    @Override
    public final int hashCode() {
        int hash = 1;
        for (int i = 0; i < length(); ++i) {
            hash = (31 * hash) + get(i);
        }
        return hash;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Bytes)) {
            return false;
        }

        Bytes that = (Bytes) obj;

        if (this.length() != that.length()) {
            return false;
        }

        for (int i = 0; i < length(); ++i) {
            if (this.get(i) != that.get(i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public final int compareTo(Bytes that) {
        int thisLen = this.length();
        int thatLen = that.length();

        int min = Math.min(thisLen, thatLen);

        for (int i = 0; i < min; ++i) {
            int diff = this.get(i) - that.get(i);
            if (diff != 0) {
                return diff;
            }
        }

        return (thisLen - thatLen);
    }
}
