package net.zhenglai.lib;


import org.apache.hadoop.io.Text;

/**
 * Created by Zhenglai on 8/1/16.
 */
public class NcdcStationMetadataParser {

    private String stationId;
    private String stationName;

    public boolean parse(String record) {
        // header
        if (record.length() < 42) return false;

        String usaf = record.substring(0, 6);
        String wban = record.substring(7, 12);
        stationId = usaf + "-" + wban;
        stationName = record.substring(13, 42);
        try {
            Integer.parseInt(usaf); // USAF identifiers are numeric
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean parse(Text record) {
        return parse(record.toString());
    }

    public String getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

}

