package com.poseidon.api.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RatingDto {

    private Integer id;

    @NotBlank(message = "Moody'sRating is mandatory")
    private String moodysRating;

    @NotBlank(message = "SandP's Rating is mandatory")
    private String sandPRating;

    @NotBlank(message = "Fitch's Rating is mandatory")
    private String fitchRating;

    private String orderNumber;

}
