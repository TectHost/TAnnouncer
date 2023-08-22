package minealex.tannouncer;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import minealex.tannouncer.commands.Commands;
import minealex.tannouncer.titles.Titles;

import java.util.List;

public class TAnnouncer extends JavaPlugin implements Listener {

    private ConfigManager configManager;
    private int taskId;
    private static int currentTitle = 1;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.setup();

        getServer().getPluginManager().registerEvents(this, this);

        getCommand("tannouncer").setExecutor(new Commands(this));

        Titles.setup(configManager.getAnnouncementsConfig());

        int announcementInterval = configManager.getAnnouncementsConfig().getInt("announcement_interval", 10);
        List<String> announcementText = configManager.getAnnouncementsConfig().getStringList("announcement_text");
        int titleCount = 3;

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            boolean announcementsEnabled = configManager.getMainConfig().getBoolean("announcements_enabled", true);

            if (announcementsEnabled) {
                Titles.sendTitleToAllPlayers(configManager.getAnnouncementsConfig(), currentTitle);
                currentTitle++;
                if (currentTitle > titleCount) {
                    currentTitle = 1;
                }

                for (String line : announcementText) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        String messageWithColor = ChatColor.translateAlternateColorCodes('&', line.replace("%player%", player.getName()));

                        if (player instanceof Player) {
                            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                                messageWithColor = PlaceholderAPI.setPlaceholders((Player) player, messageWithColor);
                            }
                        }

                        player.sendMessage(messageWithColor);
                    }
                }
            }
        }, 0L, announcementInterval * 20L);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
