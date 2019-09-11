package com.icrazyblaze.twitchmod.util;

import com.icrazyblaze.twitchmod.BotCommands;
import com.icrazyblaze.twitchmod.Main;
import com.icrazyblaze.twitchmod.chat.ChatPicker;
import com.icrazyblaze.twitchmod.irc.BotConfig;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigManager {

    private static Configuration config;

    public static void loadConfig() { // Gets called from preInit

        // Config file is made in the Main class
        config = Main.config;

        try {

            // Load config
            config.load();

            // Read props from config
            Property keyProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "TWITCH_KEY", // Property name
                    "", // Default value
                    "Oauth key from twitchapps.com"); // Comment

            Property channelProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "CHANNEL_NAME", // Property name
                    "", // Default value
                    "Name of Twitch channel"); // Comment

            Property showMessagesProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "SHOW_CHAT", // Property name
                    true, // Default value
                    "Should chat messages be shown"); // Comment

            Property showCommandsProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "SHOW_CHAT_COMMANDS", // Property name
                    false, // Default value
                    "Should chosen commands be shown if chat messages are enabled"); // Comment

            Property chatSecondsProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "CHAT_TIMER", // Property name
                    30, // Default value
                    "How many seconds until the next command is chosen"); // Comment

            Property messageSecondsProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "MESSAGE_TIMER", // Property name
                    300, // Default value
                    "How many seconds until a random viewer-written message is shown on screen"); // Comment

            Property usernameProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "USER_AFFECTED", // Property name
                    "", // Default value
                    "The streamer's Minecraft username"); // Comment

            Property prefixProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "COMMAND_PREFIX", // Property name
                    "!", // Default value
                    "The prefix for commands"); // Comment

            Property cooldownProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "COOLDOWN_ENABLED", // Property name
                    false, // Default value
                    "Prevent the same command from being executed twice in a row."); // Comment


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

        // Config file is made in the Main class
        config = Main.config;

        try {

            // Load config
            config.load();

            // Read props from config
            Property keyProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "TWITCH_KEY", // Property name
                    "", // Default value
                    "Oauth key from twitchapps.com"); // Comment

            Property channelProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "CHANNEL_NAME", // Property name
                    "", // Default value
                    "Name of Twitch channel"); // Comment

            Property showMessagesProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "SHOW_CHAT", // Property name
                    true, // Default value
                    "Should chat messages be shown"); // Comment

            Property showCommandsProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "SHOW_CHAT_COMMANDS", // Property name
                    false, // Default value
                    "Should chosen commands be shown if chat messages are enabled"); // Comment

            Property chatSecondsProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "CHAT_TIMER", // Property name
                    20, // Default value
                    "How many seconds until the next command is chosen"); // Comment

            Property messageSecondsProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "MESSAGE_TIMER", // Property name
                    300, // Default value
                    "How many seconds until a random viewer-written message is shown on screen"); // Comment

            Property usernameProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "USER_AFFECTED", // Property name
                    "", // Default value
                    "The streamer's Minecraft username"); // Comment

            Property prefixProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "COMMAND_PREFIX", // Property name
                    "!", // Default value
                    "The prefix for commands"); // Comment

            Property cooldownProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
                    "COOLDOWN_ENABLED", // Property name
                    false, // Default value
                    "Prevent the same command from being executed twice in a row."); // Comment

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
            config.save();
        }
    }

}
