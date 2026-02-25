package de.max.botproperties;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public final class BotProperties {

    private static Map<String, String> properties;
    private static boolean loaded = false;

    private BotProperties() {}

    public static synchronized BotProperties load() {
        if (loaded)
            return new BotProperties();

        try (InputStream input = new FileInputStream("bot.properties")) {
            properties = PropertyParser.parse(input);
            loaded = true;
        } catch (Exception e) {
            throw new PropertyException("Could not load bot.properties", e);
        }

        return new BotProperties();
    }

    private static void ensureLoaded() {
        if (!loaded)
            throw new PropertyException("bot.properties not loaded. Call BotProperties.load() first.");
    }

    public String get(String key) {
        ensureLoaded();
        return properties.get(key);
    }

    public String getOrDefault(String key, String defaultValue) {
        ensureLoaded();
        return properties.getOrDefault(key, defaultValue);
    }

    public int getInt(String key) {
        ensureLoaded();
        try {
            return Integer.parseInt(properties.get(key));
        } catch (Exception e) {
            throw new PropertyException("Invalid int value for key: " + key);
        }
    }

    public boolean getBoolean(String key) {
        ensureLoaded();
        return Boolean.parseBoolean(properties.get(key));
    }

    public Map<String, String> asMap() {
        ensureLoaded();
        return Collections.unmodifiableMap(properties);
    }
}
