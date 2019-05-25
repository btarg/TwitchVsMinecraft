package com.icrazyblaze.twitchmod.irc;


public class BotConfig {

    public static boolean isConnected = false;
    public static String TWITCH_KEY = null;
    public static String CHANNEL_NAME = null;

    public static boolean showChatMessages = false;

    static TwitchBot bot = new TwitchBot();

    public static void main() throws Exception {

        bot.setVerbose(true);

        bot.connect("irc.twitch.tv", 6667, TWITCH_KEY);
        bot.joinChannel("#" + CHANNEL_NAME);

    }

    public static void disconnectBot() {
        bot.disconnect();
    }

}
