package ru.otus.homework.service;

import ru.otus.homework.Nominal;

import java.util.Collection;
import java.util.Map;

public interface MoneyProcessor {
    void receive(Collection<Nominal> banknotes);
    void receive(Map<Nominal, Integer> banknotes);
    Map<Nominal, Integer> give(Integer sum);
}
