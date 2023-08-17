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
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("tannouncer.reload")) {
                plugin.reloadConfig();
                String reloadMessage = messagesConfig.getString("reload_config", "&5TAnnouncer &e> &aConfig reloaded.");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', reloadMessage));
            } else {
                String noPermissionMessage = messagesConfig.getString("no_permission", "&5TAnnouncer &e> &cYou don't have permission to execute this command.");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermissionMessage));
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
            String versionMessage = messagesConfig.getString("version_message", "&5TAnnouncer &e> &aTAnnouncer version: &7{{version}}")
                    .replace("{{version}}", plugin.getDescription().getVersion());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', versionMessage));
            return true;
        }
        return false;
    }
}
