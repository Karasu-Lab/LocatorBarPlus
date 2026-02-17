package com.karasu256.locatorbarplus.command;

import com.karasu256.locatorbarplus.config.ConfigManager;
import com.karasu256.locatorbarplus.config.ModConfig;
import com.karasu256.locatorbarplus.config.impl.FieldTypes;
import com.karasu256.locatorbarplus.util.TextMessageBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BetterLocatorBarCommand extends AbstractCommand {

    public BetterLocatorBarCommand() {
        addSubCommand(new GetSubCommand());
        addSubCommand(new SetSubCommand());
    }

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        AbstractCommand.register(dispatcher, new BetterLocatorBarCommand());
    }

    private static Object parseValue(String input, FieldTypes type) {
        return switch (type) {
            case BOOL -> Boolean.parseBoolean(input);
            case INT -> Integer.parseInt(input);
            case FLOAT -> Float.parseFloat(input);
            case DOUBLE -> Double.parseDouble(input);
            case LONG -> Long.parseLong(input);
            case STRING -> input;
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

    private static Object getValueByPath(Object obj, String path) throws Exception {
        String[] parts = path.split("\\.");
        Object current = obj;
        for (String part : parts) {
            Field field = current.getClass().getDeclaredField(part);
            field.setAccessible(true);
            current = field.get(current);
        }
        return current;
    }

    private static void setValueByPath(Object obj, String path, Object value) throws Exception {
        String[] parts = path.split("\\.");
        Object current = obj;
        for (int i = 0; i < parts.length - 1; i++) {
            Field field = current.getClass().getDeclaredField(parts[i]);
            field.setAccessible(true);
            current = field.get(current);
        }
        Field targetField = current.getClass().getDeclaredField(parts[parts.length - 1]);
        targetField.setAccessible(true);
        targetField.set(current, value);
    }

    @Override
    public String getName() {
        return "betterlocatorbar";
    }

    @SuppressWarnings("unchecked")
    public static class GetSubCommand extends AbstractCommand {
        @Override
        public String getName() {
            return "get";
        }

        @Override
        public ArgumentBuilder<FabricClientCommandSource, ?> getArgumentBuilder() {
            return ClientCommandManager.literal(getName())
                    .then(ClientCommandManager.argument("field", StringArgumentType.string())
                            .suggests((ctx, b) -> {
                                getSuggestions().forEach(s -> b.suggest(s.getName()));
                                return b.buildFuture();
                            })
                            .executes(ctx -> {
                                execute(ctx);
                                return 1;
                            })
                    );
        }

        @Override
        public <T> List<ICommandSuggestion<T>> getSuggestions() {
            List<ICommandSuggestion<T>> suggestions = new ArrayList<>();
            Map<String, FieldTypes> fields = ConfigManager.getFieldsAdapter().getFields();
            for (Map.Entry<String, FieldTypes> entry : fields.entrySet()) {
                suggestions.add((ICommandSuggestion<T>) new ConfigFieldSuggestion<>(entry.getKey(), entry.getValue()));
            }
            return suggestions;
        }

        @Override
        public <T> void execute(CommandContext<T> context) {
            if (!(context.getSource() instanceof FabricClientCommandSource source)) return;

            String fieldPath = StringArgumentType.getString(context, "field");
            ModConfig config = ConfigManager.getConfig();
            try {
                Object value = getValueByPath(config, fieldPath);
                source.sendFeedback(TextMessageBuilder.create()
                        .appendTranslatable("command.betterlocatorbar.get.success", Formatting.GREEN, fieldPath, String.valueOf(value))
                        .build());
            } catch (Exception e) {
                source.sendError(TextMessageBuilder.create()
                        .appendTranslatable("command.betterlocatorbar.error.generic", Formatting.RED, e.getMessage())
                        .build());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static class SetSubCommand extends AbstractCommand {
        @Override
        public String getName() {
            return "set";
        }

        @Override
        public ArgumentBuilder<FabricClientCommandSource, ?> getArgumentBuilder() {
            return ClientCommandManager.literal(getName())
                    .then(ClientCommandManager.argument("field", StringArgumentType.string())
                            .suggests((ctx, b) -> {
                                getSuggestions().forEach(s -> b.suggest(s.getName()));
                                return b.buildFuture();
                            })
                            .then(ClientCommandManager.argument("value", StringArgumentType.greedyString())
                                    .executes(ctx -> {
                                        execute(ctx);
                                        return 1;
                                    })
                            )
                    );
        }

        @Override
        public <T> List<ICommandSuggestion<T>> getSuggestions() {
            List<ICommandSuggestion<T>> suggestions = new ArrayList<>();
            Map<String, FieldTypes> fields = ConfigManager.getFieldsAdapter().getFields();
            for (Map.Entry<String, FieldTypes> entry : fields.entrySet()) {
                suggestions.add((ICommandSuggestion<T>) new ConfigFieldSuggestion<>(entry.getKey(), entry.getValue()));
            }
            return suggestions;
        }

        @Override
        public <T> void execute(CommandContext<T> context) {
            if (!(context.getSource() instanceof FabricClientCommandSource source)) return;

            String fieldPath = StringArgumentType.getString(context, "field");
            String valueStr = StringArgumentType.getString(context, "value");
            ModConfig config = ConfigManager.getConfig();
            Map<String, FieldTypes> fields = ConfigManager.getFieldsAdapter().getFields();

            if (!fields.containsKey(fieldPath)) {
                source.sendError(TextMessageBuilder.create()
                        .appendTranslatable("command.betterlocatorbar.error.unknown_field", Formatting.RED, fieldPath)
                        .build());
                return;
            }

            try {
                Object typedValue = parseValue(valueStr, fields.get(fieldPath));
                setValueByPath(config, fieldPath, typedValue);
                ConfigManager.getProvider().save(config);
                source.sendFeedback(TextMessageBuilder.create()
                        .appendTranslatable("command.betterlocatorbar.set.success", Formatting.GREEN, fieldPath, String.valueOf(typedValue))
                        .build());
            } catch (Exception e) {
                source.sendError(TextMessageBuilder.create()
                        .appendTranslatable("command.betterlocatorbar.error.set_failed", Formatting.RED, e.getMessage())
                        .build());
            }
        }
    }

    public static class ConfigFieldSuggestion<T> implements ICommandSuggestion<T> {
        private final String name;
        private final T value;

        public ConfigFieldSuggestion(String name, T value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Identifier getId() {
            return null;
        }

        @Override
        public void onComplete(T value) {
        }
    }
}
