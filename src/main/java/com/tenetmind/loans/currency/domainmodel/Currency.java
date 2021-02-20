package com.tenetmind.loans.currency.domainmodel;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "currencies")
@NoArgsConstructor
@Getter
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Currency(String name) {
        this.name = name;
    }

}
