package arthessia.notificator.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import arthessia.notificator.Plugin;
import lombok.RequiredArgsConstructor;

public class CommonCommands {

    @RequiredArgsConstructor
    public static class ReloadCommand implements CommandExecutor {

        private final Plugin plugin;

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender.hasPermission("arthessia.notificator.reload") &&
                    args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                // Logique de rechargement du plugin
                plugin.reloadConfig();
                sender.sendMessage("Plugin has been reloaded.");
                return true;
            }
            return false;
        }
    }
}
