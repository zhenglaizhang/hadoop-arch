package net.zhenglai.lib;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by Zhenglai on 7/30/16.
 */
public class TextPair implements WritableComparable<TextPair> {

    private Text first;
    private Text second;

    public TextPair() {
        set(new Text(), new Text());
    }

    public TextPair(String first, String second) {
        set(new Text(first), new Text((second)));
    }

    public TextPair(Text first, Text second) {
        this.first = first;
        this.second = second;
    }

    private void set(Text text, Text text1) {
        this.first = first;
        this.second = second;
    }

    public Text getFirst() {
        return first;
    }

    public Text getSecond() {
        return second;
    }

    @Override
    public int compareTo(TextPair o) {
        int cmp = first.compareTo(o.first);
        if (cmp != 0) {
            return cmp;
        }

        return second.compareTo(o.second);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        first.write(out);
        second.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        first.readFields(in);
        second.readFields(in);
    }

    // The hashCode() method is used by the HashPartitioner (the default partitioner in MapReduce) to choose a reduce partition
    @Override
    public int hashCode() {
        return first.hashCode() * 163 + second.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TextPair) {
            TextPair tp = (TextPair) o;
            return first.equals(tp.first) && second.equals(tp.second);
        }

        return false;
    }


    @Override
    public String toString() {
        return first + "\t" + second;
    }
}
