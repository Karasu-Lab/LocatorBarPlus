package com.karasu256.locatorbarplus.client;

import com.karasu256.locatorbarplus.impl.IActivationCondition;
import net.minecraft.entity.player.PlayerEntity;

public class KeyHoldCondition implements IActivationCondition {
    @Override
    public boolean isActive(PlayerEntity player) {
        return LocatorBarPlusClient.TOGGLE_LOCATOR_BAR.isPressed();
    }
}
