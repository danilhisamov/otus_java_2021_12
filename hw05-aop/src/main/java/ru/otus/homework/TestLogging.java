package ru.otus.homework;

public class TestLogging implements Calculable {
    @Log
    public void calculation(int param) {
        System.out.printf("impl override calculation(%s)%n", param);
    };
}
