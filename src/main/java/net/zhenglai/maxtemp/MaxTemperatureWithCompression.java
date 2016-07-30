package net.zhenglai.maxtemp;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by Zhenglai on 7/29/16.
 *
 * Run:
 *  hadoop net.zhenglai.maxtemp.MaxTemperatureWithCompression /data/ncdc/1901.gz /data/ncdc/output-testgz
 *  hdfs dfs -get /data/ncdc/output-testgz/part-r-00000.gz /tmp/test.gz
 *  gunzip -c /tmp/test.gz
 */
public class MaxTemperatureWithCompression {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        if (args.length != 2) {
            System.err.println("Usage: MaxTemperature <input path> <output path>");
            System.exit(-1);
        }

        Job job = Job.getInstance();
        job.setJarByClass(MaxTemperatureWithCompression.class);
        job.setJobName("Max temperature");

        // for compressed input, MR will handle it automatically based on the file extension
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(MaxTemperatureMapper.class);
        job.setReducerClass(MaxTemperatureReducer.class);

        // combiner is the same as reducer here
        job.setCombinerClass(MaxTemperatureReducer.class);


        // set the compression output
        FileOutputFormat.setCompressOutput(job, true);
        FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
