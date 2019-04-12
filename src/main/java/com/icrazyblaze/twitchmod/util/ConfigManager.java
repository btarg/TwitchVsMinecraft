package com.icrazyblaze.twitchmod.util;

import com.icrazyblaze.twitchmod.Main;
import com.icrazyblaze.twitchmod.pircbot.BotConfig;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigManager {
	
	public static Configuration config;
	
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
	        
	        Property chatSecondsProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
	                "CHAT_TIMER", // Property name
	                20, // Default value
	                "How many seconds until the next command is chosen"); // Comment

	        
	        // Get the values from file
	        BotConfig.CHANNEL_NAME = channelProp.getString();
	        BotConfig.TWITCH_KEY = keyProp.getString();
	        BotConfig.showChatMessages = showMessagesProp.getBoolean();
	        TickHandler.chatSeconds = chatSecondsProp.getInt();
	        TickHandler.chatSecondsDefault = chatSecondsProp.getInt();
	        
	        
	    } catch (Exception e) {
	        // Failed reading/writing, just continue
	    }
	}
	
	public static void saveConfig() { // Gets called from preInit		
		
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
	        
	        Property chatSecondsProp = config.get(Configuration.CATEGORY_GENERAL, // What category will it be saved to, can be any string
	                "CHAT_TIMER", // Property name
	                20, // Default value
	                "How many seconds until the next command is chosen"); // Comment
	    	
	        // Set the values in file
		    keyProp.set(BotConfig.TWITCH_KEY);
		    channelProp.set(BotConfig.CHANNEL_NAME);
		    showMessagesProp.set(BotConfig.showChatMessages);
		    chatSecondsProp.set(TickHandler.chatSecondsDefault);
	        
	    } catch (Exception e) {
	        // Failed reading/writing, just continue
	    } finally {
	        // Save properties to config file
	    	config.save();
	    }
	}
	

}
