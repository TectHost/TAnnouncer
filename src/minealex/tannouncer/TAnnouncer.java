package minealex.tannouncer;

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
    private static int currentTitle = 1; // Variable para mantener el contador de títulos

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.setup();

        // Registrar el Listener para el evento
        getServer().getPluginManager().registerEvents(this, this);

        // Registrar el comando /tannouncer reload
        getCommand("tannouncer").setExecutor(new Commands(this));

        // Cargar la configuración del título de anuncio desde el archivo announcements.yml
        Titles.setup(configManager.getAnnouncementsConfig()); // Configurar la instancia de la configuración

        // Iniciar la tarea de envío de anuncios cada 10 segundos (o el valor del archivo)
        int announcementInterval = configManager.getAnnouncementsConfig().getInt("announcement_interval", 10);
        List<String> announcementText = configManager.getAnnouncementsConfig().getStringList("announcement_text");
        int titleCount = 3; // Cambiar esto al número total de títulos definidos en el archivo announcements.yml

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            // Verificar si los anuncios están habilitados en el archivo config.yml
            boolean announcementsEnabled = configManager.getMainConfig().getBoolean("announcements_enabled", true);

            if (announcementsEnabled) {
                // Enviar el título correspondiente en cada intervalo
                Titles.sendTitleToAllPlayers(configManager.getAnnouncementsConfig(), currentTitle);

                // Incrementar el contador de títulos para el siguiente intervalo
                currentTitle++;
                if (currentTitle > titleCount) {
                    currentTitle = 1;
                }

                // Enviar el anuncio a todos los jugadores
                for (String line : announcementText) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        String messageWithColor = ChatColor.translateAlternateColorCodes('&', line.replace("%player%", player.getName()));
                        player.sendMessage(messageWithColor);
                    }
                }
            }
        }, 0L, announcementInterval * 20L);
    }

    @Override
    public void onDisable() {
        // Cancelar la tarea de envío de anuncios al desactivar el plugin
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
