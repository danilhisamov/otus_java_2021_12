package ru.otus.homework.service.impl;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.Nominal;
import ru.otus.homework.service.MoneyHolder;
import ru.otus.homework.service.MoneyProcessor;

import java.util.*;

@RequiredArgsConstructor
public class MoneyProcessorImpl implements MoneyProcessor {
    private final MoneyHolder moneyHolder;

    @Override
    public void receive(Collection<Nominal> banknotes) {
        Map<Nominal, Integer> nominalMap = new HashMap<>();
        banknotes.forEach(banknote -> nominalMap.put(banknote, nominalMap.getOrDefault(banknote, 0) + 1));
        receive(nominalMap);
    }

    public void receive(Map<Nominal, Integer> banknotes) {
        banknotes.forEach(moneyHolder::put);
    }

    public Map<Nominal, Integer> give(Integer sum) {
        var sortedNominals = moneyHolder.getNominals()
                .stream()
                .sorted(Comparator.comparingInt(Nominal::getValue).reversed())
                .toList();

        var calculatedNominals = calculateNominalsCount(sortedNominals, sum);

        return moneyHolder.extract(calculatedNominals);
    }

    private Map<Nominal, Integer> calculateNominalsCount(List<Nominal> sortedNominals, Integer sum) {
        Map<Nominal, Integer> nominalsCount = new HashMap<>();

        var tmpSum = 0;

        for (var nominal : sortedNominals) {
            int nominalCount = (sum - tmpSum) / nominal.getValue();

            if (nominalCount > 0) {
                int nominalCountInCell = moneyHolder.getNominalCount(nominal);

                int nominalCountFact = Math.min(nominalCountInCell, nominalCount);

                nominalsCount.put(nominal, nominalCountFact);
                tmpSum = tmpSum + (nominalCountFact * nominal.getValue());
                if (tmpSum == sum) {
                    break;
                }
            }
        }

        if (tmpSum != sum) {
            throw new IllegalArgumentException(String.format("Can't give requested amount of money: %s%nYou can get: %s", sum, tmpSum));
        }

        return nominalsCount;
    }
}
