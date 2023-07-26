package minealex.tannouncer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import minealex.tannouncer.commands.Commands;

import java.util.List;

public class TAnnouncer extends JavaPlugin implements Listener {

    private ConfigManager configManager;
    private int taskId;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.setup();

        // Registrar el Listener para el evento
        getServer().getPluginManager().registerEvents(this, this);

        // Registrar el comando /tannouncer reload
        getCommand("tannouncer").setExecutor(new Commands(this));

        // Cargar el tiempo de anuncio desde el archivo announcements.yml
        int announcementInterval = configManager.getAnnouncementsConfig().getInt("announcement_interval", 10);

        // Iniciar la tarea de envío de anuncios cada 10 segundos (o el valor del archivo)
        List<String> announcementText = configManager.getAnnouncementsConfig().getStringList("announcement_text");
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            // Verificar si los anuncios están habilitados en el archivo config.yml
            boolean announcementsEnabled = configManager.getMainConfig().getBoolean("announcements_enabled", true);

            if (announcementsEnabled) {
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

    // Evento para cancelar el chat en la consola
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("tannouncer.bypass")) {
            return; // Permitir mensajes de jugadores con permiso "tannouncer.bypass"
        }
        event.setCancelled(true);
    }
}
