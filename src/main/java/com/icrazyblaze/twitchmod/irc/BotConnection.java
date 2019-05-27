package com.icrazyblaze.twitchmod.irc;

public class BotConnection {

    public static boolean isConnected = false;
    static TwitchBot bot = new TwitchBot();

    public static void main() throws Exception {

        bot.setVerbose(true);

        bot.connect("irc.twitch.tv", 6667, BotConfig.TWITCH_KEY);
        bot.joinChannel("#" + BotConfig.CHANNEL_NAME);

    }

    public static void disconnectBot() {
        bot.disconnect();
    }
}
