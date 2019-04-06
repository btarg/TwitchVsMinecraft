package com.icrazyblaze.twitchmod.util;

import com.icrazyblaze.twitchmod.Main;
import com.icrazyblaze.twitchmod.pircbot.Config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigSaveLoad {
	
	public static Configuration config;
	
	public static void loadConfig() { // Gets called from preInit	
		
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
	        Config.CHANNEL_NAME = channelProp.getString();
	        Config.TWITCH_KEY = keyProp.getString();
	        Config.showChatMessages = showMessagesProp.getBoolean();
	        TickHandler.chatSeconds = chatSecondsProp.getInt();
	        TickHandler.chatSecondsDefault = chatSecondsProp.getInt();
	        
	        
	    } catch (Exception e) {
	        // Failed reading/writing, just continue
	    }
	}
	
	public static void saveConfig() { // Gets called from preInit		
			
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
		    keyProp.set(Config.TWITCH_KEY);
		    channelProp.set(Config.CHANNEL_NAME);
		    showMessagesProp.set(Config.showChatMessages);
		    chatSecondsProp.set(TickHandler.chatSecondsDefault);
	        
	    } catch (Exception e) {
	        // Failed reading/writing, just continue
	    } finally {
	        // Save properties to config file
	    	config.save();
	    }
	}
	

}
