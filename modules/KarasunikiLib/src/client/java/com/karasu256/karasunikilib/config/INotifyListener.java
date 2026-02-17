package com.karasu256.karasunikilib.config;

public interface INotifyListener<T extends IConfigProvider<C>, C> {
    IConfigProvider<C> getProvider();

    void onChange(C value, ConfigEvents event);

    void listen();

    void close(INotifyListener<T, C> instance);
}
