package com.karasu256.locatorbarplus.client;

import com.karasu256.locatorbarplus.impl.IActivationCondition;
import net.minecraft.entity.player.PlayerEntity;

public class SneakCondition implements IActivationCondition {
    @Override
    public boolean isActive(PlayerEntity player) {
        return player != null && player.isSneaking();
    }
}
