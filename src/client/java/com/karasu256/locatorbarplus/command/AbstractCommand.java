package com.karasu256.locatorbarplus.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractCommand implements ICommand {
    private final List<ICommand> subCommands = new ArrayList<>();

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, ICommand root) {
        LiteralArgumentBuilder<FabricClientCommandSource> builder = ClientCommandManager.literal(root.getName());
        
        for (ICommand sub : root.getSubCommands()) {
            builder.then(sub.getArgumentBuilder());
        }
        
        dispatcher.register(builder);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public ArgumentBuilder<FabricClientCommandSource, ?> getArgumentBuilder() {
        return null;
    }

    @Override
    public List<ICommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public <T> List<ICommandSuggestion<T>> getSuggestions() {
        return new ArrayList<>();
    }

    @Override
    public <T> CompletableFuture<ICommandSuggestion<T>> onComplete() {
        return null;
    }

    @Override
    public <T> void execute(CommandContext<T> context) {
        // Default implementation
    }

    @Override
    public <T> void execute(CommandContext<T> context, CommandRegistryAccess registryAccess) {
        execute(context);
    }

    protected void addSubCommand(ICommand subCommand) {
        this.subCommands.add(subCommand);
    }
}
