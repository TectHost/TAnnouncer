package minealex.tannouncer.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class Commands implements CommandExecutor {

    private final JavaPlugin plugin;
    private final ConfigurationSection messagesConfig;

    public Commands(JavaPlugin plugin) {
        this.plugin = plugin;
        this.messagesConfig = plugin.getConfig().getConfigurationSection("messages");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tannouncer")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("tannouncer.reload")) {
                    plugin.reloadConfig();
                    String reloadMessage = messagesConfig.getString("reload_config", "&aConfig reloaded.");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', reloadMessage));
                } else {
                    String noPermissionMessage = messagesConfig.getString("no_permission", "&cYou don't have permissions to execute this command.");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermissionMessage));
                }
                return true;
            }
        }
        return false;
    }
}
