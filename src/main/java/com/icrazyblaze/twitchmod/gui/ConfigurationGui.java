/**
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.icrazyblaze.twitchmod.gui;

import com.icrazyblaze.twitchmod.util.ConfigManager;
import com.icrazyblaze.twitchmod.util.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

/**
 * Gui configuration for Twitch vs Minecraft.
 */
public class ConfigurationGui extends GuiConfig {
    public ConfigurationGui(GuiScreen parentScreen) {
        super(
                parentScreen,
                new ConfigElement(
                        ConfigManager.getInstance()
                                .getConfig()
                                .getCategory(Configuration.CATEGORY_GENERAL))
                        .getChildElements(),
                Reference.MOD_ID,
                false,
                false,
                "Twitch Vs Minecraft settings");

    }

    @Override
    public void onGuiClosed() {
        ConfigManager.getInstance().saveConfig();
    }
}