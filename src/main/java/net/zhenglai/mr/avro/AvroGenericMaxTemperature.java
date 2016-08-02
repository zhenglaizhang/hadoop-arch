package net.zhenglai.mr.avro;

import net.zhenglai.lib.NcdcRecordParser;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyOutputFormat;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * Created by Zhenglai on 8/2/16.
 * <p>
 * This program uses the Generic Avro mapping. This frees us from generating code to
 * represent records, at the expense of type safety
 * </p>
 */
public class AvroGenericMaxTemperature extends Configured implements Tool {
    private static final Schema SCHEMA = new Schema.Parser().parse(
            "{" +
                    " \"type\": \"record\"," +
                    " \"name\": \"WeatherRecord\"," +
                    " \"doc\": \"A weather reading.\"," +
                    " \"fields\": [" +
                    " {\"name\": \"year\", \"type\": \"int\"}," +
                    " {\"name\": \"temperature\", \"type\": \"int\"}," +
                    " {\"name\": \"stationId\", \"type\": \"string\"}" +
                    " ]" +
                    "}"
    );

    public static class MaxTemperatureMapper
            extends Mapper<LongWritable, Text, AvroKey<Integer>, AvroValue<GenericRecord>> {

        private NcdcRecordParser parser = new NcdcRecordParser();
        private GenericRecord record = new GenericData.Record(SCHEMA);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            parser.parse(value.toString());
            if (parser.isValidTemperature()) {
                record.put("year", parser.getYearInt());
                record.put("temperature", parser.getAirTemperature());
                record.put("stationId", parser.getStationId());
                context.write(new AvroKey<>(parser.getYearInt()), new AvroValue<>(record));
            }
        }
    }

    public static class MaxTemperatureReducer
            extends Reducer<AvroKey<Integer>, AvroValue<GenericRecord>, AvroKey<GenericRecord>, NullWritable> {
        @Override
        protected void reduce(AvroKey<Integer> key, Iterable<AvroValue<GenericRecord>> values, Context context) throws IOException, InterruptedException {
            GenericRecord max = null;
            for (AvroValue<GenericRecord> value : values) {
                GenericRecord record = value.datum();
                if (max == null || (Integer) record.get("temperature") > (Integer) max.get("temperature")) {
                    max = newWeatherRecord(record);
                }
            }
        }

        private GenericRecord newWeatherRecord(GenericRecord value) {
            GenericRecord record = new GenericData.Record(SCHEMA);
            record.put("year", value.get("year"));
            record.put("temperature", value.get("temperature"));
            record.put("stationId", value.get("stationId"));
            return record;
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.printf("Usage: %s [generic options] <input> <output>", getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        Job job = new Job(getConf(), "Max Temperature with Avro");
        job.setJarByClass(getClass());

        job.getConfiguration().setBoolean(Job.MAPREDUCE_JOB_USER_CLASSPATH_FIRST, true);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        AvroJob.setMapOutputKeySchema(job, Schema.create(Schema.Type.INT));
        AvroJob.setMapOutputValueSchema(job, SCHEMA);
        AvroJob.setOutputKeySchema(job, SCHEMA);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(AvroKeyOutputFormat.class);

        job.setMapperClass(MaxTemperatureMapper.class);
        job.setReducerClass(MaxTemperatureReducer.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new AvroGenericMaxTemperature(), args));
    }
}
