package com.icrazyblaze.twitchmod.network;

import com.icrazyblaze.twitchmod.gui.MessageboxGui;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// The params of the IMessageHandler are <REQ, REPLY>
// This means that the first param is the packet you are receiving, and the second is the packet you are returning.
// The returned packet can be used as a "response" from a sent packet.
public class GuiMessageHandler implements IMessageHandler<GuiMessage, IMessage> {
    // Do note that the default constructor is required, but implicitly defined in this case

    @Override public IMessage onMessage(GuiMessage message, MessageContext ctx) {
        // This is the player the packet was sent to the server from
        EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
        // The value that was sent
        String display = message.toSend;
        // Execute the action on the main server thread by adding it as a scheduled task
        serverPlayer.getServerWorld().addScheduledTask(() -> {
            Minecraft.getMinecraft().displayGuiScreen(new MessageboxGui(display));
        });
        // No response packet
        return null;
    }
}