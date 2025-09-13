package arthessia.notificator.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import arthessia.notificator.Plugin;
import lombok.RequiredArgsConstructor;

public class FreeMessage {

    @RequiredArgsConstructor
    public static class FreeMessageSender implements CommandExecutor {

        private final Plugin plugin;

        @Override
        public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
            if (!sender.hasPermission("arthessia.notificator.send")) {
                sender.sendMessage("You don't have the permission.");
                return false;
            }

            if (!plugin.getConfig().getBoolean("notif.free.enabled")) {
                return false;
            }

            String bot = plugin.getConfig().getString("bot.token");
            String chatId = plugin.getConfig().getString("bot.chat");
            boolean isSilent = plugin.getConfig().getBoolean("notif.silent.enabled");
            plugin.sendMessage(bot, chatId, String.join(" ", arg3), isSilent);

            return true;
        }
    }
}
