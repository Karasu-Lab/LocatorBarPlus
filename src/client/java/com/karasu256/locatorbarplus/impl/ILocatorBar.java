package com.karasu256.locatorbarplus.impl;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public interface ILocatorBar {
    void setInGameHud(InGameHud inGameHud);

    void setExperienceBar(ExperienceBar experienceBar);

    void renderAddons(DrawContext context, RenderTickCounter tickCounter, float transparency);

    Identifier locatorBarPlus$getBackground();

    Identifier locatorBarPlus$getArrowUp();

    Identifier locatorBarPlus$getArrowDown();
}
