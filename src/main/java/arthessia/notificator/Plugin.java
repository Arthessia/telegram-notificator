package arthessia.notificator;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import arthessia.notificator.commands.CommonCommands.ReloadCommand;
import arthessia.notificator.commands.FreeMessage.FreeMessageSender;
import arthessia.notificator.telegram.Telegram;
import arthessia.notificator.telegram.TelegramBridgeBot;

public class Plugin extends JavaPlugin implements Listener {
    public static final Random RANDOM = new Random();
    private static final HttpClient client = HttpClient.newHttpClient();

    @Override
    public void onEnable() {
        getLogger().info("Notificator loading...");
        this.saveDefaultConfig();
        getLogger().info("Config loaded... ");

        getLogger().info("Setup of new default values...");
        if (!this.getConfig().contains("communication.enabled")) {
            this.getConfig().set("communication.enabled", true);
        }
        if (!this.getConfig().contains("notif.death.enabled")) {
            this.getConfig().set("notif.death.enabled", true);
        }
        if (!this.getConfig().contains("notif.death.timer.enabled")) {
            this.getConfig().set("notif.death.timer.enabled", true);
        }
        if (!this.getConfig().contains("notif.chat.enabled")) {
            this.getConfig().set("notif.chat.enabled", true);
        }
        if (!this.getConfig().contains("notif.free.enabled")) {
            this.getConfig().set("notif.free.enabled", true);
        }
        if (!this.getConfig().contains("notif.join.enabled")) {
            this.getConfig().set("notif.join.enabled", true);
        }
        if (!this.getConfig().contains("notif.quit.enabled")) {
            this.getConfig().set("notif.quit.enabled", true);
        }
        if (!this.getConfig().contains("notif.rage.enabled")) {
            this.getConfig().set("notif.rage.enabled", true);
        }
        if (!this.getConfig().contains("notif.shutdown.enabled")) {
            this.getConfig().set("notif.shutdown.enabled", true);
        }
        if (!this.getConfig().contains("notif.success.enabled")) {
            this.getConfig().set("notif.success.enabled", true);
        }
        if (!this.getConfig().contains("notif.success.categories")) {
            List<String> init = new ArrayList<>();
            init.add("adventure");
            init.add("husbandry");
            init.add("story");
            init.add("nether");
            init.add("end");
            this.getConfig().set("notif.success.categories", init);
        }
        if (!this.getConfig().contains("notif.message.rage")) {
            List<String> init = new ArrayList<>();
            init.add("🖥️👉🪟 `I think %msg% just threw their PC out the window, I lost the connection.`");
            init.add("🍼👶 `%msg% ragequit again... someone hand them a pacifier?`");
            init.add("🪦💀 `%msg% died… again. But this time they ragequit before respawning.`");
            init.add("🦥💤 `%msg% ragequit faster than they play. Impressive.`");
            init.add("🏆🥈 `%msg% thought they were a pro, but disconnecting was faster than their skills.`");
            this.getConfig().set("notif.message.rage", init);
        }
        if (!this.getConfig().contains("notif.message.chat")) {
            this.getConfig().set("notif.message.chat", "💬 `%msg%`> %message%");
        }
        if (!this.getConfig().contains("notif.message.death")) {
            this.getConfig().set("notif.message.death", "☠️ `%msg%`");
        }
        if (!this.getConfig().contains("notif.message.deathTime")) {
            this.getConfig().set("notif.message.deathTime", "⏱️ `survived for %msg%`");
        }
        if (!this.getConfig().contains("notif.message.join")) {
            this.getConfig().set("notif.message.join", "💎⛏️ `%msg% just joined the game.`");
        }
        if (!this.getConfig().contains("notif.message.quit")) {
            this.getConfig().set("notif.message.quit", "🌒 `%msg% has left the game.`");
        }
        if (!this.getConfig().contains("notif.message.shutdown")) {
            this.getConfig().set("notif.message.shutdown", "♻️🔋 `Server is shutting down.`");
        }
        if (!this.getConfig().contains("notif.message.success")) {
            this.getConfig().set("notif.message.success", "🏆 `%msg% just earned the achievement %success%! GG!`");
        }
        if (!this.getConfig().contains("bot.token")) {
            this.getConfig().set("bot.token", "dummytoken");
        }
        if (!this.getConfig().contains("bot.chat")) {
            this.getConfig().set("bot.chat", "dummyid");
        }
        if (!this.getConfig().contains("bot.username")) {
            this.getConfig().set("bot.username", "username");
        }
        if (!this.getConfig().contains("notif.silent.enabled")) {
            this.getConfig().set("notif.silent.enabled", true);
        }
        if (!this.getConfig().contains("notif.join.delay")) {
            this.getConfig().set("notif.join.delay", 15);
        }
        if (!this.getConfig().contains("notif.quit.delay")) {
            this.getConfig().set("notif.quit.delay", 15);
        }
        if (!this.getConfig().contains("notif.rage.delay")) {
            this.getConfig().set("notif.rage.delay", 30);
        }
        this.saveConfig();

        // load commands
        this.getCommand("notificator").setExecutor(new ReloadCommand(this));
        this.getCommand("send").setExecutor(new FreeMessageSender(this));
        getLogger().info("Commands loaded...");

        // load plugins
        Telegram telegram = new Telegram(this);
        Bukkit.getServer().getPluginManager().registerEvents(telegram, this);
        String token = getConfig().getString("bot.token");
        long chatId = getConfig().getLong("bot.chatid", 0L);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBridgeBot(this, chatId, token));
        } catch (TelegramApiException e) {
            getLogger().severe("Erreur lors du lancement du bot Telegram : " + e.getMessage());
        }

        getLogger().info("Notificator loaded...");
    }

    @Override
    public void saveConfig() {
        super.saveConfig();
    }

    @Override
    public FileConfiguration getConfig() {
        return super.getConfig();
    }

    @Override
    public void onDisable() {
        if (getConfig().getBoolean("notif.shutdown.enabled")) {
            String bot = getConfig().getString("bot.token");
            String chatId = getConfig().getString("bot.chat");
            boolean isSilent = getConfig().getBoolean("notif.silent.enabled");
            String customMessage = getConfig().getString("notif.message.shutdown");

            sendMessage(bot, chatId, customMessage, isSilent);
        }
        this.saveConfig();
        getLogger().info("Notificator shutdown...");
    }

    public void sendMessage(String botToken, String chatId, String text, boolean isSilent) {
        try {
            String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

            String form = "chat_id=" + URLEncoder.encode(chatId, StandardCharsets.UTF_8)
                    + "&text=" + URLEncoder.encode(text, StandardCharsets.UTF_8)
                    + "&parse_mode=Markdown"
                    + "&disable_notification=" + isSilent;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            this.getLogger().info(response.body());
        } catch (Exception e) {
            this.getLogger().warning(e.getMessage());
        }
    }
}
