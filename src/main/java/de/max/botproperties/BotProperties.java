package de.max.botproperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hochwertiger Property-Loader im Stil von dotenv.
 * Unterstützt .properties und .env Formate sowie System-Overrides.
 */
public class BotProperties {
    private static final Logger LOGGER = Logger.getLogger("BotConfig");
    private final Map<String, String> config = new HashMap<>();
    private final String sourceName;

    private BotProperties(String path, boolean silent) {
        this.sourceName = path;
        if (Files.exists(Paths.get(path))) {
            loadFromFile(path);
        } else if (!silent) {
            LOGGER.log(Level.WARNING, "Konfigurationsdatei {0} nicht gefunden. Nutze nur System-Umgebungsvariablen.", path);
        }
    }

    /**
     * Erstellt eine neue Instanz. Sucht standardmäßig nach der Datei.
     */
    public static BotProperties load(String path) {
        return new BotProperties(path, false);
    }

    /**
     * Lädt die Datei und parst sie (unterstützt KEY=VALUE und Kommentare).
     */
    private void loadFromFile(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) continue;

                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    config.put(parts[0].trim(), parts[1].trim());
                } else {
                    LOGGER.log(Level.WARNING, "Ungültiges Format in {0} Zeile {1}: {2}", new Object[]{path, lineNum, line});
                }
            }
            LOGGER.log(Level.INFO, "Erfolgreich {0} Einträge aus {1} geladen.", new Object[]{config.size(), path});
        } catch (IOException e) {
            throw new ConfigException("Fehler beim Lesen der Datei: " + path, e);
        }
    }

    /**
     * Kern-Logik: Sucht in Datei, dann in Umgebungsvariablen (UPPER_CASE).
     */
    public Optional<String> get(String key) {
        // 1. Priorität: Datei
        String value = config.get(key);
        
        // 2. Priorität: System Environment (z.B. bot.token -> BOT_TOKEN)
        if (value == null || value.isEmpty()) {
            String envKey = key.toUpperCase().replace(".", "_").replace("-", "_");
            value = System.getenv(envKey);
        }
        
        return Optional.ofNullable(value);
    }

    /**
     * Holt einen Wert oder wirft eine detaillierte Exception, wenn er fehlt.
     */
    public String getOrThrow(String key) {
        return get(key).orElseThrow(() -> new ConfigException(
            String.format("Kritischer Fehler: Konfiguration '%s' fehlt in %s und im System-Environment!", key, sourceName)
        ));
    }

    public int getInt(String key) {
        String val = getOrThrow(key);
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            throw new ConfigException("Key '" + key + "' soll eine Zahl sein, ist aber: " + val);
        }
    }

    public boolean getBoolean(String key) {
        String val = getOrThrow(key);
        return val.equalsIgnoreCase("true") || val.equalsIgnoreCase("1") || val.equalsIgnoreCase("yes");
    }

    // --- Exception Handling ---
    public static class ConfigException extends RuntimeException {
        public ConfigException(String message) { super(message); }
        public ConfigException(String message, Throwable cause) { super(message, cause); }
    }
}
