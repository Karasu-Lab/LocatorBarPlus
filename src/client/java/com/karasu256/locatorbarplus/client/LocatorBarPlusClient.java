package com.karasu256.locatorbarplus.client;

import com.karasu256.locatorbarplus.command.BetterLocatorBarCommand;
import com.karasu256.locatorbarplus.command.LocatorBarTesterCommand;
import com.karasu256.locatorbarplus.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class LocatorBarPlusClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigManager.getProvider().register();

        ClientCommandRegistrationCallback.EVENT.register(LocatorBarTesterCommand::register);
        ClientCommandRegistrationCallback.EVENT.register(BetterLocatorBarCommand::register);
    }
}
