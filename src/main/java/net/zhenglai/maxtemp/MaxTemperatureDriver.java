package net.zhenglai.maxtemp;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by Zhenglai on 7/30/16.
 * <p>
 * <p>
 * Run
 * hadoop net.zhenglai.maxtemp.MaxTemperatureDriver -conf etc/develop/hadoop-local.xml input/ncdc/1901 /tmp/output01
 * cat /tmp/output01/part-r-00000
 * or
 * hadoop net.zhenglai.maxtemp.MaxTemperatureDriver -fs file:/// -jt local input/ncdc/1901 /tmp/output02
 * or input as one dir
 * hadoop net.zhenglai.maxtemp.MaxTemperatureDriver -fs file:/// -jt local input/ncdc/micro /tmp/output04
 * <p>
 * hadoop-arch [master●●] % cat /tmp/output04/part-r-00000
 * 1901    317
 * 1949    111
 * 1950    22
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * TemperatureQuality
 * 1=13129
 * 9=1
 * net.zhenglai.maxtemp.MaxTemperatureMapper$Temperature
 * MISSING=1
 */
public class MaxTemperatureDriver extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.printf("Usage: %s [generic options] <input> <output> \n", getClass().getName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        Job job = new Job(getConf(), "Max temperature");
        job.setJarByClass(getClass());

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(MaxTemperatureMapper.class);
        job.setCombinerClass(MaxTemperatureReducer.class);
        job.setReducerClass(MaxTemperatureReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

//        The waitForCompletion() method on Job launches the job and polls for progress, writing a line summarizing the map and reduce’s progress whenever either changes.
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exit = ToolRunner.run(new MaxTemperatureDriver(), args);
        System.exit(exit);
    }
}


