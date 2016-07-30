package net.zhenglai.mr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by Zhenglai on 7/30/16.
 *  hadoop jar target/hadoop-arch.jar net.zhenglai.mr.LoggingDriver -fs file:/// -jt local -D mapreduce.map.log.level=DEBUG input/ncdc/all /tmp/output07
 */
public class LoggingIdentityMapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
        extends Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT> {
    private static final Log LOG = LogFactory.getLog(LoggingIdentityMapper.class);

    @Override
    @SuppressWarnings("unchecked")
    protected void map(KEYIN key, VALUEIN value, Context context) throws IOException, InterruptedException {
        super.map(key, value, context);

        // Log to stdout file
        System.out.printf("Map Key: %s", key);

        // log to syslog file
        LOG.info("Map key: " + key);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Map value: " + value);
        }

        context.write((KEYOUT) key, (VALUEOUT) value);
    }
}
