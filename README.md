# Telegram Notificator

Bring your Minecraft server to life with **Telegram Notificator** – the ultimate bridge between your in-game events and your Telegram community!

## 🚀 Features

- **Real-Time Telegram Notifications:** Instantly send messages to your Telegram chat for player deaths, joins, quits, achievements, and more.
- **Customizable Messages:** Personalize every notification with fun, themed messages and placeholders.
- **Ragequit Detector:** Celebrate (or mock!) dramatic ragequits with a random selection of witty messages.
- **Silent Mode:** Choose whether notifications should ping your Telegram group or stay discreet.
- **Command Support:** Reload the plugin or send free-form messages to Telegram directly from Minecraft.
- **Flexible Configuration:** Easily tweak every aspect via the included `config.yml`.

## 📦 Installation

1. Download the plugin `.jar` and drop it into your server’s `plugins` folder.
2. Start your server to generate the default configuration.
3. Edit `plugins/notificator/config.yml` with your Telegram bot token, chat ID, and preferences.
4. Reload or restart your server.

## 🛠️ Configuration

All options are managed in [`src/main/resources/config.yml`](src/main/resources/config.yml):

- **Bot Settings:**  
  `bot.token`, `bot.username`, `bot.chat`
- **Notification Toggles:**  
  Enable/disable notifications for death, join, quit, chat, achievements, ragequit, shutdown, and more.
- **Message Customization:**  
  Edit messages for each event, including placeholders like `%msg%`, `%success%`, `%location%`.
- **Delays:**  
  Control spam with configurable delays for join, quit, and ragequit notifications.

## 💬 Commands

- `/notificator reload`  
  Reloads the plugin configuration.
- `/send <message>`  
  Sends a custom message to your Telegram chat.

## 🎉 Why Choose Telegram Notificator?

- **Keep your community engaged** even when they’re not online.
- **Celebrate victories, mock ragequits, and share server moments** instantly.
- **Easy to set up, fun to use, and highly customizable.**

---

Ready to connect your Minecraft world to Telegram?  
Try **Telegram Notificator** and make your server unforgettable!
