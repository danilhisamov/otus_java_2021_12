package ru.otus.homework;

import ru.otus.homework.Log.LogInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyProvider {
    private ProxyProvider() {}

    static Calculable createCalculableLogProxy(Calculable instance) {
        InvocationHandler handler = new LogInvocationHandler(instance);
        return (Calculable) Proxy.newProxyInstance(ProxyProvider.class.getClassLoader(),
                new Class[]{Calculable.class}, handler);
    }
}
