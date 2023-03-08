package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table("client")
@Data
public class Client {
    @Id
    private Long id;
    @Nonnull
    private String name;
    @Nonnull
    @MappedCollection(idColumn = "client_id")
    private Address address;
    @Nonnull
    @MappedCollection(idColumn = "client_id", keyColumn = "order_column")
    private List<Phone> phones = new ArrayList<>();
}
