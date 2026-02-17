package com.karasu256.locatorbarplus.config.impl;

import com.karasu256.karasunikilib.config.AbstractReflectedFieldGetter;
import com.karasu256.locatorbarplus.config.ModConfig;

public class AutoConfigFieldsAdapter extends AbstractReflectedFieldGetter {
    public AutoConfigFieldsAdapter() {
        super(ModConfig.class);
    }
}
