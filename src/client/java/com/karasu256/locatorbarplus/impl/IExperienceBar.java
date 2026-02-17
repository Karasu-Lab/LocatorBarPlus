package com.karasu256.locatorbarplus.impl;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public interface IExperienceBar {
    void renderBar(DrawContext context, RenderTickCounter tickCounter, float transparency);

    Identifier locatorBarPlus$getBackground();
    Identifier locatorBarPlus$getProgress();
}
