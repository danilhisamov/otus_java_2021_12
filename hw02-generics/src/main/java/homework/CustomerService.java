package homework;


import java.util.*;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    TreeMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        var entry = map.firstEntry();
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return entry == null ? null : Map.entry(Customer.copy(entry.getKey()), entry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var entry = map.higherEntry(customer);
        return entry == null ? null : Map.entry(Customer.copy(entry.getKey()), entry.getValue());
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
