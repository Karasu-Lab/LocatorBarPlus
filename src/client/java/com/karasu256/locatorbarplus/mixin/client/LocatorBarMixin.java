package com.karasu256.locatorbarplus.mixin.client;

import com.karasu256.locatorbarplus.client.renderer.LocatorBarRenderer;
import com.karasu256.locatorbarplus.impl.ILocatorBar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(LocatorBar.class)
public abstract class LocatorBarMixin implements ILocatorBar, Bar {
    @Shadow
    @Final
    private static Identifier BACKGROUND;
    @Shadow
    @Final
    private static Identifier ARROW_UP;
    @Shadow
    @Final
    private static Identifier ARROW_DOWN;
    @Shadow
    @Final
    private MinecraftClient client;
    @Unique
    private ExperienceBar experienceBar;

    @Override
    public void renderAddons(DrawContext context, RenderTickCounter tickCounter, float transparency) {
        LocatorBarRenderer.renderLocatorAddons(this, context, tickCounter, transparency, this.client, this.experienceBar);
    }

    @Override
    public Identifier locatorBarPlus$getBackground() {
        return BACKGROUND;
    }

    @Override
    public Identifier locatorBarPlus$getArrowUp() {
        return ARROW_UP;
    }

    @Override
    public Identifier locatorBarPlus$getArrowDown() {
        return ARROW_DOWN;
    }

    @Override
    public void setInGameHud(InGameHud inGameHud) {
    }

    @Override
    public void setExperienceBar(ExperienceBar experienceBar) {
        this.experienceBar = experienceBar;
    }
}


