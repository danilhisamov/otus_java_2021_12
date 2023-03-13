package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class ParallelSequence {
    private static final Logger log = LoggerFactory.getLogger(ParallelSequence.class);
    private static final int START = 1;
    private static final int END = 10;
    private static final int NUMS_COUNT = 19;
    private boolean firstThreadIsActive = true;

    public static void main(String[] args) {
        var ps = new ParallelSequence();
        new Thread(() -> ps.printSequence(() -> !ps.firstThreadIsActive), "Thread_1").start();
        new Thread(() -> ps.printSequence(() -> ps.firstThreadIsActive), "Thread_2").start();
    }

    private synchronized void printSequence(Supplier<Boolean> waitCondition) {
        int delta = 1;
        int num = START;
        int count = 0;

        while (!Thread.currentThread().isInterrupted() && count < NUMS_COUNT) {
            try {
                while (waitCondition.get()) this.wait();

                log.info(String.valueOf(num));
                count++;
                num += delta;
                if (num == START || num == END) delta *= -1;

                firstThreadIsActive = !firstThreadIsActive;
                this.notify();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
