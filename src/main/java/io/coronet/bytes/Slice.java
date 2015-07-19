package io.coronet.bytes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * A single, linear slice of a byte array.
 */
final class Slice extends Bytes {

    private final byte[] array;
    private final int offset;
    private final int length;

    Slice(byte[] array, int offset, int length) {
        if (array == null) {
            throw new NullPointerException("array");
        }
        if (offset < 0) {
            throw new IndexOutOfBoundsException("offset < 0: " + offset);
        }
        if (length < 0) {
            throw new IndexOutOfBoundsException("length < 0: " + length);
        }
        if (array.length - offset < length) {
            throw new IndexOutOfBoundsException(
                    "offset + length > array.length: "
                            + offset + ", "
                            + length + ", "
                            + array.length);
        }

        this.array = array;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public byte get(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("index (" + index + ") < 0");
        }
        if (index >= length) {
            throw new IndexOutOfBoundsException(
                    "index (" + index + ") >= length (" + length + ")");
        }

        return array[offset + index];
    }

    @Override
    public Bytes slice(int offset, int length) {
        if (offset < 0) {
            throw new IndexOutOfBoundsException("offset (" + offset + ") < 0");
        }
        if (length < 0) {
            throw new IndexOutOfBoundsException("length (" + length + ") < 0");
        }
        if (this.length - offset < length) {
            throw new IndexOutOfBoundsException(
                    "offset (" + offset
                    + ") + length (" + length
                    + ") > this.length (" + this.length + ")");
        }

        return new Slice(array, this.offset + offset, length);
    }

    @Override
    public ByteBuffer asByteBuffer() {
        return ByteBuffer.wrap(array, offset, length).asReadOnlyBuffer();
    }

    @Override
    public InputStream asInputStream() {
        return new ByteArrayInputStream(array, offset, length);
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(array, offset, length);
    }
}
