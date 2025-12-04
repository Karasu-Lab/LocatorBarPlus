package com.karasu256.locatorbarplus.mixin.client;

import com.google.common.collect.ImmutableMap;
import com.karasu256.locatorbarplus.config.ModConfig;
import com.karasu256.locatorbarplus.impl.IInGameHud;
import com.karasu256.locatorbarplus.impl.ILocatorBar;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.client.gui.hud.bar.JumpBar;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.client.render.RenderTickCounter;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("DataFlowIssue")
@Mixin(InGameHud.class)
public class InGameHudMixin implements IInGameHud {
    @Shadow
    @Final
    private MinecraftClient client;
    @Mutable
    @Shadow
    @Final
    private Map<InGameHud.BarType, Supplier<Bar>> bars;
    @Shadow
    private Pair<InGameHud.BarType, Bar> currentBar;
    @Unique
    private ModConfig config;

    @Unique
    public LocatorBar locatorBar;

    @Unique
    public ExperienceBar experienceBar;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstructed(MinecraftClient client, CallbackInfo ci) {
        this.config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        this.locatorBar = new LocatorBar(client);
        this.experienceBar = new ExperienceBar(client);

        ((ILocatorBar) this.locatorBar).setInGameHud((InGameHud) (Object) this);
        ((ILocatorBar) this.locatorBar).setExperienceBar(this.experienceBar);

        this.bars = ImmutableMap.of(
                InGameHud.BarType.EMPTY, () -> Bar.EMPTY,
                InGameHud.BarType.EXPERIENCE, () -> this.experienceBar,
                InGameHud.BarType.LOCATOR, () -> this.locatorBar,
                InGameHud.BarType.JUMPABLE_VEHICLE, () -> new JumpBar(client));
    }

    @Inject(method = "shouldShowExperienceBar", at = @At(value = "HEAD"), cancellable = true)
    private void shouldShowExperienceBar(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!config.general.neverHideLocatorBar && (config.general.alwaysHideLocatorBar
                || this.client.player.experienceBarDisplayStartTime + 100 > this.client.player.age));
    }

    @Unique
    @Override
    public LocatorBar getLocatorBar() {
        return locatorBar;
    }

    @Unique
    @Override
    public ExperienceBar getExperienceBar() {
        return experienceBar;
    }

    @Redirect(method = "renderMainHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/bar/Bar;renderBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"))
    private void renderBar(Bar instance, DrawContext context, RenderTickCounter tickCounter) {
        var bar = this.currentBar.getValue();

        if (config.general.modEnabled) {
            if (config.general.alwaysHideLocatorBar) {
                if (bar instanceof LocatorBar locatorBar) {
                    ((ILocatorBar) locatorBar).renderAddons(context, tickCounter, 0);
                    return;
                }
            }
            bar.renderBar(context, tickCounter);
        } else {
            bar.renderBar(context, tickCounter);
        }
    }
}
