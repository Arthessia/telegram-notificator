package arthessia.notificator.telegram;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBridgeBot extends TelegramLongPollingBot {

    private final Plugin plugin;
    private final long allowedChatId;

    public TelegramBridgeBot(Plugin plugin, long allowedChatId, String botToken) {
        super(botToken);
        this.plugin = plugin;
        this.allowedChatId = allowedChatId;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update == null || !update.hasMessage())
            return;

        Message msg = update.getMessage();
        if (!msg.hasText())
            return;

        long chatId = msg.getChatId();
        // optional: ignore messages from other chats
        if (allowedChatId != 0 && chatId != allowedChatId)
            return;

        String text = msg.getText();
        // optional: ignore bot commands
        if (text.startsWith("/"))
            return;

        String author = msg.getFrom().getFirstName();
        if (msg.getFrom().getLastName() != null) {
            author += " " + msg.getFrom().getLastName();
        }
        if (msg.getFrom().getUserName() != null) {
            author += " (@" + msg.getFrom().getUserName() + ")";
        }

        final String mcMessage = "§b[Telegram] §r" + author + ": " + text;

        // must run on main server thread
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(mcMessage);
            }
        }.runTask(plugin);
    }

    @Override
    public String getBotUsername() {
        return plugin.getConfig().getString("bot.username");
    }

    @Override
    public String getBotToken() {
        return plugin.getConfig().getString("bot.token");
    }
}
