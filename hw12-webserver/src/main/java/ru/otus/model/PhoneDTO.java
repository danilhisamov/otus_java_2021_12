package ru.otus.model;

import ru.otus.crm.model.Phone;

public record PhoneDTO(Long id, String number) {
    public static PhoneDTO fromEntity(Phone phone) {
        return new PhoneDTO(phone.getId(), phone.getNumber());
    }
}
