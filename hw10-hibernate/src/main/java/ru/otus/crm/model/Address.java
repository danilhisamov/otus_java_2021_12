package ru.otus.crm.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    @OneToOne
    @JoinColumn(name = "client_id")
    @ToString.Exclude
    private Client client;

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }
}
