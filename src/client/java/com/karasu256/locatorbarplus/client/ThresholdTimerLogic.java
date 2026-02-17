package com.karasu256.locatorbarplus.client;

import com.karasu256.locatorbarplus.config.ModConfig;
import com.karasu256.locatorbarplus.impl.IActivationLogic;

public class ThresholdTimerLogic implements IActivationLogic {
    private long startTime = 0;

    @Override
    public boolean updateAndCheck(boolean conditionActive, ModConfig config) {
        if (conditionActive) {
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            long elapsed = System.currentTimeMillis() - startTime;
            long threshold = (long)(config.general.sneakThresholdSeconds * 1000);
            return elapsed >= threshold;
        } else {
            startTime = 0;
            return false;
        }
    }

    @Override
    public void reset() {
        startTime = 0;
    }
}
