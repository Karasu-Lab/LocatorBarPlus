package com.karasu256.locatorbarplus.client;

import com.karasu256.locatorbarplus.command.BetterLocatorBarCommand;
import com.karasu256.locatorbarplus.command.LocatorBarTesterCommand;
import com.karasu256.locatorbarplus.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class LocatorBarPlusClient implements ClientModInitializer {

    public static KeyBinding TOGGLE_LOCATOR_BAR;

    @Override
    public void onInitializeClient() {
        ConfigManager.getProvider().register();

        TOGGLE_LOCATOR_BAR = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.locatorbarplus.toggle_locator_bar",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.locatorbarplus.title"
        ));

        ClientCommandRegistrationCallback.EVENT.register(LocatorBarTesterCommand::register);
        ClientCommandRegistrationCallback.EVENT.register(BetterLocatorBarCommand::register);
    }
}
