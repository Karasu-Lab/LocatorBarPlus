package com.karasu256.locatorbarplus.impl;

import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.client.gui.hud.bar.LocatorBar;

public interface IInGameHud {
    LocatorBar locatorBarPlus$getLocatorBar();

    ExperienceBar locatorBarPlus$getExperienceBar();

    Bar locatorBarPlus$getCurrentBar();
}
