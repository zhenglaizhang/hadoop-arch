package net.zhenglai.mr;

import net.zhenglai.lib.JobBuilder;
import net.zhenglai.lib.NcdcRecordParser;
import net.zhenglai.lib.NcdcStationMetadataParser;
import net.zhenglai.lib.TextPair;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Zhenglai on 8/1/16.
 */
public class JoinRecordWithStationName extends Configured implements Tool {

    public static class JoinStationMapper
            extends Mapper<LongWritable, Text, TextPair, Text> {
        private NcdcStationMetadataParser parser = new NcdcStationMetadataParser();

        /*
To tag each record, we use TextPair for the keys (to store the
station ID) and the tag. The only requirement for the tag values is that they sort in such
a way that the station records come before the weather records. This can be achieved
by tagging station records as 0 and weather records as 1.
         */
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            if (parser.parse(value)) {
                System.out.printf("Meta-StationId: %s", parser.getStationId());
                context.write(new TextPair(
                        parser.getStationId(), "0"
                ), new Text(parser.getStationName()));
            }
        }
    }

    public static class JoinRecordMapper
            extends Mapper<LongWritable, Text, TextPair, Text> {
        private NcdcRecordParser parser = new NcdcRecordParser();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            parser.parse(value);
            System.out.printf("Record-StationId: %s", parser.getStationId());
            context.write(new TextPair(parser.getStationId(), "1"), value);
        }
    }

    // Reducer for joining tagged station records with tagged weather records
    public class JoinReducer extends Reducer<TextPair, Text, Text, Text> {
        // The code assumes that every station ID in the weather records has exactly one matching
        //        record in the station dataset.
        @Override
        protected void reduce(TextPair key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Iterator<Text> iter = values.iterator();
            // The reducer knows that it will receive the station record first
            // Because objects in the reducer’s values iterator are reused (for efficiency
//            purposes), it is vital that the code makes a copy of the first
//            Text object from the values iterator
            Text stationName = new Text(iter.next());
            while (iter.hasNext()) {
                Text record = iter.next();
                Text outValue = new Text(stationName.toString() + "\t" + record.toString());
                context.write(key.getFirst(), outValue);
            }
        }
    }

    public static class KeyPartitioner extends Partitioner<TextPair, Text> {
        @Override
        public int getPartition(TextPair key, Text value, int numPartitions) {
            return (key.getFirst().hashCode() & Integer.MAX_VALUE) % numPartitions;
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 3) {
            JobBuilder.printUsage(this, "<ncdc input> <station input> <output>");
            return -1;
        }

        Job job = new Job(getConf(), "Join weather records with station names");
        job.setJarByClass(getClass());

        Path ncdcInputPath = new Path(args[0]);
        Path stationInputPath = new Path(args[1]);
        Path outputPath = new Path(args[2]);

        MultipleInputs.addInputPath(job, ncdcInputPath,
                TextInputFormat.class, JoinRecordMapper.class);
        MultipleInputs.addInputPath(job, stationInputPath,
                TextInputFormat.class, JoinStationMapper.class);
        FileOutputFormat.setOutputPath(job, outputPath);

        job.setPartitionerClass(KeyPartitioner.class);
        job.setGroupingComparatorClass(TextPair.FirstComparator.class);

        job.setMapOutputKeyClass(TextPair.class);

        job.setReducerClass(JoinReducer.class);
        job.setOutputKeyClass(Text.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(
                ToolRunner.run(new JoinRecordWithStationName(), args)
        );
    }
}
