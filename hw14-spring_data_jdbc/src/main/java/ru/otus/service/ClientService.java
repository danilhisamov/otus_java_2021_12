package ru.otus.service;

import ru.otus.model.Client;

import java.util.List;

public interface ClientService {
    List<Client> getClients();

    Client save(Client client);
}
