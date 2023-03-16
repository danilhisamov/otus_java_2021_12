package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class ParallelSequence {
    private static final Logger log = LoggerFactory.getLogger(ParallelSequence.class);
    private static final int START = 1;
    private static final int END = 10;
    private static final int NUMS_COUNT = 19;
    private int lastActiveThread = 2;

    public static void main(String[] args) {
        var ps = new ParallelSequence();
        new Thread(() -> ps.printSequence(1), "Thread_1").start();
        new Thread(() -> ps.printSequence(2), "Thread_2").start();
    }

    private synchronized void printSequence(int threadNumber) {
        int delta = 1;
        int num = START;
        int count = 0;

        while (!Thread.currentThread().isInterrupted() && count < NUMS_COUNT) {
            try {
                while (lastActiveThread == threadNumber) {
                    this.wait();
                }

                log.info(String.valueOf(num));
                count++;
                num += delta;
                if (num == START || num == END) delta *= -1;

                lastActiveThread = threadNumber;
                this.notify();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
