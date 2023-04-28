package com.poseidon.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "rating")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Rating {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "moodys_rating")
    @NotBlank(message = "Moody's rating is mandatory")
    private String moodysRating;

    @Column(name = "sandp_rating")
    private String sandPRating;

    @Column(name = "fitch_rating")
    private String fitchRating;

    @Column(name = "order_number")
    private Integer orderNumber;

}
