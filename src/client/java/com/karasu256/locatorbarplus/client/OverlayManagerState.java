package com.karasu256.locatorbarplus.client;

import com.karasu256.locatorbarplus.client.condition.SneakCondition;
import com.karasu256.locatorbarplus.config.ModConfig;
import com.karasu256.locatorbarplus.impl.IActivationCondition;
import com.karasu256.locatorbarplus.impl.IActivationLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class OverlayManagerState {
    private static final OverlayManagerState INSTANCE = new OverlayManagerState();
    private final List<Entity> forcedEntities = new ArrayList<>();
    private IActivationCondition condition;
    private IActivationLogic logic;
    private boolean isModeActive = false;
    private ModConfig.ConditionMode lastMode = null;

    private OverlayManagerState() {
        this.condition = new SneakCondition();
        this.logic = new ThresholdTimerLogic();
    }

    public static OverlayManagerState getInstance() {
        return INSTANCE;
    }

    public void update(PlayerEntity player, ModConfig config) {
        if (player == null || config == null) return;

        if (lastMode != config.general.conditionMode) {
            updateStrategies(config.general.conditionMode);
            lastMode = config.general.conditionMode;
        }

        this.isModeActive = this.logic.updateAndCheck(this.condition.isActive(player), config);
    }

    private void updateStrategies(ModConfig.ConditionMode mode) {
        this.condition = mode.createCondition();
        this.logic = mode.createLogic();
    }

    public boolean shouldShowOverlay() {
        return !isModeActive;
    }

    public void setForcedByCommand(boolean forced) {
        if (!forced) {
            this.isModeActive = false;
            this.logic.reset();
            this.forcedEntities.clear();
        }
    }

    public List<Entity> getForcedEntities() {
        return forcedEntities;
    }

    public void setForcedEntities(List<Entity> entities) {
        this.forcedEntities.clear();
        this.forcedEntities.addAll(entities);
    }
}
