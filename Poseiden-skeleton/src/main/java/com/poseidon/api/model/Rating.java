package com.poseidon.api.model;

import com.poseidon.api.repositories.customconfig.Identifiable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Table(name = "rating")
@NoArgsConstructor
@Data
public class Rating implements Identifiable<Long> {

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

    public Rating( String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) {
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }

    @Override
    public Long getId() {
        return id;
    }
}
