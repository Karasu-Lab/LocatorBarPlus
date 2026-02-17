package com.karasu256.locatorbarplus.config.impl;

import net.minecraft.util.ActionResult;

public abstract class AbstractNotifyListener<T extends me.shedaniel.autoconfig.ConfigData> implements INotifyListener<IConfigProvider<T>, T> {
    public final T config;
    public final IConfigProvider<T> provider;
    private boolean listening = false;
    private boolean isProcessing = false;

    protected AbstractNotifyListener(T initializevalue, IConfigProvider<T> provider) {
        this.config = initializevalue;
        this.provider = provider;
    }

    @Override
    public void listen() {
        if (listening) return;
        if (provider instanceof AutoConfigProvider<T> autoProvider) {
            autoProvider.getHolder().registerSaveListener((holder, value) -> {
                if (isProcessing) return ActionResult.SUCCESS;
                isProcessing = true;
                try {
                    onChange(value, ConfigEvents.CHANGE_FIELD);
                } finally {
                    isProcessing = false;
                }
                return ActionResult.SUCCESS;
            });
            listening = true;
        }
    }

    @Override
    public void close(INotifyListener<IConfigProvider<T>, T> instance) {
        // me.shedaniel.autoconfig.ConfigHolder doesn't provide an easy way to unregister a specific listener
        // by reference without keeping the registration callback or using an internal list.
        // For this implementation, we will reset the listening flag. 
        // In a more complex scenario, we'd need a more involved bridge.
        this.listening = false;
    }
}
