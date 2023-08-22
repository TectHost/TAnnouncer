package minealex.tannouncer.titles;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Titles {

    public static void sendTitleToAllPlayers(FileConfiguration announcementsConfig, int titleNumber) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendTitle(player, announcementsConfig, titleNumber);
        }
    }

    public static void sendTitle(Player player, FileConfiguration announcementsConfig, int titleNumber) {
        String titlePath = "title" + titleNumber;
        if (announcementsConfig.isConfigurationSection(titlePath)) {
            ConfigurationSection titleSection = announcementsConfig.getConfigurationSection(titlePath);
            String titleText = ChatColor.translateAlternateColorCodes('&', titleSection.getString("text"));
            int fadeIn = titleSection.getInt("fadeIn", 10);
            int stay = titleSection.getInt("stay", 40);
            int fadeOut = titleSection.getInt("fadeOut", 10);

            CraftPlayer craftPlayer = (CraftPlayer) player;
            PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatComponentText(titleText), fadeIn, stay, fadeOut);
            craftPlayer.getHandle().playerConnection.sendPacket(titlePacket);
        }
    }

    public static void setup(FileConfiguration announcementsConfig) {
        // No es necesario hacer nada aquí, ya que ahora enviamos los títulos directamente desde el método onEnable de TAnnouncer.java.
    }
}
