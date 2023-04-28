package com.poseidon.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.security.Timestamp;


@Entity
@Table(name = "bid")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Bid {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Account is mandatory")
    private String account;

    @Column
    @NotBlank(message = "Type is mandatory")
    private String type;

    @Column(name = "bid_quantity")
    private Double bidQuantity;

    // Besoin de ce constructeur pour les tests
    public Bid(Long id, String account, String type, Double bidQuantity) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }

    // useless //

    @Column(name = "ask_quantity", nullable = true)
    private Double askQuantity;

    @Column(name = "bid_date", nullable = true)
    private Double bidDate;

    @Column(nullable = true)
    private Double ask;

    @Column(name = "bid_list_date", nullable = true)
    private Timestamp bidListDate;

    @Column(nullable = true)
    private String commentary;

    @Column(nullable = true)
    private String security;

    @Column(nullable = true)
    private String status;

    @Column(nullable = true)
    private String trader;

    @Column(nullable = true)
    private String book;

    @Column(name = "creation_name", nullable = true)
    private String creationName;

    @Column(name = "creation_date", nullable = true)
    private Timestamp creationDate;

    @Column(name = "revision_name", nullable = true)
    private String revisionName;

    @Column(name = "revision_date", nullable = true)
    private Timestamp revisionDate;

    @Column(name = "deal_name", nullable = true)
    private String dealName;

    @Column(name = "deal_type", nullable = true)
    private String dealType;

    @Column(name = "source_list_id", nullable = true)
    private String sourceListId;

    @Column(nullable = true)
    private String side;

}
