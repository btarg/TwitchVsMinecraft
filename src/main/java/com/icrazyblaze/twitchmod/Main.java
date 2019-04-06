package com.icrazyblaze.twitchmod;

import org.apache.logging.log4j.Logger;

import com.icrazyblaze.twitchmod.commands.TTVCommand;
import com.icrazyblaze.twitchmod.gui.TimerGui;
import com.icrazyblaze.twitchmod.pircbot.TwitchBot;
import com.icrazyblaze.twitchmod.util.ConfigSaveLoad;
import com.icrazyblaze.twitchmod.util.Reference;
import com.icrazyblaze.twitchmod.util.TickHandler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;



@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main {

    public static Logger logger;
    public static Configuration config;


    @Instance
    public static Main instance;


    @EventHandler
    public static void Preinit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());
        ConfigSaveLoad.loadConfig();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new TwitchBot());
        MinecraftForge.EVENT_BUS.register(new TickHandler());
        MinecraftForge.EVENT_BUS.register(new TimerGui());
        MinecraftForge.EVENT_BUS.register(new BotCommands());
    }

    @EventHandler
    public static void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new TTVCommand());
    }

}