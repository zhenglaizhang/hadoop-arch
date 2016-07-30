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

    private NcdcRecordParser parser = new NcdcRecordParser();

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        parser.parse(value);

        if (parser.isValidTemperature()) {
            context.write(
                    new Text(parser.getYear()),
                    new IntWritable(parser.getAirTemperature())
            );
        }
    }
}
