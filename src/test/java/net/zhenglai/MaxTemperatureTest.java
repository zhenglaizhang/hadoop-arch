package net.zhenglai;

import net.zhenglai.maxtemp.MaxTemperatureMapper;
import net.zhenglai.maxtemp.MaxTemperatureReducer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Zhenglai on 7/30/16.
 */
public class MaxTemperatureTest {

    // test-driven fasion
    @Test
    public void processValidRecord() throws IOException {
        Text value = new Text("0043011990999991950051518004+68750+023550FM-12+0382" +
// Year ^^^^
                "99999V0203201N00261220001CN9999999N9-00111+99999999999");
// Temperature ^^^^^

        new MapDriver<LongWritable, Text, Text, IntWritable>()
                .withMapper(new MaxTemperatureMapper())
                .withInput(new LongWritable(0), value) // input key is ignored, so set it to any value is ok
                .withOutput(new Text("1950"), new IntWritable(-11))
                .runTest();
    }


    @Test
    public void ignoreMissingTemperatureRecord() throws IOException {
        Text value = new Text("0043011990999991950051518004+68750+023550FM-12+0382" +
// Year ^^^^
                "99999V0203201N00261220001CN9999999N9+99991+99999999999");
// Temperature ^^^^^


        // A MapDriver can be used to check for zero, one, or more output records, according to the number of times that withOutput() is called
        // So this map shall have no output
        new MapDriver<LongWritable, Text, Text, IntWritable>()
                .withMapper(new MaxTemperatureMapper())
                .withInput(new LongWritable(0), value)
                .runTest();
    }


    @Test
    public void returnMaxIntegerInValues() throws IOException, InterruptedException {
        new ReduceDriver<Text, IntWritable, Text, IntWritable>()
                .withReducer(new MaxTemperatureReducer())
                .withInput(new Text("1950"),
                        Arrays.asList(new IntWritable(10), new IntWritable(5)))
                .withOutput(new Text("1950"), new IntWritable(10))
                .runTest();
    }
}
