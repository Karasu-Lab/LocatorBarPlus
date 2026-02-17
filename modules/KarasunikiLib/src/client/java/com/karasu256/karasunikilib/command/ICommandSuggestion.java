package com.karasu256.karasunikilib.command;

import net.minecraft.util.Identifier;

public interface ICommandSuggestion<T> {
    String getName();

    Identifier getId();

    void onComplete(T value);
}
