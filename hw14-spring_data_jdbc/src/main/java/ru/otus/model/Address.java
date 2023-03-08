package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
@Data
public class Address {
    @Id
    private Long id;
    @Nonnull
    private String street;
    @Nonnull
    private Long clientId;
}
