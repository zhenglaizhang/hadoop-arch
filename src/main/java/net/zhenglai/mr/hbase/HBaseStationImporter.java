package net.zhenglai.mr.hbase;

import net.zhenglai.lib.NcdcStationMetadata;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.Tool;

import java.io.File;
import java.util.Map;

/**
 * Created by Zhenglai on 8/3/16.
 */
public class HBaseStationImporter extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.printf("Usage: %s <input>", getClass().getName());
            return -1;
        }

        Configuration conf = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(conf);
        try {
            TableName tableName = TableName.valueOf("stations");
            Table table = connection.getTable(tableName);
            try {
                NcdcStationMetadata metadata = new NcdcStationMetadata();
                metadata.initialize(new File(args[0]));
                Map<String, String> stationIdToNameMap = metadata.getStationIdToNameMap();

                for (Map.Entry<String, String> entry : stationIdToNameMap.entrySet()) {
                    Put put = new Put(Bytes.toBytes(entry.getKey()));
                }
            } finally {
                table.close();
            }
        } finally {
            connection.close();
        }

        return 0;
    }
}
