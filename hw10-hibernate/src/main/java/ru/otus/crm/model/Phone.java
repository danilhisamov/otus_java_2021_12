package ru.otus.crm.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "phone")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @ToString.Exclude
    private Client client;

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }
}
