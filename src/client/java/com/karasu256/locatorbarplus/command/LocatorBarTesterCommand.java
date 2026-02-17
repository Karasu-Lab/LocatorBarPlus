package com.karasu256.locatorbarplus.command;

import com.google.common.collect.Lists;
import com.karasu256.locatorbarplus.client.OverlayManagerState;
import com.karasu256.locatorbarplus.impl.IEntitySelector;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

import java.util.List;

public class LocatorBarTesterCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("locatorbartester")
                .then(ClientCommandManager.literal("enable")
                        .then(ClientCommandManager.argument("targets", EntityArgumentType.entities())
                                .executes(LocatorBarTesterCommand::enableParams)
                        )
                )
                .then(ClientCommandManager.literal("disable")
                        .executes(LocatorBarTesterCommand::disableParams)
                )
        );
    }

    private static int enableParams(CommandContext<FabricClientCommandSource> context) {
        try {
            EntitySelector selector = context.getArgument("targets", EntitySelector.class);
            MinecraftClient client = MinecraftClient.getInstance();
            
            if (client.player == null || client.world == null) return 0;

            List<Entity> allEntities = Lists.newArrayList(client.world.getEntities());
            List<Entity> targets = ((IEntitySelector) selector).locatorBarPlus$getEntities(client.player.getPos(), allEntities);
            
            if (!targets.isEmpty()) {
                OverlayManagerState state = OverlayManagerState.getInstance();
                state.setForcedByCommand(true);
                state.setForcedEntities(targets);
                context.getSource().sendFeedback(Text.translatable("command.locatorbarplus.tester.enabled", targets.size()));
                return targets.size();
            }
            
            context.getSource().sendFeedback(Text.translatable("command.locatorbarplus.tester.no_targets"));
            return 0;

        } catch (Exception e) {
            context.getSource().sendFeedback(Text.translatable("command.locatorbarplus.tester.error", e.getMessage()));
            return 0;
        }
    }

    private static int disableParams(CommandContext<FabricClientCommandSource> context) {
        OverlayManagerState.getInstance().setForcedByCommand(false);
        context.getSource().sendFeedback(Text.translatable("command.locatorbarplus.tester.disabled"));
        return 1;
    }
}
