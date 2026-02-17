package com.karasu256.locatorbarplus.impl;

import com.karasu256.locatorbarplus.config.ModConfig;

public interface IActivationLogic {
    boolean updateAndCheck(boolean conditionActive, ModConfig config);

    void reset();
}
