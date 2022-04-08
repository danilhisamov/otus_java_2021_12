package ru.otus.processor.homework;

import java.time.LocalDateTime;

@FunctionalInterface
public interface LocalDateTimeProvider {
    LocalDateTime get();
}
