package com.karasu256.locatorbarplus.client;

import com.karasu256.locatorbarplus.command.LocatorBarTesterCommand;
import com.karasu256.locatorbarplus.config.ModConfig;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class LocatorBarPlusClient implements ClientModInitializer {
    public static final String MOD_ID = "locatorbarplus";

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        
        ClientCommandRegistrationCallback.EVENT.register(LocatorBarTesterCommand::register);
    }
}
