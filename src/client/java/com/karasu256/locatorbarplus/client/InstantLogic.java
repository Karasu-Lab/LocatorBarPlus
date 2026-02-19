package com.karasu256.locatorbarplus.client;

import com.karasu256.locatorbarplus.config.ModConfig;
import com.karasu256.locatorbarplus.impl.IActivationLogic;

public class InstantLogic implements IActivationLogic {
    @Override
    public boolean updateAndCheck(boolean conditionActive, ModConfig config) {
        return conditionActive;
    }

    @Override
    public void reset() {
        // No state to reset
    }
}
