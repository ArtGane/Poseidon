package com.poseidon.api.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class BidDto {

    private Long id;
    private String account;
    private String type;
    private Double bidQuantity;
}
