package ru.otus.homework;

public interface Calculable {
    default void calculation(int param1) {
        System.out.printf("default calculation(%s)%n", param1);
    }

    default void calculation(int param1, int param2) {
        System.out.printf("default calculation(%s, %s)%n", param1, param2);
    }

    default void calculation(int param1, int param2, String param3) {
        System.out.printf("default calculation(%s, %s, %s)%n", param1, param2, param3);
    };
}
