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

## ⚙️ Configuration details

```yaml
bot:
  token: dummytoken           # Your Telegram bot token (get it from BotFather)
  username: botusername       # Telegram bot username
  chat: dummychat             # Telegram chat ID where notifications are sent

communication:
  enabled: true               # Enable/disable all Telegram communication

notif:
  death:
    enabled: true             # Send notifications on player death
    timer:
      enabled: true           # Include survival time in death messages

  silent:
    enabled: true             # If true, notifications are sent silently (no Telegram group ping)

  message:
    death: ☠️ `%msg%`          # Message template for player death (%msg% = player name)
    join: 💎⛏️ `%msg% just joined the game.`   # Message for player join
    quit: 🌒 `%msg% has left the game.`        # Message for player quit
    success: 🏆 `%msg% just earned the achievement %success%! GG!`   # Achievement message (%success% = achievement name)
    chat: 💬 `%msg%:` %message%                # Chat message template (%message% = chat content)
    shutdown: ♻️🔋 `Server is shutting down.`   # Message when server shuts down
    rage:                                      # List of random ragequit messages
      - 🖥️👉🪟 `I think %msg% just threw their PC out the window, I lost the connection.`
      - 🍼👶 `%msg% ragequit again... someone hand them a pacifier?`
      - 🧂😤 `%msg%’s tears are saltier than the Atlantic Ocean.`
      - 🪦💀 `%msg% died… again. But this time they ragequit before respawning.`
      - 🎻😢 `%msg% deserves a tiny violin for that dramatic exit.`
      - 🚪🏃 `%msg% tried to run away! Turns out even that’s hard...`
      - 🧹🗑️ `%msg% ragequit... Let’s hope they don’t slip on their tears and die a second time.`
      - 🛑🤡 `%msg% thought they were a pro, but disconnecting was faster than their skills.`
      - 🐔🏳️ `%msg% quit like a chicken in front of a fox.`
      - 🥲💔 `%msg% ragequit at %location%... if anyone can go help them, I almost feel bad.` # %location% = player location
      - 🏆🥈😂 `%msg% didn’t lose… they just ragequit as the “grand champion” of second place.`
      - 📉📉😂 `%msg% keeps tanking the team’s motivation chart.`
      - 🧟🧠 `%msg% used 0% of their brain before ragequitting.`
      - 🎯🖱️❄️ `%msg% finally managed to hit their fridge with the mouse.`
      - 🦥💤 `%msg% ragequit faster than they play. Impressive.`
      - 🤹‍♂️🙄 `%msg% is juggling between dying and ragequitting.`
      - 🔮🤔 `%msg% didn’t ragequit. They’re just “predicting” a loss for later.`
      - 🍿😂🎬 `%msg% always puts on a good ragequit show, thanks for the entertainment.`
      - 🥲🚪 `%msg% confused the 'Quit' key with 'Win'. (Spoiler: there’s no such key lol).`
      - 🎮🕹️👾 `%msg% thinks quitting the game is an eSport skill.`
      - 🚫🏅 `%msg% wins the gold medal for the most pathetic ragequit.`
      - 🍺🍹🍷 `%msg%, maybe grab a drink to forget what just happened? I won’t tell the others.`
    deathTime: ⏱️ `survived for %msg%`         # Message for survival time after death

  join:
    delay: 15                  # Minimum seconds between join notifications (anti-spam)
    enabled: true              # Enable join notifications

  quit:
    delay: 15                  # Minimum seconds between quit notifications (anti-spam)
    enabled: true              # Enable quit notifications

  success:
    enabled: true              # Enable achievement notifications
    categories:                # Achievement categories to notify
    - adventure
    - husbandry
    - story
    - nether
    - end

  free:
    enabled: true              # Enable free-form message sending via command

  rage:
    enabled: true              # Enable ragequit notifications
    delay: 30                  # Maximum delay (in seconds) after death to trigger a ragequit message

  shutdown:
    enabled: true              # Enable server shutdown notifications
```

Ready to connect your Minecraft world to Telegram?  
Try **Telegram Notificator** and make your server unforgettable!
