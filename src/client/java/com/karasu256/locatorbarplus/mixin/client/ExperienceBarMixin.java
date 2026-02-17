package com.karasu256.locatorbarplus.mixin.client;

import com.karasu256.locatorbarplus.client.LocatorBarRenderer;
import com.karasu256.locatorbarplus.impl.IExperienceBar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings({"AddedMixinMembersNamePattern"})
@Mixin(ExperienceBar.class)
public abstract class ExperienceBarMixin implements Bar, IExperienceBar {
    @Shadow
    @Final
    private MinecraftClient client;

    @Override
    public void renderBar(DrawContext context, RenderTickCounter tickCounter, float transparency) {
        LocatorBarRenderer.renderExperienceBar(this, context, tickCounter, transparency, this.client);
    }

    @Override
    public Identifier locatorBarPlus$getBackground() {
        return ((ExperienceBarAccessor) this).getBackground();
    }

    @Override
    public Identifier locatorBarPlus$getProgress() {
        return ((ExperienceBarAccessor) this).getProgress();
    }
}
