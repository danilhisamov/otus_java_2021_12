package ru.otus.homework;

public class TestLogging implements Calculable {
    @Log
    public void calculation(int param) {
        System.out.printf("impl override calculation(%s)%n", param);
    };

    public void calculation(int param1, int param2) {
        System.out.printf("impl override calculation(%s, %s)%n", param1, param2);;
    }
}
