package ru.otus.homework;

public class Main {
    public static void main(String[] args) {
        Calculable calculable = ProxyProvider.createCalculableLogProxy(new TestLogging());
        calculable.calculation(6);
        calculable.calculation(6, 7);
        calculable.calculation(6, 7, "Str");
    }
}
