package net.zhenglai.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.Map;

/**
 * Created by Zhenglai on 7/30/16.
 *
 *
 * Run:
 *      export HADOOP_CLASSPATH=target/classes/
 *      hadoop net.zhenglai.util.ConfigurationPrinter | grep yarn.resourcemanager.address=
 *      hadoop net.zhenglai.util.ConfigurationPrinter -D color=yellow -D autocolor=true | grep color
 *
 * Options specified with -D take priority over properties from the configuration files.
 *
 * The syntax for JVM system properties does not allow any whitespace between the D and the property name, whereas GenericOptionsParser does allow whitespace.
 */
public class ConfigurationPrinter extends Configured implements Tool {

    static {
        // ignore the core ones (which Configuration knows about already).
        Configuration.addDefaultResource("hdfs-default.xml");
        Configuration.addDefaultResource("hdfs-site.xml");
        Configuration.addDefaultResource("yarn-default.xml");
        Configuration.addDefaultResource("yarn-site.xml");
        Configuration.addDefaultResource("mapred-default.xml");
        Configuration.addDefaultResource("mapred-site.xml");
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        for (Map.Entry<String, String> entry : conf) {
            System.out.printf("%s=%s\n", entry.getKey(), entry.getValue());
        }

        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exit = ToolRunner.run(new ConfigurationPrinter(), args);
        System.exit(exit);
    }
}
