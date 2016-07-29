package net.zhenglai.lib;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by Zhenglai on 7/29/16.
 */
public class Streams {

    public static <T> Stream<T> streamOf(final Iterable<T> iterable) {
        return toStream(iterable, false);
    }

    public static <T> Stream<T> parallelStreamOf(final Iterable<T> iterable) {
        return toStream(iterable, true);
    }

    private static <T> Stream<T> toStream(final Iterable<T> iterable, final boolean isParallel) {
        return StreamSupport.stream(iterable.spliterator(), isParallel);
    }
}
