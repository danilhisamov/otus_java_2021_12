package ru.otus.model;

import ru.otus.crm.model.Address;

public record AddressDTO(Long id, String street) {
    public static AddressDTO fromEntity(Address address) {
        return new AddressDTO(address.getId(), address.getStreet());
    }
}
