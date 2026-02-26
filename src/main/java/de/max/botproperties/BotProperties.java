package de.max.botproperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * High-performance property loader with environment variable priority.
 */
public class BotProperties {
    private static final Logger LOGGER = Logger.getLogger("BotConfig");
    private final Map<String, String> fileConfig = new HashMap<>();
    private final String sourcePath;

    private BotProperties(String path) {
        this.sourcePath = path;
        if (Files.exists(Paths.get(path))) {
            loadFile(path);
        } else {
            LOGGER.log(Level.INFO, "No config file found at {0}. Using environment variables.", path);
        }
    }

    /**
     * Creates a new instance and loads properties from the given path.
     * @param path The path to the properties or .env file.
     * @return A new BotProperties instance.
     */
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
        } catch (IOException e) {
            throw new ConfigException("Failed to read config file: " + path, e);
        }
    }

    /**
     * Resolves a value by key. Priority: Environment Variables > File.
     * @param key The configuration key.
     * @return An Optional containing the value if found.
     */
    public Optional<String> get(String key) {
        String envKey = key.toUpperCase().replace(".", "_").replace("-", "_");
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isEmpty()) return Optional.of(envValue);
        return Optional.ofNullable(fileConfig.get(key));
    }

    /**
     * Gets a value or returns a default.
     * @param key The configuration key.
     * @param defaultValue The fallback value.
     * @return The configuration value or default.
     */
    public String getOrDefault(String key, String defaultValue) {
        return get(key).orElse(defaultValue);
    }

    /**
     * Gets a value or throws an exception if missing.
     * @param key The configuration key.
     * @return The configuration value.
     * @throws ConfigException if key is not found.
     */
    public String getOrThrow(String key) {
        return get(key).orElseThrow(() -> new ConfigException("Missing required configuration: " + key));
    }

    /**
     * Retrieves an ID or Long value.
     * @param key The configuration key.
     * @return The value as a Long.
     */
    public Long getLong(String key) {
        String val = getOrThrow(key);
        try {
            return Long.parseLong(val);
        } catch (NumberFormatException e) {
            throw new ConfigException("Key '" + key + "' must be a Long, but was: " + val);
        }
    }

    /**
     * Retrieves an integer value.
     * @param key The configuration key.
     * @return The value as an int.
     */
    public int getInt(String key) {
        String val = getOrThrow(key);
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            throw new ConfigException("Key '" + key + "' must be an Integer, but was: " + val);
        }
    }

    /**
     * Retrieves a boolean value.
     * @param key The configuration key.
     * @return True if value is 'true', '1', or 'yes'.
     */
    public boolean getBoolean(String key) {
        String val = getOrDefault(key, "false");
        return val.equalsIgnoreCase("true") || val.equalsIgnoreCase("1") || val.equalsIgnoreCase("yes");
    }

    /**
     * Retrieves a comma-separated list of values.
     * @param key The configuration key.
     * @return A list of trimmed strings.
     */
    public List<String> getList(String key) {
        return get(key)
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .filter(item -> !item.isEmpty())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    /**
     * Custom exception for configuration errors.
     */
    public static class ConfigException extends RuntimeException {
        /** * Exception with message.
         * @param message The error message. 
         */
        public ConfigException(String message) { 
            super(message); 
        }

        /** * Exception with message and cause.
         * @param message The error message. 
         * @param cause The root cause.
         */
        public ConfigException(String message, Throwable cause) { 
            super(message, cause); 
        }
    }
}
