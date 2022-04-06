package ru.otus.processor.homework;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.model.Message;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EvenSecondExProcessorTest {
    private EvenSecondExProcessor processor;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void process(boolean isEvenSecond) {
        processor = new EvenSecondExProcessor(() -> LocalDateTime.of(2022, 4, 6, 22, 29, isEvenSecond ? 14 : 15));
        var message = new Message.Builder(1L).build();
        if (isEvenSecond) {
            assertThrows(EvenSecondException.class, () -> processor.process(message));
        } else {
            assertDoesNotThrow(() -> processor.process(message));
        }
    }
}
