package de.max.botproperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BotProperties {
    private static final Logger LOGGER = Logger.getLogger("BotConfig");
    private final Map<String, String> fileConfig = new HashMap<>();
    private final String sourcePath;

    private BotProperties(String path) {
        this.sourcePath = path;
        if (Files.exists(Paths.get(path))) {
            loadFile(path);
        } else {
            LOGGER.log(Level.INFO, "No config file found at {0}. Relying on environment variables.", path);
        }
    }

    public static BotProperties load(String path) {
        return new BotProperties(path);
    }

    private void loadFile(String path) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) continue;

                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    fileConfig.put(parts[0].trim(), parts[1].trim());
                }
            }
            LOGGER.log(Level.INFO, "Loaded {0} properties from {1}", new Object[]{fileConfig.size(), path});
        } catch (IOException e) {
            throw new ConfigException("Failed to read config file: " + path, e);
        }
    }

    /**
     * Resolves the value. Priority: 1. System Environment, 2. File
     */
    public Optional<String> get(String key) {
        // Environment variables usually use UPPER_SNAKE_CASE (e.g., BOT_TOKEN for bot.token)
        String envKey = key.toUpperCase().replace(".", "_").replace("-", "_");
        String envValue = System.getenv(envKey);

        if (envValue != null && !envValue.isEmpty()) {
            return Optional.of(envValue);
        }

        return Optional.ofNullable(fileConfig.get(key));
    }

    public String getOrDefault(String key, String defaultValue) {
        return get(key).orElse(defaultValue);
    }

    public String getOrThrow(String key) {
        return get(key).orElseThrow(() -> new ConfigException("Missing required configuration: " + key));
    }

    // --- Type Specific Getters ---

    /**
     * Essential for IDs as requested.
     */
    public Long getLong(String key) {
        String val = getOrThrow(key);
        try {
            return Long.parseLong(val);
        } catch (NumberFormatException e) {
            throw new ConfigException("Key '" + key + "' must be a Long (ID), but was: " + val);
        }
    }

    public int getInt(String key) {
        String val = getOrThrow(key);
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            throw new ConfigException("Key '" + key + "' must be an Integer, but was: " + val);
        }
    }

    public boolean getBoolean(String key) {
        String val = getOrDefault(key, "false");
        return val.equalsIgnoreCase("true") || val.equalsIgnoreCase("1") || val.equalsIgnoreCase("yes");
    }

    public List<String> getList(String key) {
        return get(key)
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .filter(item -> !item.isEmpty())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    // --- Custom Exception ---
    public static class ConfigException extends RuntimeException {
        public ConfigException(String message) { super(message); }
        public ConfigException(String message, Throwable cause) { super(message, cause); }
    }
}
