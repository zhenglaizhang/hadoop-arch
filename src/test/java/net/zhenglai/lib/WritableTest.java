package net.zhenglai.lib;

import org.apache.hadoop.io.*;
import org.apache.hadoop.util.StringUtils;
import org.junit.Test;

import java.io.IOException;

import static net.zhenglai.lib.SerializeHelper.serialize;
import static org.junit.Assert.assertEquals;

/**
 * Created by Zhenglai on 7/30/16.
 */


// junit destory and regenerate one intance of our test class for each test method
public class WritableTest {
    private Text text = new Text("hadoop");

    @org.junit.After
    public void tearDown() throws Exception {

    }


    @Test
    public void TextTest() {
        assertEquals(text.getLength(), 6);
        assertEquals(text.getBytes().length, 6);

        // Notice that charAt() returns an int representing a Unicode code point
        assertEquals(text.charAt(2), (int) 'd');
        assertEquals("out of bounds", text.charAt(100), -1);

        assertEquals(text.find("d"), 2);
        assertEquals("No match", text.find("pig"), -1);
    }

    @Test
    public void WritableTest() throws IOException {
        IntWritable writable = new IntWritable();
        writable.set(12);
        IntWritable writable1 = new IntWritable(12);
        assertEquals(writable, writable1);

//        The bytes are written in big-endian order (so the most significant byte is written to the stream first
        byte[] bytes = serialize(writable);
        assertEquals(bytes.length, 4);
        assertEquals(StringUtils.byteToHexString(bytes), "0000000c");

        // Text is mutable
        text.set("pig");
        assertEquals(text, new Text("pig"));


        // it's imperative to call getLength() when calling getBytes()
        Text t = new Text("hadoop");
        t.set(new Text("pig"));
        assertEquals(t.getLength(), (3));
        assertEquals("Byte length not shortened", t.getBytes().length,
                (6));

        // resorting to String
        assertEquals(new Text("hadoop").toString(), "hadoop");
    }


    @Test
    public void BytesWritableTest() throws IOException {
        BytesWritable b = new BytesWritable(new byte[]{3, 5});
        byte[] bytes = SerializeHelper.serialize(b);
        // length(Int) + bytes content
        assertEquals(StringUtils.byteToHexString(bytes), "000000020305");

        b.setCapacity(1000);
        assertEquals(b.getLength(), 2);
        assertEquals(b.getBytes().length, 1000);
    }


    @Test
    public void MapWritableTest() throws IOException {
        // Use MapWritable with different types of keys
        MapWritable src = new MapWritable();
        src.put(new IntWritable(1), new Text("cat"));
        src.put(new VIntWritable(2), new LongWritable(163));

        MapWritable dst = new MapWritable();
        WritableUtils.cloneInto(dst, src);
        assertEquals((Text) dst.get(new IntWritable(1)), new Text("cat"));
        assertEquals((LongWritable) dst.get(new VIntWritable(2)), new LongWritable(163));
    }
}