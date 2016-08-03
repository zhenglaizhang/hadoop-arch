package net.zhenglai.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Zhenglai on 8/3/16.
 * <p>
 * Apache HBase is the Hadoop database. Use it when you need random, realtime read/write access to your Big Data. This project's goal is the hosting of very large tables -- billions of rows X millions of columns -- atop clusters of commodity hardware.
 */
public class ExampleClientOldApi {
    public static void main(String[] args) throws IOException {
        /*
        It will return a Configuration that has read the HBase configuration from the hbasesite.
xml and hbase-default.xml files found on the program’s classpath
         */
        Configuration conf = HBaseConfiguration.create();

        try (HBaseAdmin admin = new HBaseAdmin(conf)) {
            TableName tableName = TableName.valueOf("test2");
            HTableDescriptor htd = new HTableDescriptor(tableName);
            HColumnDescriptor hcd = new HColumnDescriptor("data");
            htd.addFamily(hcd);
            admin.createTable(htd);
            HTableDescriptor[] tables = admin.listTables();

            // test and test2 table
            if (Arrays.stream(tables).filter((t) -> Bytes.equals(tableName.getName(), t.getTableName().getName()))
                    .count() == 0) {
                throw new IOException("Failed to create of table");
            }

            // Run some operations -- three puts, a get, and a scan -- against the table.
            try (HTable table = new HTable(conf, tableName)) {
                for (int i = 0; i < 3; ++i) {
                    byte[] row = Bytes.toBytes("row" + i);
                    Put put = new Put(row);
                    // The column name is specified in two parts: the column family name, and the column family qualifier
                    byte[] cf = Bytes.toBytes("data");
                    byte[] qualifier = Bytes.toBytes(String.valueOf(i));
                    byte[] value = Bytes.toBytes("value" + i);
                    put.add(cf, qualifier, value);
                    table.put(put);
                }
                Get get = new Get(Bytes.toBytes("row1"));
                Result result = table.get(get);
                System.out.printf("Get: %s\n", result);
                Scan scan = new Scan();
                try (ResultScanner scanner = table.getScanner(scan)) {
                    for (Result scannerResult : scanner) {
                        System.out.printf("Scan: %s\n", scannerResult);
                    }
                }

                // disable then drop the table
                admin.disableTable(tableName);
                admin.deleteTable(tableName);
            }
        }
    }
}
