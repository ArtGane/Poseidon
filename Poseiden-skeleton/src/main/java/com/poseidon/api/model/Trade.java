package com.poseidon.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "trade")
@NoArgsConstructor
@Getter
@Setter
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;
    @Column(name = "account")
    String account;
    @Column(name = "type")
    String type;
    @Column(name = "action")
    String action;


    public Trade(Long id, String account, String type, String action) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.action = action;
    }


    @Column(name = "buy_quantity")
    Double buyQuantity;

    @Column(name = "sellQuantity", nullable = true)
    Double sellQuantity;

    @Column(name = "buyPrice", nullable = true)
    Double buyPrice;

    @Column(name = "sellPrice", nullable = true)
    Double sellPrice;

    @Column(name = "security", nullable = true)
    String security;

    @Column(name = "status", nullable = true)
    String status;

    @Column(name = "trader", nullable = true)
    String trader;

    @Column(name = "benchmark", nullable = true)
    String benchmark;

    @Column(name = "book", nullable = true)
    String book;

    @Column(name = "creationName", nullable = true)
    String creationName;

    @Column(name = "revisionName", nullable = true)
    String revisionName;

    @Column(name = "dealName", nullable = true)
    String dealName;

    @Column(name = "dealType", nullable = true)
    String dealType;

    @Column(name = "sourceListId", nullable = true)
    String sourceListId;

    @Column(name = "side", nullable = true)
    String side;

    @Column(name = "tradeDate", nullable = true)
    private LocalDateTime tradeDate;

    @Column(name = "creationDate", nullable = true)
    private LocalDateTime creationDate;

    @Column(name = "revisionDate")
    private LocalDateTime revisionDate;
}