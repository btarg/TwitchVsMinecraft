package com.icrazyblaze.twitchmod.gui;

import com.icrazyblaze.twitchmod.util.Reference;
import com.icrazyblaze.twitchmod.util.TickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

// Thanks Silk!
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Reference.MOD_ID)
public class TimerGui extends Gui {

    @SubscribeEvent
    public static void onRenderGui(RenderGameOverlayEvent.Post event) {

        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }

        if (TickHandler.killTimer) {

            Minecraft mc = Minecraft.getMinecraft();
            String text = "TIMER: " + TickHandler.timerSeconds;

            mc.fontRenderer.drawStringWithShadow(text, 4, 4, Integer.parseInt("AA0000", 16));

        }

    }

}
