package com.poseidon.api.model;

import com.poseidon.api.repositories.customconfig.Identifiable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.security.Timestamp;


@Entity
@Table(name = "bid")
@NoArgsConstructor
@Getter
@Setter
public class Bid implements Identifiable<Long> {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String account;

    @Column
    private String type;

    @Column(name = "bid_quantity")
    private Double bidQuantity;


    public Bid(String account, String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }

    @Override
    public Long getId() {
        return id;
    }














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
