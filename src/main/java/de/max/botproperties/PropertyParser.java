package de.max.botproperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

final class PropertyParser {

    private PropertyParser() {}

    static Map<String, String> parse(InputStream inputStream) {
        Map<String, String> properties = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#"))
                    continue;

                int separator = line.indexOf('=');
                if (separator <= 0)
                    continue;

                String key = line.substring(0, separator).trim();
                String value = line.substring(separator + 1).trim();

                properties.put(key, value);
            }

        } catch (IOException e) {
            throw new PropertyException("Failed to parse bot.properties", e);
        }

        return properties;
    }
}
