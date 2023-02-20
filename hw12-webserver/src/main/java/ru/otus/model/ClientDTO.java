package ru.otus.model;

import ru.otus.crm.model.Client;

import java.util.List;

public record ClientDTO(
        Long id,
        String name,
        AddressDTO address,
        List<PhoneDTO> phones
) {
    public static ClientDTO fromEntity(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getName(),
                AddressDTO.fromEntity(client.getAddress()),
                client.getPhones().stream().map(PhoneDTO::fromEntity).toList()
        );
    }
}
