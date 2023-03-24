package com.poseidon.api.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RatingDto {

    private Long id;
    private String moodysRating;
    private String sandPRating;
    private String fitchRating;
    private String orderNumber;

}
