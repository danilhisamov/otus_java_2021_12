package ru.otus.protobuf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class SleepUtils {
    private static final Logger log = LoggerFactory.getLogger(SleepUtils.class);

    public static void sleep(TimeUnit timeUnit, long amount) {
        try {
            timeUnit.sleep(amount);
        } catch (InterruptedException e) {
            log.error("Error during thread sleep", e);
            Thread.currentThread().interrupt();
        }
    }
}
