package arthessia.notificator.telegram;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import arthessia.notificator.Plugin;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Telegram implements Listener {

    private final Plugin plugin;

    private static Map<String, LocalDateTime> AVOID_SPAM = new HashMap<>();
    private static Map<String, LocalDateTime> LIFECYCLE = new HashMap<>();
    private static Map<String, LocalDateTime> DEATH_TIME = new HashMap<>();
    private static Random RANDOM = new Random();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!plugin.getConfig().getBoolean("notif.death.enabled")) {
            return;
        }

        String player = event.getEntity().getName();
        LocalDateTime deathTime = LocalDateTime.now();
        LocalDateTime spawnTime = LIFECYCLE.getOrDefault(player, deathTime);
        Duration duration = Duration.between(spawnTime, deathTime);

        plugin.getLogger().info(event.getDeathMessage() + ", sending update on Telegram.");
        String customMessage = plugin.getConfig().getString("notif.message.death");
        String finalMessage = customMessage.replace("%msg%", event.getDeathMessage());
        if (plugin.getConfig().getBoolean("notif.death.timer.enabled")) {
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            long seconds = duration.getSeconds() % 60;

            String lifetimeFormatted;
            if (hours > 0) {
                lifetimeFormatted = String.format("%dh %dm %ds", hours, minutes, seconds);
            } else if (minutes > 0) {
                lifetimeFormatted = String.format("%dm %ds", minutes, seconds);
            } else {
                lifetimeFormatted = String.format("%ds", seconds);
            }
            String customDeathTimeMessage = plugin.getConfig().getString("notif.message.deathTime");
            String finalDeathTimeMessage = customDeathTimeMessage.replace("%msg%", lifetimeFormatted);
            finalMessage += " " + finalDeathTimeMessage;
        }
        String bot = plugin.getConfig().getString("bot.token");
        String chatId = plugin.getConfig().getString("bot.chat");
        boolean isSilent = plugin.getConfig().getBoolean("notif.silent.enabled");
        DEATH_TIME.put(player, deathTime);
        LIFECYCLE.put(player, deathTime);
        plugin.sendMessage(bot, chatId, finalMessage, isSilent);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!plugin.getConfig().getBoolean("notif.join.enabled")) {
            return;
        }

        LocalDateTime delay = AVOID_SPAM.getOrDefault("join-" + event.getPlayer().getName(),
                LocalDateTime.now().minusSeconds(1));

        LIFECYCLE.put(event.getPlayer().getName(), LocalDateTime.now());

        if (delay.isBefore(LocalDateTime.now())) {
            plugin.getLogger().info(event.getPlayer().getName() + " just arrived, sending update on Telegram.");
            String customMessage = plugin.getConfig().getString("notif.message.join");
            String finalMessage = customMessage.replace("%msg%", event.getPlayer().getName());
            String bot = plugin.getConfig().getString("bot.token");
            String chatId = plugin.getConfig().getString("bot.chat");
            boolean isSilent = plugin.getConfig().getBoolean("notif.silent.enabled");
            plugin.sendMessage(bot, chatId, finalMessage, isSilent);
        }
        AVOID_SPAM.put("join-" + event.getPlayer().getName(),
                LocalDateTime.now().plusMinutes(plugin.getConfig().getLong("notif.join.delay")));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!plugin.getConfig().getBoolean("notif.quit.enabled")) {
            return;
        }
        String player = event.getPlayer().getName();
        LocalDateTime delay = AVOID_SPAM.getOrDefault("quit-" + player,
                LocalDateTime.now().minusSeconds(1));
        String bot = plugin.getConfig().getString("bot.token");
        String chatId = plugin.getConfig().getString("bot.chat");
        boolean isSilent = plugin.getConfig().getBoolean("notif.silent.enabled");

        boolean rage = false;
        if (plugin.getConfig().getBoolean("notif.rage.enabled") && DEATH_TIME.containsKey(player)) {
            List<String> rageMessages = plugin.getConfig().getStringList("notif.message.rage");
            if (Duration.between(DEATH_TIME.get(player), LocalDateTime.now()).toSeconds() < plugin.getConfig()
                    .getLong("notif.rage.delay") && !rageMessages.isEmpty()) {
                rage = true;
                String customMessage = rageMessages.get(RANDOM.nextInt(rageMessages.size()));
                Location dl = event.getPlayer().getLastDeathLocation();
                String location = dl.getWorld().getName() + " x:" + dl.getX() + " y:" + dl.getY() + " z:" + dl.getZ();
                String finalMessage = customMessage.replace("%msg%", player).replace("%location%",
                        location);
                plugin.sendMessage(bot, chatId, finalMessage, isSilent);
                DEATH_TIME.remove(player);
            }
        }

        if (delay.isBefore(LocalDateTime.now()) && !rage) {
            plugin.getLogger().info(player + " just left, sending update on Telegram.");
            String customMessage = plugin.getConfig().getString("notif.message.quit");
            String finalMessage = customMessage.replace("%msg%", player);
            plugin.sendMessage(bot, chatId, finalMessage, isSilent);
        }

        AVOID_SPAM.put("quit-" + player,
                LocalDateTime.now().plusMinutes(plugin.getConfig().getLong("notif.quit.delay")));
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

            plugin.getLogger().info(event.getPlayer().getName() + " got a success, sending update on Telegram.");
            String customMessage = plugin.getConfig().getString("notif.message.success");
            String finalMessage = customMessage
                    .replace("%msg%", event.getPlayer().getName())
                    .replace("%success%", toDisplay);
            String bot = plugin.getConfig().getString("bot.token");
            String chatId = plugin.getConfig().getString("bot.chat");
            boolean isSilent = plugin.getConfig().getBoolean("notif.silent.enabled");
            plugin.sendMessage(bot, chatId, finalMessage, isSilent);
        }
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        if (!plugin.getConfig().getBoolean("notif.chat.enabled")) {
            return;
        }
        String bot = plugin.getConfig().getString("bot.token");
        String chatId = plugin.getConfig().getString("bot.chat");
        boolean isSilent = plugin.getConfig().getBoolean("notif.silent.enabled");
        String customMessage = plugin.getConfig().getString("notif.message.chat");
        String finalMessage = customMessage
                .replace("%msg%", event.getPlayer().getName())
                .replace("%message%", event.getMessage());
        plugin.sendMessage(bot, chatId, finalMessage, isSilent);
    }
}
