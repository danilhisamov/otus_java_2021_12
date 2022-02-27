package ru.otus.homework;

import ru.otus.homework.service.MoneyHolder;
import ru.otus.homework.service.MoneyProcessor;
import ru.otus.homework.service.impl.MoneyHolderImpl;
import ru.otus.homework.service.impl.MoneyProcessorImpl;

import java.util.*;

public class ATM {
    private final MoneyHolder moneyHolder;
    private final MoneyProcessor moneyProcessor;

    public ATM() {
        this.moneyHolder = new MoneyHolderImpl();
        this.moneyProcessor = new MoneyProcessorImpl(moneyHolder);
    }

    public void receive(Collection<Nominal> banknotes) {
        moneyProcessor.receive(banknotes);
    }

    public void receive(Map<Nominal, Integer> banknotes) {
        moneyProcessor.receive(banknotes);
    }

    public Map<Nominal, Integer> give(Integer sum) {
        return moneyProcessor.give(sum);
    }

    public String getBalanceAsString() {
        return moneyHolder.getBalanceAsString();
    }

    public Map<Nominal, Integer> getBalanceAsMap() {
        return moneyHolder.getBalanceAsMap();
    }
}
