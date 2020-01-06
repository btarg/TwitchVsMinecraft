package com.icrazyblaze.twitchmod.util;

import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.Main;
import com.icrazyblaze.twitchmod.chat.ChatPicker;
import com.icrazyblaze.twitchmod.irc.BotConfig;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigManager {

    private static Configuration config;
    private static ConfigManager instance;

    private static Property keyProp;
    private static Property channelProp;
    private static Property showMessagesProp;
    private static Property showCommandsProp;
    private static Property chatSecondsProp;
    private static Property messageSecondsProp;
    private static Property usernameProp;
    private static Property prefixProp;
    private static Property cooldownProp;

    public static void initialize() {
        instance = new ConfigManager();

        // Config file is made in the Main class
        config = Main.config;

        // Read props from config
        keyProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                "TWITCH_KEY", // Property name
                "", // Default value
                "Oauth key from twitchapps.com"); // Comment

        channelProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                "CHANNEL_NAME", // Property name
                "", // Default value
                "Name of Twitch channel"); // Comment

        showMessagesProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                "SHOW_CHAT", // Property name
                true, // Default value
                "Should chat messages be shown"); // Comment

        showCommandsProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                "SHOW_CHAT_COMMANDS", // Property name
                false, // Default value
                "Should chosen commands be shown if chat messages are enabled"); // Comment

        chatSecondsProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                "CHAT_TIMER", // Property name
                20, // Default value
                "How many seconds until the next command is chosen"); // Comment

        messageSecondsProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                "MESSAGE_TIMER", // Property name
                300, // Default value
                "How many seconds until a random viewer-written message is shown on screen"); // Comment

        usernameProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                "USER_AFFECTED", // Property name
                "", // Default value
                "The streamer's Minecraft username"); // Comment

        prefixProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                "COMMAND_PREFIX", // Property name
                "!", // Default value
                "The prefix for commands"); // Comment

        cooldownProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                "COOLDOWN_ENABLED", // Property name
                false, // Default value
                "Prevent the same command from being executed twice in a row."); // Comment

        loadConfig();

    }

    public static ConfigManager getInstance() {
        return instance;
    }

    public static void loadConfig() { // Gets called from preInit

        try {

            // Load config
            config.load();

            // Get the values from file
            BotConfig.CHANNEL_NAME = channelProp.getString();
            BotConfig.TWITCH_KEY = keyProp.getString();
            BotConfig.showChatMessages = showMessagesProp.getBoolean();
            BotConfig.showCommands = showCommandsProp.getBoolean();
            TickHandler.chatSeconds = chatSecondsProp.getInt();
            TickHandler.messageSeconds = messageSecondsProp.getInt();
            TickHandler.chatSecondsDefault = chatSecondsProp.getInt();
            BotCommands.username = usernameProp.getString();
            BotConfig.prefix = prefixProp.getString();
            ChatPicker.cooldownEnabled = cooldownProp.getBoolean();


        } catch (Exception e) {
            // Failed reading/writing, just continue
        }
    }

    public static void saveConfig() {

        try {

            // Load config
            config.load();

            // Set the values in file
            keyProp.set(BotConfig.TWITCH_KEY);
            channelProp.set(BotConfig.CHANNEL_NAME);
            showMessagesProp.set(BotConfig.showChatMessages);
            showCommandsProp.set(BotConfig.showCommands);
            chatSecondsProp.set(TickHandler.chatSecondsDefault);
            messageSecondsProp.set(TickHandler.messageSecondsDefault);
            usernameProp.set(BotCommands.username);
            prefixProp.set(BotConfig.prefix);
            cooldownProp.set(ChatPicker.cooldownEnabled);

        } catch (Exception e) {
            // Failed reading/writing, just continue
        } finally {
            // Save properties to config file
            saveConfigFile();
        }
    }

    public static void saveConfigFile() {
        config.save();
    }

    public Configuration getConfig() {
        return config;
    }

}
