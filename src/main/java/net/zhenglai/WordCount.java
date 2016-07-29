package net.zhenglai;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.omg.CORBA.Object;

import java.io.IOException;

/**
 * Created by Zhenglai on 7/28/16.
 */
public class WordCount {
    public static class SOWordCountMapper extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        }
    }
}
