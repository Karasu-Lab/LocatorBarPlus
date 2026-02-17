package com.karasu256.locatorbarplus.mixin.client;

import com.karasu256.karasunikilib.impl.EntitySelectorExtensions;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(EntitySelector.class)
public abstract class EntitySelectorMixin implements EntitySelectorExtensions {

    @Shadow
    @Final
    private int limit;
    @Shadow
    @Final
    private boolean includesNonPlayers;
    @Shadow
    @Final
    private List<Predicate<Entity>> predicates;
    @Shadow
    @Final
    private NumberRange.DoubleRange distance;
    @Shadow
    @Final
    private Function<Vec3d, Vec3d> positionOffset;
    @Shadow
    @Final
    @Nullable
    private Box box;
    @Shadow
    @Final
    private BiConsumer<Vec3d, List<? extends Entity>> sorter;
    @Shadow
    @Final
    @Nullable
    private String playerName;
    @Shadow
    @Final
    @Nullable
    private UUID uuid;
    @Shadow
    @Final
    private TypeFilter<Entity, ?> entityFilter;

    @Override
    public boolean locatorBarPlus$includesNonPlayers() {
        return includesNonPlayers;
    }

    @Override
    public String locatorBarPlus$getPlayerName() {
        return playerName;
    }

    @Override
    public UUID locatorBarPlus$getUuid() {
        return uuid;
    }

    @Override
    public Function<Vec3d, Vec3d> locatorBarPlus$getPositionOffset() {
        return positionOffset;
    }

    @Override
    public Box locatorBarPlus$getBox() {
        return box;
    }

    @Override
    public NumberRange.DoubleRange locatorBarPlus$getDistance() {
        return distance;
    }

    @Override
    public List<Predicate<Entity>> locatorBarPlus$getPredicates() {
        return predicates;
    }

    @Override
    public TypeFilter<Entity, ?> locatorBarPlus$getEntityFilter() {
        return entityFilter;
    }

    @Override
    public int locatorBarPlus$getLimit() {
        return limit;
    }

    @Override
    public BiConsumer<Vec3d, List<? extends Entity>> locatorBarPlus$getSorter() {
        return sorter;
    }
}
