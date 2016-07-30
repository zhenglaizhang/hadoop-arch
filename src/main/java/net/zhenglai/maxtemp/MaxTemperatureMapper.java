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
        OVER_100
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
                context.getCounter(Temperature.OVER_100).increment(1);
            } else {
                context.write(
                        new Text(parser.getYear()),
                        new IntWritable(airTemperature)
                );
            }

        }
    }
}
