package com.karasu256.locatorbarplus.mixin.client;

import com.google.common.collect.ImmutableMap;
import com.karasu256.locatorbarplus.client.renderer.LocatorBarRenderer;
import com.karasu256.locatorbarplus.client.OverlayManagerState;
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

@SuppressWarnings({"DataFlowIssue", "RedundantCast"})
@Mixin(InGameHud.class)
public class InGameHudMixin implements IInGameHud {
    @Unique
    public LocatorBar locatorBar;
    @Unique
    public ExperienceBar experienceBar;
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
        if (config.general.alwaysHideLocatorBar) {
            cir.setReturnValue(true);
            return;
        }

        if (OverlayManagerState.getInstance().shouldShowOverlay() && LocatorBarRenderer.hasAnyMarkers(this.client)) {
            cir.setReturnValue(true);
        }
    }

    @Unique
    @Override
    public LocatorBar locatorBarPlus$getLocatorBar() {
        return locatorBar;
    }

    @Unique
    @Override
    public ExperienceBar locatorBarPlus$getExperienceBar() {
        return experienceBar;
    }

    @Unique
    @Override
    public Bar locatorBarPlus$getCurrentBar() {
        return this.currentBar.getValue();
    }

    @Redirect(method = "renderMainHud", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/bar/Bar;renderBar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"))
    private void renderBar(Bar instance, DrawContext context, RenderTickCounter tickCounter) {
        LocatorBarRenderer.renderHud(this, context, tickCounter, this.config, this.client);
    }
}
