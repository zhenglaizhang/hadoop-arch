package net.zhenglai.util;

import net.zhenglai.lib.JobBuilder;
import net.zhenglai.maxtemp.MaxTemperatureMapper;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by Zhenglai on 8/1/16.
 */
public class MissingTemperatureFields extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 1) {
            JobBuilder.printUsage(this, "<job ID>");
            return -1;
        }

        String jobID = args[0];
        Cluster cluster = new Cluster(getConf());
        Job job = cluster.getJob(JobID.forName(jobID));

        if (job == null) {
            // the ID was incorrectly specified or because the job is no longer in the job history
            System.err.printf("No job with ID %s found.\n", jobID);
            return -1;
        }

        if (!job.isComplete()) {
            System.err.printf("Job %s is not complete.\n", jobID);
            return -1;
        }

        Counters counters = job.getCounters();
        long missing = counters.findCounter(
                MaxTemperatureMapper.Temperature.MISSING
        ).getValue();
        long total = counters.findCounter(TaskCounter.MAP_INPUT_RECORDS).getValue();

        System.out.printf("Records with missing temperature fields: %.2f%%.\n",
                100.0 * missing / total);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exit = ToolRunner.run(
                new MissingTemperatureFields(),
                args
        );
        System.exit(exit);
    }

}
