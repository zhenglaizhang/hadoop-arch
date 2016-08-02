package net.zhenglai.test;


import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by Zhenglai on 8/2/16.
 */
public class AvroTest {

    @Test
    public void testInt() throws IOException {
        Schema schema = new Schema.Parser().parse("\"int\"");
        int datum = 163;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DatumWriter<Integer> writer = new GenericDatumWriter<>(schema);
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null /*reuse*/);
        writer.write(datum, encoder);
        encoder.flush();
        out.close();

        DatumReader<Integer> reader = new GenericDatumReader<>(schema);
        Decoder decoder = DecoderFactory.get()
                .binaryDecoder(out.toByteArray(), null);
        Integer result = reader.read(null, decoder);
        assertThat(result, is(163));

        try {
            reader.read(null, decoder);
            fail("Expected EOFException");
        } catch (EOFException e) {
            // expected
        }
    }

    @Test
    public void TestGenericApi() throws IOException {
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse("{\n" +
                "    \"type\": \"record\",\n" +
                "     \"name\": \"StringPair\",\n" +
                "     \"doc\": \"A pair of strings.\",\n" +
                "     \"fields\": [\n" +
                "        { \"name\": \"left\", \"type\": \"string\" },\n" +
                "        { \"name\": \"right\", \"type\": \"string\" }\n" +
                "     ]\n" +
                "}");
        GenericRecord datum = new GenericData.Record(schema);
        datum.put("left", "L");
        datum.put("right", "R");

        // seraialize the record to output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DatumWriter<GenericRecord> writeri = new GenericDatumWriter<>(schema);
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        writeri.write(datum, encoder);
        encoder.flush();
        out.close();


        DatumReader<GenericRecord> reader = new GenericDatumReader<>(schema);
        Decoder decoder = DecoderFactory.get().binaryDecoder(out.toByteArray(), null);
        GenericRecord result = reader.read(null, decoder);
        assertThat(result.get("left").toString(), is("L"));
        assertThat(result.get("right").toString(), is("R"));
    }
}
