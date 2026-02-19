package com.karasu256.locatorbarplus.client;

import com.karasu256.locatorbarplus.impl.IActivationCondition;
import net.minecraft.entity.player.PlayerEntity;

public class KeyToggleCondition implements IActivationCondition {
    private boolean isActive = false;
    private boolean wasPressed = false;

    @Override
    public boolean isActive(PlayerEntity player) {
        boolean isPressed = LocatorBarPlusClient.TOGGLE_LOCATOR_BAR.isPressed();
        if (isPressed && !wasPressed) {
            isActive = !isActive;
        }
        wasPressed = isPressed;
        return isActive;
    }
}
