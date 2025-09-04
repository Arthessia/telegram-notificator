package arthessia.notificator.telegram;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import arthessia.notificator.Plugin;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Telegram implements Listener {

    private final Plugin plugin;

    private Map<String, LocalDateTime> AVOID_SPAM = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!plugin.getConfig().getBoolean("notif.death.enabled")) {
            return;
        }
        plugin.getLogger().info(event.getDeathMessage() + ", sending update on Telegram.");
        String customMessage = plugin.getConfig().getString("notif.message.death");
        String finalMessage = customMessage.replace("%msg%", event.getDeathMessage());
        String bot = plugin.getConfig().getString("notif.bot.token");
        String chatId = plugin.getConfig().getString("notif.bot.chat");
        boolean isSilent = plugin.getConfig().getBoolean("notif.silent.enabled");
        plugin.sendMessage(bot, chatId, finalMessage, isSilent);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!plugin.getConfig().getBoolean("notif.join.enabled")) {
            return;
        }

        LocalDateTime delay = AVOID_SPAM.getOrDefault("join-" + event.getPlayer().getDisplayName(),
                LocalDateTime.now().minusSeconds(1));

        if (delay.isBefore(LocalDateTime.now())) {
            plugin.getLogger().info(event.getPlayer().getDisplayName() + " just arrived, sending update on Telegram.");
            String customMessage = plugin.getConfig().getString("notif.message.join");
            String finalMessage = customMessage.replace("%msg%", event.getPlayer().getDisplayName());
            String bot = plugin.getConfig().getString("notif.bot.token");
            String chatId = plugin.getConfig().getString("notif.bot.chat");
            boolean isSilent = plugin.getConfig().getBoolean("notif.silent.enabled");
            plugin.sendMessage(bot, chatId, finalMessage, isSilent);
        }
        AVOID_SPAM.put("join-" + event.getPlayer().getDisplayName(),
                LocalDateTime.now().plusMinutes(plugin.getConfig().getLong("notif.delay")));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!plugin.getConfig().getBoolean("notif.quit.enabled")) {
            return;
        }

        LocalDateTime delay = AVOID_SPAM.getOrDefault("quit-" + event.getPlayer().getDisplayName(),
                LocalDateTime.now().minusSeconds(1));

        if (delay.isBefore(LocalDateTime.now())) {
            plugin.getLogger().info(event.getPlayer().getDisplayName() + " just left, sending update on Telegram.");
            String customMessage = plugin.getConfig().getString("notif.message.quit");
            String finalMessage = customMessage.replace("%msg%", event.getPlayer().getDisplayName());
            String bot = plugin.getConfig().getString("notif.bot.token");
            String chatId = plugin.getConfig().getString("notif.bot.chat");
            boolean isSilent = plugin.getConfig().getBoolean("notif.silent.enabled");
            plugin.sendMessage(bot, chatId, finalMessage, isSilent);
        }

        AVOID_SPAM.put("quit-" + event.getPlayer().getDisplayName(),
                LocalDateTime.now().plusMinutes(plugin.getConfig().getLong("notif.delay")));
    }

    @EventHandler
    public void onSuccess(PlayerAdvancementDoneEvent event) {
        if (!plugin.getConfig().getBoolean("notif.success.enabled")) {
            return;
        }

        if (event.getAdvancement() == null ||
                event.getAdvancement().getKey() == null ||
                event.getAdvancement().getKey().getKey() == null ||
                !event.getAdvancement().getKey().getKey().contains("/")) {
            return;
        }

        String type = event.getAdvancement().getKey().getKey().split("/")[0];
        List<String> successToCommunicate = plugin.getConfig().getStringList("notif.success.categories");

        if (successToCommunicate.contains(type)) {
            String toDisplay = event.getAdvancement().getKey().getKey()
                    .replace(type + "/", "")
                    .replaceAll("_", " ");

            if (toDisplay.equals("root"))
                return;

            plugin.getLogger().info(event.getPlayer().getDisplayName() + " got a success, sending update on Telegram.");
            String customMessage = plugin.getConfig().getString("notif.message.success");
            String finalMessage = customMessage
                    .replace("%msg%", event.getPlayer().getDisplayName())
                    .replace("%success%", toDisplay);
            String bot = plugin.getConfig().getString("notif.bot.token");
            String chatId = plugin.getConfig().getString("notif.bot.chat");
            boolean isSilent = plugin.getConfig().getBoolean("notif.silent.enabled");
            plugin.sendMessage(bot, chatId, finalMessage, isSilent);
        }
    }
}
