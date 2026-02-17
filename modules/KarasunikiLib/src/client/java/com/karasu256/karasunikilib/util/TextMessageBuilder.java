package com.karasu256.karasunikilib.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TextMessageBuilder {
    private final MutableText text;

    private TextMessageBuilder(MutableText initial) {
        this.text = initial;
    }

    public static TextMessageBuilder create() {
        return new TextMessageBuilder(Text.empty());
    }

    public static TextMessageBuilder translatable(String key, Object... args) {
        return new TextMessageBuilder(Text.translatable(key, args));
    }

    public TextMessageBuilder append(String content, Formatting formatting) {
        text.append(Text.literal(content).formatted(formatting));
        return this;
    }

    public TextMessageBuilder append(Text content) {
        text.append(content);
        return this;
    }

    public TextMessageBuilder appendTranslatable(String key, Formatting formatting, Object... args) {
        text.append(Text.translatable(key, args).formatted(formatting));
        return this;
    }

    public MutableText build() {
        return text;
    }
}
