package com.karasu256.locatorbarplus.command;

import com.google.common.collect.Lists;
import com.karasu256.locatorbarplus.client.OverlayManagerState;
import com.karasu256.locatorbarplus.impl.IEntitySelector;
import com.karasu256.locatorbarplus.util.TextMessageBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.util.Formatting;

import java.util.List;

public class LocatorBarTesterCommand extends AbstractCommand {

    public LocatorBarTesterCommand() {
        addSubCommand(new EnableSubCommand());
        addSubCommand(new DisableSubCommand());
    }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        AbstractCommand.register(dispatcher, new LocatorBarTesterCommand());
    }

    @Override
    public String getName() {
        return "locatorbartester";
    }

    public static class EnableSubCommand extends AbstractCommand {
        @Override
        public String getName() {
            return "enable";
        }

        @Override
        public ArgumentBuilder<FabricClientCommandSource, ?> getArgumentBuilder() {
            return ClientCommandManager.literal(getName())
                    .then(ClientCommandManager.argument("targets", EntityArgumentType.entities())
                            .executes(ctx -> {
                                execute(ctx);
                                return 1;
                            })
                    );
        }

        @Override
        public <T> void execute(CommandContext<T> context) {
            if (!(context.getSource() instanceof FabricClientCommandSource source)) return;

            try {
                EntitySelector selector = context.getArgument("targets", EntitySelector.class);
                MinecraftClient client = MinecraftClient.getInstance();

                if (client.player == null || client.world == null) return;

                List<Entity> allEntities = Lists.newArrayList(client.world.getEntities());
                List<Entity> targets = ((IEntitySelector) selector).locatorBarPlus$getEntities(client.player.getPos(), allEntities);

                if (!targets.isEmpty()) {
                    OverlayManagerState state = OverlayManagerState.getInstance();
                    state.setForcedByCommand(true);
                    state.setForcedEntities(targets);
                    source.sendFeedback(TextMessageBuilder.create()
                            .appendTranslatable("command.locatorbarplus.tester.enabled", Formatting.GREEN, targets.size())
                            .build());
                    return;
                }

                source.sendFeedback(TextMessageBuilder.create()
                        .appendTranslatable("command.locatorbarplus.tester.no_targets", Formatting.YELLOW)
                        .build());

            } catch (Exception e) {
                source.sendFeedback(TextMessageBuilder.create()
                        .appendTranslatable("command.locatorbarplus.tester.error", Formatting.RED, e.getMessage())
                        .build());
            }
        }
    }

    public static class DisableSubCommand extends AbstractCommand {
        @Override
        public String getName() {
            return "disable";
        }

        @Override
        public ArgumentBuilder<FabricClientCommandSource, ?> getArgumentBuilder() {
            return ClientCommandManager.literal(getName())
                    .executes(ctx -> {
                        execute(ctx);
                        return 1;
                    });
        }

        @Override
        public <T> void execute(CommandContext<T> context) {
            if (!(context.getSource() instanceof FabricClientCommandSource source)) return;

            OverlayManagerState.getInstance().setForcedByCommand(false);
            source.sendFeedback(TextMessageBuilder.create()
                    .appendTranslatable("command.locatorbarplus.tester.disabled", Formatting.YELLOW)
                    .build());
        }
    }
}
