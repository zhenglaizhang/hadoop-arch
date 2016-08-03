package net.zhenglai.mr.hbase;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by Zhenglai on 8/3/16.
 */
public class SimpleRowCounter extends Configured implements Tool {

    static class RowCounterMapper extends TableMapper<ImmutableBytesWritable, Result> {
        public static enum Counters {
            ROWS
        }

        /*
        Input keys are ImmutableBytesWritable objects (row keys), and values are Result objects (row results from a scan).
         */
        @Override
        public void map(ImmutableBytesWritable row, Result value, Context context) {
            context.getCounter(Counters.ROWS).increment(1L);
        }
    }


    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.printf("Usage: %s <tablename>", getClass().getSimpleName());
            return -1;
        }

        String tableName = args[0];
        Scan scan = new Scan();
        // the FirstKeyOnlyFilter is run on the server and strips all the column data before sending the result to the client
        /*
        When performing a table scan where only the row keys are needed (no families, qualifiers, values or timestamps), add a FilterList with a MUST_PASS_ALL operator to the scanner using setFilter. The filter list should include both a FirstKeyOnlyFilter and a KeyOnlyFilter. Using this filter combination will result in a worst case scenario of a RegionServer reading a single value from disk and minimal network traffic to the client for a single row.
         */
        scan.setFilter(new FirstKeyOnlyFilter());
        scan.setCaching(1000);

        Job job = new Job(getConf(), getClass().getSimpleName());
        job.setJarByClass(getClass());

        TableMapReduceUtil.initTableMapperJob(
                tableName, scan, RowCounterMapper.class, ImmutableBytesWritable.class, Result.class, job
        );

        job.setNumReduceTasks(0);
        job.setOutputFormatClass(NullOutputFormat.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(HBaseConfiguration.create(), new SimpleRowCounter(), args));
    }
}
