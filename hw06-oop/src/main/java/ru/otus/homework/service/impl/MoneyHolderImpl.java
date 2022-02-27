package ru.otus.homework.service.impl;

import ru.otus.homework.Nominal;
import ru.otus.homework.service.MoneyHolder;

import java.util.*;

public class MoneyHolderImpl implements MoneyHolder {
    private final TreeMap<Nominal, Integer> cells = new TreeMap<>(Comparator.comparing(Nominal::getValue).reversed());

    public void put(Nominal nominal, Integer count) {
        cells.put(nominal, cells.getOrDefault(nominal, 0) + count);
    }

    public Map<Nominal, Integer> extract(Nominal nominal, Integer count) {
        if (cells.get(nominal) >= count) {
            cells.compute(nominal, (k, v) -> v - count);
            return Map.of(nominal, count);
        } else {
            throw new IllegalArgumentException(String.format("MoneyHolder doesn't have %s with amount of %s", nominal, count));
        }
    }

    @Override
    public Map<Nominal, Integer> extract(Map<Nominal, Integer> banknotes) {
        final Map<Nominal, Integer> money = new HashMap<>();

        banknotes.entrySet()
                .stream()
                .map(entry -> extract(entry.getKey(), entry.getValue()))
                .forEach(money::putAll);

        return money;
    }

    public Integer getNominalCount(Nominal nominal) {
        return cells.getOrDefault(nominal, 0);
    }

    public NavigableSet<Nominal> getNominals() {
        return cells.navigableKeySet();
    }

    @Override
    public String getBalanceAsString() {
        return cells.toString();
    }

    @Override
    public Map<Nominal, Integer> getBalanceAsMap() {
        return Map.copyOf(cells);
    }


}
