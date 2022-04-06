package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class EvenSecondExProcessor implements Processor {
    private final LocalDateTimeProvider provider;

    public EvenSecondExProcessor(LocalDateTimeProvider provider) {
        this.provider = provider;
    }

    @Override
    public Message process(Message message) {
        var ldt = provider.get();
        if (ldt.getSecond() % 2 == 0) {
            throw new EvenSecondException("Second is even: " + ldt);
        }
        return message;
    }
}
