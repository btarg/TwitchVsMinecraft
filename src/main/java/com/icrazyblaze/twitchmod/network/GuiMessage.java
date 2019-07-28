package com.icrazyblaze.twitchmod.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class GuiMessage implements IMessage {
    // A default constructor is always required
    public GuiMessage(){}

    public String toSend;
    public GuiMessage(String toSend) {
        this.toSend = toSend;
    }

    @Override public void toBytes(ByteBuf buf) {
        // Writes the int into the buf
        ByteBufUtils.writeUTF8String(buf, toSend);
    }

    @Override public void fromBytes(ByteBuf buf) {
        // Reads the int back from the buf. Note that if you have multiple values, you must read in the same order you wrote.
        toSend = ByteBufUtils.readUTF8String(buf);
    }
}
