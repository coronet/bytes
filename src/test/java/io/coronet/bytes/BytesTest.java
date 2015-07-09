package io.coronet.bytes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

public class BytesTest {

    @Test
    public void testEmpty() {
        Bytes empty = Bytes.empty();
        Assert.assertEquals(0, empty.length());

        try {
            empty.get(-1);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected.
        }

        try {
            empty.get(0);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected.
        }
    }

    @Test
    public void testSliceEmpty2() {
        Bytes empty = Bytes.empty();

        Bytes empty3 = empty.slice(0, 0);
        Assert.assertEquals(0, empty3.length());

        Assert.assertNotSame(empty, empty3);
        Assert.assertEquals(empty.hashCode(), empty3.hashCode());
        Assert.assertEquals(empty, empty3);

        try {
            empty.slice(-1, 0);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected.
        }

        try {
            empty.slice(1, 0);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected.
        }

        try {
            empty.slice(0, 1);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected.
        }

        try {
            empty.slice(1, -1);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected.
        }
    }

    @Test
    public void testEmptyConvert() throws IOException {
        Bytes empty = Bytes.empty();

        ByteBuffer buf = empty.asByteBuffer();

        Assert.assertEquals(0, buf.position());
        Assert.assertEquals(0, buf.remaining());
        Assert.assertEquals(0, buf.limit());

        InputStream stream = empty.asInputStream();
        Assert.assertEquals(-1, stream.read());

        Assert.assertEquals("", empty.toString());
    }

    @Test
    public void testFromBytes() {
        try {
            Bytes.wrap((byte[]) null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected.
        }

        Bytes bytes = Bytes.wrap(new byte[] { 0, 1, 2, 3 });

        Assert.assertEquals(4, bytes.length());
        for (int i = 0; i < 4; ++i) {
            Assert.assertEquals(i, bytes.get(i));
        }

        try {
            bytes.get(-1);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected.
        }

        try {
            bytes.get(4);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected.
        }
    }

    @Test
    public void testSliceFromBytes() {
        Bytes bytes = Bytes.wrap(new byte[] { 0, 1, 2, 3 });

        Bytes prefix = bytes.slice(0, 2);
        Assert.assertEquals(2, prefix.length());
        Assert.assertEquals(0, prefix.get(0));
        Assert.assertEquals(1, prefix.get(1));

        Bytes postfix = bytes.slice(1, 3);
        Assert.assertEquals(3, postfix.length());
        Assert.assertEquals(1, postfix.get(0));
        Assert.assertEquals(2, postfix.get(1));
        Assert.assertEquals(3, postfix.get(2));

        Bytes empty = bytes.slice(4, 0);
        Assert.assertEquals(0, empty.length());

        try {
            bytes.slice(-1, 2);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            bytes.slice(0, 5);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            bytes.slice(4, 1);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        try {
            bytes.slice(5, -3);
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testFromBytesConvert() throws IOException {
        Bytes bytes = Bytes.wrap(new byte[] { 'H', 'e', 'l', 'O' });

        ByteBuffer buf = bytes.asByteBuffer();

        Assert.assertEquals(0, buf.position());
        Assert.assertEquals(4, buf.remaining());
        Assert.assertEquals(4, buf.limit());

        Assert.assertEquals('H', buf.get());
        Assert.assertEquals('e', buf.get());
        Assert.assertEquals('l', buf.get());
        Assert.assertEquals('O', buf.get());

        InputStream stream = bytes.asInputStream();
        Assert.assertEquals('H', stream.read());
        Assert.assertEquals('e', stream.read());
        Assert.assertEquals('l', stream.read());
        Assert.assertEquals('O', stream.read());
        Assert.assertEquals(-1, stream.read());

        Assert.assertEquals("HelO", bytes.toString());
    }

    @Test
    public void testFromString() {
        Bytes bytes = Bytes.from("Hello World");

        Assert.assertEquals(11, bytes.length());
        Assert.assertEquals('H', bytes.get(0));
        Assert.assertEquals('e', bytes.get(1));
        Assert.assertEquals('l', bytes.get(2));
        Assert.assertEquals('l', bytes.get(3));
        Assert.assertEquals('o', bytes.get(4));
        Assert.assertEquals(' ', bytes.get(5));
        Assert.assertEquals('W', bytes.get(6));
        Assert.assertEquals('o', bytes.get(7));
        Assert.assertEquals('r', bytes.get(8));
        Assert.assertEquals('l', bytes.get(9));
        Assert.assertEquals('d', bytes.get(10));

        Assert.assertEquals(Bytes.from("Hello"), bytes.slice(0, 5));
        Assert.assertEquals(Bytes.from("lo Wo"), bytes.slice(3, 5));
        Assert.assertEquals(Bytes.empty(), bytes.slice(11, 0));
    }
}
