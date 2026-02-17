package com.karasu256.locatorbarplus.impl;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Util;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface EntitySelectorExtensions extends IEntitySelector {

    default List<Entity> locatorBarPlus$getEntities(Vec3d pos, List<? extends Entity> entities) {
        if (!this.locatorBarPlus$includesNonPlayers()) {
            return this.locatorBarPlus$getPlayers(pos, entities);
        } else if (this.locatorBarPlus$getPlayerName() != null) {
            List<Entity> result = new ArrayList<>();
            for (Entity entity : entities) {
                if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getGameProfile().getName().equals(this.locatorBarPlus$getPlayerName())) {
                    result.add(entity);
                }
            }
            return result;
        } else if (this.locatorBarPlus$getUuid() != null) {
            List<Entity> result = new ArrayList<>();
            for (Entity entity : entities) {
                if (entity.getUuid().equals(this.locatorBarPlus$getUuid())) {
                    result.add(entity);
                }
            }
            return result;
        } else {
            Vec3d vec3d = this.locatorBarPlus$getPositionOffset().apply(pos);
            Box box = this.locatorBarPlus$getOffsetBox(this.locatorBarPlus$getBox(), vec3d);
            Predicate<Entity> predicate = this.locatorBarPlus$getPositionPredicate(vec3d, box, null);

            List<Entity> list = new ArrayList<>();
            for (Entity entity : entities) {
                if (this.locatorBarPlus$getEntityFilter().downcast(entity) != null && predicate.test(entity)) {
                    list.add(entity);
                }
            }
            return this.locatorBarPlus$sortAndLimit(vec3d, list);
        }
    }

    private List<Entity> locatorBarPlus$getPlayers(Vec3d pos, List<? extends Entity> entities) {
        if (this.locatorBarPlus$getPlayerName() != null) {
            List<Entity> result = new ArrayList<>();
            for (Entity entity : entities) {
                if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getGameProfile().getName().equals(this.locatorBarPlus$getPlayerName())) {
                    result.add(entity);
                }
            }
            return result;
        } else if (this.locatorBarPlus$getUuid() != null) {
            List<Entity> result = new ArrayList<>();
            for (Entity entity : entities) {
                if (entity instanceof PlayerEntity && entity.getUuid().equals(this.locatorBarPlus$getUuid())) {
                    result.add(entity);
                }
            }
            return result;
        } else {
            Vec3d vec3d = this.locatorBarPlus$getPositionOffset().apply(pos);
            Box box = this.locatorBarPlus$getOffsetBox(this.locatorBarPlus$getBox(), vec3d);
            Predicate<Entity> predicate = this.locatorBarPlus$getPositionPredicate(vec3d, box, null);

            List<Entity> list = new ArrayList<>();
            for (Entity entity : entities) {
                if (entity instanceof PlayerEntity && predicate.test(entity)) {
                    if (this.locatorBarPlus$getEntityFilter().downcast(entity) != null) {
                        list.add(entity);
                    }
                }
            }
            return this.locatorBarPlus$sortAndLimit(vec3d, list);
        }
    }

    private <T extends Entity> List<T> locatorBarPlus$sortAndLimit(Vec3d pos, List<T> entities) {
        if (entities.size() > 1) {
            this.locatorBarPlus$getSorter().accept(pos, entities);
        }
        return entities.subList(0, Math.min(this.locatorBarPlus$getLimit(), entities.size()));
    }

    private Box locatorBarPlus$getOffsetBox(@Nullable Box box, Vec3d offset) {
        return box != null ? box.offset(offset) : null;
    }

    private Predicate<Entity> locatorBarPlus$getPositionPredicate(Vec3d pos, @Nullable Box box, @Nullable FeatureSet enabledFeatures) {
        boolean bl = enabledFeatures != null;
        boolean bl2 = box != null;
        boolean bl3 = !this.locatorBarPlus$getDistance().isDummy();
        int i = (bl ? 1 : 0) + (bl2 ? 1 : 0) + (bl3 ? 1 : 0);
        List<Predicate<Entity>> list;
        if (i == 0) {
            list = this.locatorBarPlus$getPredicates();
        } else {
            List<Predicate<Entity>> list2 = new ObjectArrayList<>(this.locatorBarPlus$getPredicates().size() + i);
            list2.addAll(this.locatorBarPlus$getPredicates());
            if (bl) {
                list2.add((entity) -> entity.getType().isEnabled(enabledFeatures));
            }

            if (bl2) {
                list2.add((entity) -> box.intersects(entity.getBoundingBox()));
            }

            if (bl3) {
                list2.add((entity) -> this.locatorBarPlus$getDistance().testSqrt(entity.squaredDistanceTo(pos)));
            }

            list = list2;
        }

        return Util.allOf(list);
    }
}
