package net.zhenglai.maxtemp;

import net.zhenglai.lib.NcdcRecordParser;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


/**
 * Created by Zhenglai on 7/29/16.
 */
public class MaxTemperatureMapper
        extends Mapper<LongWritable, Text, Text, IntWritable> {

    public enum Temperature {
        OVER_100,
        MALFORMED,
        MISSING
    }

    private NcdcRecordParser parser = new NcdcRecordParser();

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        parser.parse(value);

        if (parser.isValidTemperature()) {
            int airTemperature = parser.getAirTemperature();
            if (airTemperature > 1000) {
                System.err.println("Temperature over 100 degrees for input: " + value);
                // check the task and task attempts page
                context.setStatus("Detected possibly corrupt record: see logs");
                // mapred job -counter job_1410450250506_0006  'net.zhenglai.maxtemp.MaxTemperatureMapper$Temperature' OVER_100
                context.getCounter(Temperature.OVER_100).increment(1L);
            }

            context.write(
                    new Text(parser.getYear()),
                    new IntWritable(airTemperature)
            );
        } else if (parser.isMalformedTemperature()) {
            System.err.println("Ignoring possibly corrupt input: " + value);
            context.getCounter(Temperature.MALFORMED).increment(1L);
        } else if (parser.isMissingTemperature()) {
            context.getCounter(Temperature.MISSING).increment(1L);
        }

        // dynamic counter
        context.getCounter("TemperatureQuality", parser.getQuality()).increment(1L);
    }
}
