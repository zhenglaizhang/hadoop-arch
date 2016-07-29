package net.zhenglai;

import net.zhenglai.lib.Streams;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by Zhenglai on 7/29/16.
 */
public class MaxTemperatureReducer
        extends Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        Stream<IntWritable> streams = Streams.streamOf(values);
        IntWritable max = streams.reduce((a, b) -> a.get() >= b.get() ? a : b).get();

        context.write(key, max);
    }
}
