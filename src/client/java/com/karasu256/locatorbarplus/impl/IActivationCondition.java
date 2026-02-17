package com.karasu256.locatorbarplus.impl;

import net.minecraft.entity.player.PlayerEntity;

public interface IActivationCondition {
    boolean isActive(PlayerEntity player);
}
