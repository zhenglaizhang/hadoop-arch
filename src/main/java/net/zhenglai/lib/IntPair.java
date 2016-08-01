package net.zhenglai.lib;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by Zhenglai on 8/1/16.
 */
public class IntPair implements WritableComparable<IntPair> {

    private IntWritable first;
    private IntWritable second;

    public IntPair() {
        this(new IntWritable(), new IntWritable());
    }

    public IntPair(IntWritable first, IntWritable second) {
        this.first = first;
        this.second = second;
    }

    public IntPair(int first, int second) {
        this(new IntWritable(first), new IntWritable(second));
    }

    @Override
    public int compareTo(IntPair o) {
        int cmp = first.compareTo(o.first);
        if (cmp != 0) {
            return cmp;
        }

        return second.compareTo(o.second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntPair intPair = (IntPair) o;

        return first.equals(intPair.first) && second.equals(intPair.second);
    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(first.get());
        out.writeInt(second.get());
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        first.readFields(in);
        second.readFields(in);
    }

    @Override
    public String toString() {
        return "IntPair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    public static int compare(int a, int b) {
        return (a < b ? -1 : (a == b ? 0 : 1));
    }
}
