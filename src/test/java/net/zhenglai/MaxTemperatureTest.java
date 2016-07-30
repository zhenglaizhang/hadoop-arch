package net.zhenglai;

import net.zhenglai.maxtemp.MaxTemperatureDriver;
import net.zhenglai.maxtemp.MaxTemperatureMapper;
import net.zhenglai.maxtemp.MaxTemperatureReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

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

    // A test for MaxTemperatureDriver that uses a local, in-process job runner
    @Test
    public void testConf() throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "file:///");
        conf.set("mapreduce.framework.name", "local");
        conf.setInt("mapreduce.task.io.sort.mb", 1);

        Path input = new Path("input/ncdc/micro");
        Path output = new Path("/tmp/output");

        FileSystem fs = FileSystem.getLocal(conf);
        fs.delete(output, true);        // delete old output

        MaxTemperatureDriver driver = new MaxTemperatureDriver();
        driver.setConf(conf);

        int exit = driver.run(new String[]{
                input.toString(),
                output.toString()
        });

        assertThat(exit, is(0));

        checkOutput(conf, output);
    }

    public static class OutputLogFilter implements PathFilter {

        @Override
        public boolean accept(Path path) {
            return !path.getName().startsWith("_");
        }
    }

    private void checkOutput(Configuration conf, Path output) throws IOException {
        FileSystem fs = FileSystem.getLocal(conf);
        Path[] outputFiles = FileUtil.stat2Paths(fs.listStatus(output, new OutputLogFilter()));
        assertThat(outputFiles.length, is(1));

        BufferedReader actual = asBufferedReader(fs.open(outputFiles[0]));
        BufferedReader expected = asBufferedReader(getClass().getResourceAsStream("/expected.txt"));

        String expectedLine;
        while ((expectedLine = expected.readLine()) != null) {
            assertThat(actual.readLine(), is(expectedLine));
        }

        assertThat(actual.readLine(), nullValue());
        actual.close();
        expected.close();

    }

    private BufferedReader asBufferedReader(InputStream in) {
        return new BufferedReader(new InputStreamReader(in));
    }
}
