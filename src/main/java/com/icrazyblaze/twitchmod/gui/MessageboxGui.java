package com.icrazyblaze.twitchmod.gui;


import com.icrazyblaze.twitchmod.util.Reference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;


public class MessageboxGui extends GuiScreen {

    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/messagebox_background.png");
    public static String message = null;
    boolean displayGUI = true;

    public MessageboxGui(String message) {
        MessageboxGui.message = message;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    public void initGui() {
        GuiButton btn = new GuiButton(200, width / 2 - 75, height / 2 + 55, 150, 20, I18n.format("gui.done"));

        this.buttonList.add(btn);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawDefaultBackground();

        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(BG_TEXTURE);
        int x = (width / 2) - 88;
        int y = (height / 2) - 83;

        drawTexturedModalRect(x, y, 0, 0, 256, 256);
        fontRenderer.drawString("Message Box", width / 2 - 32, height / 2 - 78, 4210752);
        fontRenderer.drawSplitString(message, x + 7, height / 2 - 60, 165, 4210752);

        if (displayGUI) {
            super.drawScreen(mouseX, mouseY, partialTicks);
        } else {
            mc.player.closeScreen();
        }
    }

    @Override
    public void actionPerformed(GuiButton btn) {
        if (btn.id == buttonList.get(0).id) {
            displayGUI = false;
        }
    }
}
