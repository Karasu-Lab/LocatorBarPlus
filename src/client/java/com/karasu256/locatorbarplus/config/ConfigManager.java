package com.karasu256.locatorbarplus.config;

import com.karasu256.locatorbarplus.config.impl.AutoConfigFieldsAdapter;
import com.karasu256.locatorbarplus.config.impl.AutoConfigProvider;
import com.karasu256.karasunikilib.config.IConfigFieldsAdapter;
import com.karasu256.karasunikilib.config.IConfigProvider;

public class ConfigManager {
    private static final IConfigProvider<ModConfig> PROVIDER = new AutoConfigProvider<>(ModConfig.class);

    private static final IConfigFieldsAdapter FIELDS_ADAPTER = new AutoConfigFieldsAdapter();

    public static IConfigProvider<ModConfig> getProvider() {
        return PROVIDER;
    }

    public static ModConfig getConfig() {
        return PROVIDER.load();
    }

    public static IConfigFieldsAdapter getFieldsAdapter() {
        return FIELDS_ADAPTER;
    }
}
