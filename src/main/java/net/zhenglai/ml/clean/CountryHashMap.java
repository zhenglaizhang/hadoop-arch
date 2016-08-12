package net.zhenglai.ml.clean;


import java.util.HashMap;
import java.util.Map;

// Always remember that freedom of input, where it can be avoided, should be avoided
// find all the distinct names in the country fi eld and associate them with a two-letter country code.
public class CountryHashMap {

    private Map<String, String> countries = new HashMap<>();


    // However, you are strongly advised to look
    // the source of the problem and refactor the input
    public CountryHashMap() {
        countries.put("Ireland", "IE");
        countries.put("Eire", "IE");
        countries.put("Republic of Ireland", "IE");
        countries.put("Northern Ireland", "UK");
        countries.put("England", "UK");
// you could add more or generate from a database.
    }

    public String getCountryCode(String country) {
        return countries.get(country);
    }

    public static void main(String[] args) {
        CountryHashMap map = new CountryHashMap();
        System.out.println(map.getCountryCode("Ireland"));
        System.out.println(map.getCountryCode("Northern Ireland"));
    }
}
