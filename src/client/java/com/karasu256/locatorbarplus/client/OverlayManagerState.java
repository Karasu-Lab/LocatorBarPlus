package com.karasu256.locatorbarplus.client;

import net.minecraft.entity.Entity;
import java.util.ArrayList;
import java.util.List;

import com.karasu256.locatorbarplus.config.ModConfig;

import net.minecraft.entity.player.PlayerEntity;

public class OverlayManagerState {
    private static final OverlayManagerState INSTANCE = new OverlayManagerState();

    private long sneakStartTime = 0;
    private boolean isModeActive = false;
    private boolean forcedByCommand = false;
    private List<Entity> forcedEntities = new ArrayList<>();

    private OverlayManagerState() {}

    public static OverlayManagerState getInstance() {
        return INSTANCE;
    }

    public void update(PlayerEntity player, ModConfig config) {
        if (player == null || config == null) return;

        if (player.isSneaking()) {
            if (sneakStartTime == 0) {
                sneakStartTime = System.currentTimeMillis();
            }
            long elapsed = System.currentTimeMillis() - sneakStartTime;
            long threshold = (long)(config.general.sneakThresholdSeconds * 1000);

            if (elapsed >= threshold) {
                isModeActive = true;
            }
        } else {
            sneakStartTime = 0;
            isModeActive = false;
        }
    }

    public boolean shouldShowOverlay() {
        return !isModeActive;
    }

    public void setForcedByCommand(boolean forced) {
        this.forcedByCommand = forced;
        if (!forced) {
            this.isModeActive = false; 
            this.sneakStartTime = 0;
            this.forcedEntities.clear();
        }
    }
    
    public boolean isForcedByCommand() {
        return forcedByCommand;
    }

    public void setForcedEntities(List<Entity> entities) {
        this.forcedEntities = new ArrayList<>(entities);
    }

    public List<Entity> getForcedEntities() {
        return forcedEntities;
    }
}
