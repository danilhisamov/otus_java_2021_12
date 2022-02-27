package ru.otus.homework.service;

import ru.otus.homework.Nominal;

import java.util.Map;
import java.util.NavigableSet;

public interface MoneyHolder {
    void put(Nominal nominal, Integer count);
    Map<Nominal, Integer> extract(Nominal nominal, Integer count);
    Map<Nominal, Integer> extract(Map<Nominal, Integer> banknotes);
    Integer getNominalCount(Nominal nominal);
    NavigableSet<Nominal> getNominals();
    String getBalanceAsString();
    Map<Nominal, Integer> getBalanceAsMap();
}
