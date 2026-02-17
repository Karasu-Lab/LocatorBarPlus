package com.karasu256.karasunikilib.config;

public interface IConfigProvider<T> {
    void save();

    void save(T value);

    T load();

    void register();
}
