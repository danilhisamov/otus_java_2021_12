package ru.otus.model;

import jakarta.annotation.Nonnull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
@Data
public class Phone {
    @Id
    private Long id;
    @Nonnull
    private String number;
    @Nonnull
    private Long clientId;
}
