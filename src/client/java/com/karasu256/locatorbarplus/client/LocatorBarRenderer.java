package com.karasu256.locatorbarplus.client;

import com.karasu256.locatorbarplus.config.ModConfig;
import com.karasu256.locatorbarplus.impl.IExperienceBar;
import com.karasu256.locatorbarplus.impl.IInGameHud;
import com.karasu256.locatorbarplus.impl.ILocatorBar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.resource.waypoint.WaypointStyleAsset;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.waypoint.TrackedWaypoint;
import net.minecraft.world.waypoint.Waypoint;

import java.util.List;

@SuppressWarnings({"unused", "RedundantCast"})
public class LocatorBarRenderer {

    public static void renderHud(IInGameHud hud, DrawContext context, RenderTickCounter tickCounter, ModConfig config, MinecraftClient client) {
        OverlayManagerState.getInstance().update(client.player, config);
        
        boolean overlayActive = OverlayManagerState.getInstance().shouldShowOverlay();
        boolean hasMarkers = hasAnyMarkers(client);

        if (overlayActive && hasMarkers) {
            LocatorBar locatorBar = hud.locatorBarPlus$getLocatorBar();
            ((ILocatorBar) locatorBar).renderAddons(context, tickCounter, config.locatorBar.experienceBarTransparency);
        } else {
            Bar currentBar = hud.locatorBarPlus$getCurrentBar();
            if (currentBar instanceof LocatorBar) {
                if (config.general.alwaysHideLocatorBar) return;
                hud.locatorBarPlus$getExperienceBar().renderBar(context, tickCounter);
            } else {
                currentBar.renderBar(context, tickCounter);
            }
        }
    }

    public static boolean hasAnyMarkers(MinecraftClient client) {
        if (client.player == null) return false;

        for (Entity entity : OverlayManagerState.getInstance().getForcedEntities()) {
            if (entity == client.cameraEntity) continue;
            
            Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
            Vec3d entityPos = entity.getPos();
            
            double dx = entityPos.x - cameraPos.x;
            double dz = entityPos.z - cameraPos.z;
            
            double bearing = Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
            double relativeYaw = MathHelper.wrapDegrees(bearing - client.gameRenderer.getCamera().getYaw());
            
            if (relativeYaw >= -60.0 && relativeYaw <= 60.0) return true;
        }
        
        if (client.cameraEntity != null && client.player.networkHandler != null) {
            World world = client.cameraEntity.getWorld();
            boolean[] found = {false};
            client.player.networkHandler.getWaypointHandler().forEachWaypoint(client.cameraEntity, (waypoint) -> {
                if (!(Boolean) waypoint.getSource().left().map((uuid) -> uuid.equals(client.cameraEntity.getUuid())).orElse(false)) {
                    float distSq = (float) waypoint.squaredDistanceTo(client.cameraEntity);
                    if (distSq > 1.0F) {
                        double d = waypoint.getRelativeYaw(world, client.gameRenderer.getCamera());
                        if (d > -61.0 && d <= 60.0) {
                            found[0] = true;
                        }
                    }
                }
            });
            return found[0];
        }
        return false;
    }

    public static void renderLocatorAddons(ILocatorBar locatorBar, DrawContext context, RenderTickCounter tickCounter, float transparency, MinecraftClient client, ExperienceBar experienceBar) {
        int alpha = (int) (transparency * 255.0F);
        if (alpha <= 0) return;
        int color = ColorHelper.getArgb(alpha, 255, 255, 255);
        Bar bar = (Bar) locatorBar;

        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, locatorBar.locatorBarPlus$getBackground(), bar.getCenterX(client.getWindow()), bar.getCenterY(client.getWindow()), 182, 5, color);

        ((IExperienceBar) experienceBar).renderBar(context, tickCounter, transparency);

        int i = bar.getCenterY(client.getWindow());
        
        List<Entity> forcedEntities = OverlayManagerState.getInstance().getForcedEntities();
        for (Entity entity : forcedEntities) {
            if (entity == client.cameraEntity) continue;
            
            Vec3d cameraPos = client.gameRenderer.getCamera().getPos();
            Vec3d entityPos = entity.getPos();
            
            double dx = entityPos.x - cameraPos.x;
            double dz = entityPos.z - cameraPos.z;
            
            double bearing = Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
            double relativeYaw = MathHelper.wrapDegrees(bearing - client.gameRenderer.getCamera().getYaw());
            
            if (relativeYaw >= -60.0 && relativeYaw <= 60.0) {
                 int j = MathHelper.ceil((float) (context.getScaledWindowWidth() - 9) / 2.0F);
                 int l = (int) (relativeYaw * 173.0 / 2.0 / 60.0);
                 context.fill(j + l + 2, i, j + l + 6, i + 4, color);

                 double dy = entityPos.y - cameraPos.y;
                 double dist = Math.sqrt(dx * dx + dz * dz);
                 double angleToEntity = Math.toDegrees(Math.atan2(dy, dist));
                 double relativePitch = angleToEntity + client.gameRenderer.getCamera().getPitch();
                 
                 if (Math.abs(relativePitch) > 15.0) {
                     boolean isAbove = relativePitch > 0;
                     Identifier arrow = isAbove ? locatorBar.locatorBarPlus$getArrowUp() : locatorBar.locatorBarPlus$getArrowDown();
                     int m = isAbove ? -6 : 6;
                     context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, arrow, j + l + 1, i + m, 7, 5, color);
                 }
            }
        }

        if (client.cameraEntity != null) {
            World world = client.cameraEntity.getWorld();
            if (client.player != null && client.player.networkHandler != null) {
                client.player.networkHandler.getWaypointHandler().forEachWaypoint(client.cameraEntity, (waypoint) -> {
                    if (!(Boolean) waypoint.getSource().left().map((uuid) -> uuid.equals(client.cameraEntity.getUuid())).orElse(false)) {
                        double d = waypoint.getRelativeYaw(world, client.gameRenderer.getCamera());
                        if (!(d <= -61.0) && !(d > 60.0)) {
                            int j = MathHelper.ceil((float) (context.getScaledWindowWidth() - 9) / 2.0F);
                            Waypoint.Config config = waypoint.getConfig();
                            WaypointStyleAsset waypointStyleAsset = client.getWaypointStyleAssetManager().get(config.style);
                            float f = MathHelper.sqrt((float) waypoint.squaredDistanceTo(client.cameraEntity));
                            Identifier identifier = waypointStyleAsset.getSpriteForDistance(f);
                            
                            int k = config.color.orElseGet(() -> 
                                waypoint.getSource().map(
                                    (uuid) -> ColorHelper.withBrightness(ColorHelper.withAlpha(255, uuid.hashCode()), 0.9F), 
                                    (name) -> ColorHelper.withBrightness(ColorHelper.withAlpha(255, name.hashCode()), 0.9F)
                                )
                            );
                            
                            int originalAlpha = ColorHelper.getAlpha(k);
                            int newAlpha = (int) (originalAlpha * transparency);
                            int newColor = ColorHelper.withAlpha(newAlpha, k);

                            int l = (int) (d * 173.0 / 2.0 / 60.0);
                            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier, j + l, i - 2, 9, 9, newColor);
                            
                            TrackedWaypoint.Pitch pitch = waypoint.getPitch(world, client.gameRenderer);
                            if (pitch != TrackedWaypoint.Pitch.NONE) {
                                int m;
                                Identifier identifier2;
                                if (pitch == TrackedWaypoint.Pitch.DOWN) {
                                    m = 6;
                                    identifier2 = locatorBar.locatorBarPlus$getArrowDown();
                                } else {
                                    m = -6;
                                    identifier2 = locatorBar.locatorBarPlus$getArrowUp();
                                }
                                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier2, j + l + 1, i + m, 7, 5, color);
                            }
                        }
                    }
                });
            }
        }

        if (client.player != null) {
            Bar.drawExperienceLevel(context, client.textRenderer, client.player.experienceLevel);
        }
    }

    public static void renderExperienceBar(IExperienceBar experienceBar, DrawContext context, RenderTickCounter tickCounter, float transparency, MinecraftClient client) {
        ClientPlayerEntity clientPlayerEntity = client.player;
        if (clientPlayerEntity == null) return;
        Bar bar = (Bar) experienceBar;

        int i = bar.getCenterX(client.getWindow());
        int j = bar.getCenterY(client.getWindow());
        int k = clientPlayerEntity.getNextLevelExperience();
        if (k > 0) {
            int l = (int) (clientPlayerEntity.experienceProgress * 183.0F);
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, experienceBar.locatorBarPlus$getBackground(), i, j, 182, 5);
            if (l > 0) {
                int alpha = (int) (transparency * 255);
                int color = (alpha << 24) | 0xFFFFFF;
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, experienceBar.locatorBarPlus$getProgress(), 182, 5, 0, 0, i, j, l, 5, color);
            }
        }
    }
}
