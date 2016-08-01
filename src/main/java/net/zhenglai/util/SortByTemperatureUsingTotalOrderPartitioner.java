package net.zhenglai.util;

import net.zhenglai.lib.JobBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.net.URI;

/**
 * Created by Zhenglai on 8/1/16.
 * <p>
 * <p>
 * <code></code>hadoop net.zhenglai.util.SortByTemperatureUsingTotalOrderPartitioner -D mapreduce.job.reduces=30 -fs
 * file:///
 * -jt local /tmp/ncdc/all-seq2 /tmp/ncdc/all-seq5</code>
 * <p>
 * The program produces 30 output partitions, each of which is internally sorted; in addition,
 * for these partitions, all the keys in partition i are less than the keys in partition
 * i + 1.
 */
public class SortByTemperatureUsingTotalOrderPartitioner extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        Job job = JobBuilder.parseInputAndOutput(this, getConf(), args);
        if (job == null) {
            return -1;
        }

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setCompressOutput(job, false);

        job.setPartitionerClass(TotalOrderPartitioner.class);

        InputSampler.Sampler<IntWritable, Text> sampler =
                new InputSampler.RandomSampler<>(0.1, 10000, 10);

        InputSampler.writePartitionFile(job, sampler);

        // Add to DistributedCache
        Configuration conf = new Configuration();
        String partitionFile = TotalOrderPartitioner.getPartitionFile(conf);
        URI partitionUri = new URI(partitionFile);
        job.addCacheFile(partitionUri);


        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exit = ToolRunner.run(new SortByTemperatureUsingTotalOrderPartitioner(), args);
        System.exit(exit);
    }
}
