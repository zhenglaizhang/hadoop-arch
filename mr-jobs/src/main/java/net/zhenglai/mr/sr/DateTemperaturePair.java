package net.zhenglai.mr.sr;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DateTemperaturePair implements WritableComparable<DateTemperaturePair> {

    private Text yearMonth = new Text();

    @Override
    public int compareTo(DateTemperaturePair o) {
        return 0;
    }

    @Override
    public void write(DataOutput out) throws IOException {

    }

    @Override
    public void readFields(DataInput in) throws IOException {

    }
}
