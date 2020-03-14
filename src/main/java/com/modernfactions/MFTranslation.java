package com.modernfactions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class MFTranslation {

    /**
     * Load a MFTranslation object from an InputSteam
     */
    public static MFTranslation load(InputStream in) throws IOException {
        MFTranslation translation = new MFTranslation();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.indexOf('=') < 0) {
                continue;
            }

            String key = line.substring(0, line.indexOf('='));
            String value = line.substring(line.indexOf('=') + 1);

            translation.map.put(key, value);
        }

        reader.close();

        return translation;
    }

    private HashMap<String, String> map = new HashMap<>();

    public String get(String key) {
        return map.get(key);
    }
}
