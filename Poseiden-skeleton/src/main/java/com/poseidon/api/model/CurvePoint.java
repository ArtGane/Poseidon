package com.poseidon.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "curvepoint")
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CurvePoint {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(name = "curve_id")
    Long curveId;
    @Column(name = "term")
    Double term;
    @Column(name = "value")
    Double value;

    @Column(name = "as_of_Date", nullable = true)
    private LocalDateTime asOfDate;

    @Column(name = "creation_date", nullable = true)
    private LocalDateTime creationDate;

}
