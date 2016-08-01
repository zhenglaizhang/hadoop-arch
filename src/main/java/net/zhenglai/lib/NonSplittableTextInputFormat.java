package net.zhenglai.lib;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobContext;
import org.apache.hadoop.mapred.TextInputFormat;

/**
 * Created by Zhenglai on 8/1/16.
 */
public class NonSplittableTextInputFormat extends TextInputFormat {

    @Override
    protected boolean isSplitable(FileSystem fs, Path file) {
        return false;
    }
}
