package net.zhenglai.test;

import org.apache.hadoop.conf.Configuration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Zhenglai on 7/30/16.
 */
public class ConfTest {

    @Test
    public void confTest() {
        Configuration conf = new Configuration();
        conf.addResource("configuration-1.xml");
        assertEquals(conf.get("color"), "yellow");
        assertEquals(conf.getInt("size", 0), 10);

        // specify default value
        assertEquals(conf.get("breadth", "wide"), "wide");


        // Properties defined in resources that are added later override the earlier definitions
        conf.addResource("configuration-2.xml");
        assertEquals(conf.getInt("size", 0), 12);

        // properties that are marked as final cannot be overridden in later definitions
        assertEquals(conf.get("weight"), "heavy");


        // variable expansion
        assertEquals(conf.get("size-weight"), "12,heavy");


        // System properties take priority over properties defined in resource files
        System.setProperty("size", "14");
        assertEquals(conf.get("size-weight"), "14,heavy");


        // unless system properties are redefined using configuration properties, they are not accessible through the configuration API
        System.setProperty("length", "2");
        assertEquals(conf.get("length"), (String) null);
    }
}
