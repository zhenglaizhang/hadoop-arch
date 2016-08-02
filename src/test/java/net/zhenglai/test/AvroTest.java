package net.zhenglai.test;


import net.zhenglai.avro.StringPair;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
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

    /*
$ mvn compile # includes code generation via Avro Maven plugin
$ mvn -q exec:java -Dexec.mainClass=example.SpecificMain
     */
    @Test
    public void testSpecificApi() throws IOException {
        StringPair datum = new StringPair();
        datum.setLeft("L");
        datum.setRight("R");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DatumWriter<StringPair> writer =
                new SpecificDatumWriter<StringPair>(StringPair.class);
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        writer.write(datum, encoder);
        encoder.flush();
        out.close();
        DatumReader<StringPair> reader =
                new SpecificDatumReader<StringPair>(StringPair.class);
        Decoder decoder = DecoderFactory.get().binaryDecoder(out.toByteArray(),
                null);
        StringPair result = reader.read(null, decoder);
        assertThat(result.getLeft(), is("L"));
        assertThat(result.getRight(), is("R"));


        StringPair sp1 = new StringPair("Hello", "Avro");
        StringPair sp2 = StringPair.newBuilder()
                .setLeft("Avro")
                .setRight("Hello")
                .build();
        // Serialize user1, user2 and user3 to disk
        DatumWriter<StringPair> userDatumWriter = new SpecificDatumWriter<StringPair>(StringPair.class);
        DataFileWriter<StringPair> dataFileWriter = new DataFileWriter<StringPair>(userDatumWriter);
        dataFileWriter.create(datum.getSchema(), new File("users.avro"));
        dataFileWriter.append(datum);
        dataFileWriter.append(sp1);
        dataFileWriter.append(sp2);
        dataFileWriter.close();

        // Deserialize Users from disk
        File file = new File("users.avro");
        DatumReader<StringPair> userDatumReader = new SpecificDatumReader<StringPair>(StringPair.class);
        DataFileReader<StringPair> dataFileReader = new DataFileReader<StringPair>(file, userDatumReader);
        StringPair user = null;
        while (dataFileReader.hasNext()) {
// Reuse user object by passing it to next(). This saves us from
// allocating and garbage collecting many objects for files with
// many items.
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }
}
