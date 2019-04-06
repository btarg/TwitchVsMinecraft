package com.icrazyblaze.twitchmod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class PrintToChat {

	public static void print(TextFormatting formatting, String message) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(formatting + message));
	}
	
}
