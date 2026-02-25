package de.max.botproperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Combined BotProperties Library
 * Minimalist and type-safe property loader for Java Bots.
 */
public class BotProperties {
    private static final Logger LOGGER = Logger.getLogger(BotProperties.class.getName());
    private final Properties properties = new Properties();

    private BotProperties(String fileName) {
        loadProperties(fileName);
    }

    /**
     * Factory-Methode zum Laden der Eigenschaften.
     * @param fileName Name der Datei (z.B. "bot.properties")
     * @return Eine Instanz von BotProperties
     */
    public static BotProperties load(String fileName) {
        return new BotProperties(fileName);
    }

    private void loadProperties(String fileName) {
        try (InputStream input = new FileInputStream(fileName)) {
            properties.load(input);
            LOGGER.info("Konfiguration erfolgreich aus " + fileName + " geladen.");
        } catch (IOException e) {
            throw new BotPropertyException("Fehler beim Laden der Datei: " + fileName, e);
        }
    }

    /**
     * Holt einen String-Wert.
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            LOGGER.warning("Property '" + key + "' nicht gefunden!");
        }
        return value;
    }

    /**
     * Holt einen String-Wert mit Fallback.
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Holt einen Integer-Wert.
     */
    public int getInt(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new BotPropertyException("Property '" + key + "' ist keine gültige Zahl: " + value);
        }
    }

    /**
     * Holt einen Boolean-Wert.
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    /**
     * Interne Exception-Klasse für Konfigurationsfehler.
     */
    public static class BotPropertyException extends RuntimeException {
        public BotPropertyException(String message) {
            super(message);
        }

        public BotPropertyException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
