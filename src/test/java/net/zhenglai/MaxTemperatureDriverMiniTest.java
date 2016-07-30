package net.zhenglai;

import net.zhenglai.maxtemp.MaxTemperatureDriver;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.ClusterMapReduceTestCase;
import org.apache.hadoop.mapred.OutputLogFilter;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Zhenglai on 7/30/16.
 */

// A test for MaxTemperatureDriver that runs in a "mini" HDFS and MapReduce cluster
public class MaxTemperatureDriverMiniTest extends ClusterMapReduceTestCase {

    @Override
    protected void setUp() throws Exception {
        if (System.getProperty("test.build.data") == null) {
            System.setProperty("test.build.data", "/tmp");
        }

        if (System.getProperty("hadoop.log.dir") == null) {
            System.setProperty("hadoop.log.dir", "/tmp");
        }

        super.setUp();
    }

    @Override
    protected Path getInputDir() {
        return new Path("/tmp/net.zhenglai.test/input");
    }

    @Override
    protected Path getOutputDir() {
        return new Path("tmp/net.zhenglai.test/output");
    }

    // TODO bug fix
    @Test
    public void test() throws Exception {
        Configuration conf = new Configuration();

        Path localInput = new Path("input/ncdc/micro");
        Path input = getInputDir();
        Path output = getOutputDir();

        if (!getFileSystem().exists(input)) {
            getFileSystem().create(input);
        }

        if (!getFileSystem().exists(output)) {
            getFileSystem().create(output);
        }

        // copy input data to test hdfs
        getFileSystem().copyFromLocalFile(localInput, input);

        MaxTemperatureDriver driver = new MaxTemperatureDriver();
        driver.setConf(conf);

        int exit = driver.run(new String[]{
                input.toString(),
                output.toString()
        });

        Path[] outputFiles = FileUtil.stat2Paths(getFileSystem().listStatus(output, new OutputLogFilter()));
        assertThat(outputFiles.length, is(1));

        InputStream in = getFileSystem().open(outputFiles[0]);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        assertThat(reader.readLine(), is("1901\t317"));
        assertThat(reader.readLine(), is("1949\t111"));
        assertThat(reader.readLine(), is("1950\t22"));
        reader.close();
    }
}
