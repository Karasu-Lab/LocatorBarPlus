package com.karasu256.locatorbarplus.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import static com.karasu256.locatorbarplus.client.LocatorBarPlusClient.MOD_ID;

@Config(name = MOD_ID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public General general = new General();

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public LocatorBar locatorBar = new LocatorBar();

    public static class General{
        public boolean modEnabled = true;
        public boolean alwaysHideLocatorBar = false;
        public boolean neverHideLocatorBar = false;

    }

    public static class LocatorBar {
        @ConfigEntry.Gui.TransitiveObject
        public float experienceBarTransparency = 0.5f;
    }
}
