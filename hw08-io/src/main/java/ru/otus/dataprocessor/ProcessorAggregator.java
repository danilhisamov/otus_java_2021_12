package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.*;
import java.util.stream.Collectors;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value
        final var result = new TreeMap<String, Double>();
        data.forEach(m -> result.put(m.getName(), result.getOrDefault(m.getName(), 0D) + m.getValue()));
        return result;
    }
}
