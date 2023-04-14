package com.poseidon.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "rating")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Rating {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "moodys_rating")
    String moodysRating;

    @Column(name = "sandp_rating")
    String sandPRating;

    @Column(name = "fitch_rating")
    String fitchRating;

    @Column(name = "order_number")
    Integer orderNumber;

}
