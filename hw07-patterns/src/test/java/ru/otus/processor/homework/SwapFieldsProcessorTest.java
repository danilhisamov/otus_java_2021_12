package ru.otus.processor.homework;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

import static org.junit.jupiter.api.Assertions.*;

class SwapFieldsProcessorTest {

    @Test
    void process() {
        var message = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .build();

        var result = new SwapFieldsProcessor().process(message);

        assertEquals(message.getField11(), result.getField12());
        assertEquals(message.getField12(), result.getField11());
    }
}
