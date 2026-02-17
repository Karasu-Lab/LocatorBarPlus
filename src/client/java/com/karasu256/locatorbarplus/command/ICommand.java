package com.karasu256.locatorbarplus.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ICommand {
    String getName();
    ArgumentBuilder<FabricClientCommandSource, ?> getArgumentBuilder();

    List<ICommand> getSubCommands();
    <T> List<ICommandSuggestion<T>> getSuggestions();

    <T> CompletableFuture<ICommandSuggestion<T>> onComplete();
    <T> void execute(CommandContext<T> context);
    <T> void execute(CommandContext<T> context, CommandRegistryAccess registryAccess);
}
