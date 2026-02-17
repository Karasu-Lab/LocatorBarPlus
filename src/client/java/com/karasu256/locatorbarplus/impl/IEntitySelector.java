package com.karasu256.locatorbarplus.impl;

import net.minecraft.entity.Entity;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IEntitySelector {
    List<Entity> locatorBarPlus$getEntities(Vec3d pos, List<? extends Entity> entities);
    boolean locatorBarPlus$includesNonPlayers();
    String locatorBarPlus$getPlayerName();
    UUID locatorBarPlus$getUuid();
    Function<Vec3d, Vec3d> locatorBarPlus$getPositionOffset();
    Box locatorBarPlus$getBox();
    NumberRange.DoubleRange locatorBarPlus$getDistance();
    List<Predicate<Entity>> locatorBarPlus$getPredicates();
    TypeFilter<Entity, ?> locatorBarPlus$getEntityFilter();
    int locatorBarPlus$getLimit();
    BiConsumer<Vec3d, List<? extends Entity>> locatorBarPlus$getSorter();
}
