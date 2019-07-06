package com.icrazyblaze.twitchmod.irc;

public class BotConnection {

    public static boolean isConnected = false;
	public static boolean isVerbose = false;
    static TwitchBot bot = new TwitchBot();

    public static void main() throws Exception {

        // Now no longer logs by default (this prevents Oauth keys being stored in minecraft logs)
        bot.setVerbose(false);

        bot.connect("irc.twitch.tv", 6667, BotConfig.TWITCH_KEY);
        bot.joinChannel("#" + BotConfig.CHANNEL_NAME);

    }

    public static void disconnectBot() {
        bot.disconnect();
    }

    public static void setVerboseMode(boolean set) {
        bot.setVerbose(set);
		isVerbose = set;
    }
}
