package com.icrazyblaze.twitchmod.irc;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;


public class BotConnection {

    public static PircBotX bot = null;


    public static void main() throws Exception {

        try {

            Configuration config = new Configuration.Builder()
                    .setAutoReconnect(true)
                    .setAutoNickChange(false) // Twitch doesn't support multiple users
                    .setOnJoinWhoEnabled(false) // Twitch doesn't support WHO command
                    .setName("MinecraftBot")
                    .addServer("irc.twitch.tv", 6667)
                    .setServerPassword(BotConfig.TWITCH_KEY)
                    .addAutoJoinChannel("#" + BotConfig.CHANNEL_NAME)
                    .addListener(new TwitchBot())
                    .buildConfiguration();

            bot = new PircBotX(config);
            bot.startBot();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isConnected() {

        if (bot != null) {
            return bot.isConnected();
        } else {
            return false;
        }

    }

    public static void disconnectBot() {

        bot.stopBotReconnect();
        bot.close();

    }

}
