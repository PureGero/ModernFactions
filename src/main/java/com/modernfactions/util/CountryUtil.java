package com.modernfactions.util;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class CountryUtil {

    private static HashMap<String, String> countryCache = new HashMap<>();
    public static String getCountry(String ip) {
        if (countryCache.containsKey(ip)) {
            return countryCache.get(ip);
        }

        try {
            URL url = new URL("http://ip-api.com/csv/" + ip);
            InputStream in = url.openStream();
            byte[] b = new byte[4096];
            int l = in.read(b);
            in.close();

            String data = new String(b, 0, l, StandardCharsets.UTF_8);
            String[] args = data.split(",");
            String country = args[1];

            countryCache.put(ip, country);
            return country;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Void";
    }

}
