package minealex.tannouncer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration announcementsConfig;
    private FileConfiguration mainConfig;
    private File announcementsFile;
    private File mainFile;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        mainFile = new File(plugin.getDataFolder(), "config.yml");
        announcementsFile = new File(plugin.getDataFolder(), "announcements.yml");

        if (!mainFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        if (!announcementsFile.exists()) {
            plugin.saveResource("announcements.yml", false);
        }

        mainConfig = YamlConfiguration.loadConfiguration(mainFile);
        announcementsConfig = YamlConfiguration.loadConfiguration(announcementsFile);
    }

    public FileConfiguration getAnnouncementsConfig() {
        return announcementsConfig;
    }

    public FileConfiguration getMainConfig() {
        return mainConfig;
    }

    public void saveAnnouncementsConfig() {
        try {
            announcementsConfig.save(announcementsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
