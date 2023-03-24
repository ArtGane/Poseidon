package com.poseidon.api.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class TradeDto {
    private Long id;
    private String account;
    private String type;
    private Double buyQuantity;
    private String action;
}
