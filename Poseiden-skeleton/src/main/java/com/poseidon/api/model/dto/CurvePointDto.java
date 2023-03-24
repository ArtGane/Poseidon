package com.poseidon.api.model.dto;


import lombok.Data;

@Data
public class CurvePointDto {

    private Long id;
    private Integer curveId;
    private Double term;
    private Double value;

}