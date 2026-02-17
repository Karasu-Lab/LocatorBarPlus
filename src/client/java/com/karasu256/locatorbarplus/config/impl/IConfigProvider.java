package com.karasu256.locatorbarplus.config.impl;

public interface IConfigProvider<T> {
    void save();

    void save(T value);

    T load();

    void register();
}
