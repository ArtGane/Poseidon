package com.poseidon.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "curve_id")
    @NotNull(message = "must not be null")
    private Long curveId;

    @Column
    private Double term;

    @Column
    private Double value;

    @Column
    private LocalDateTime asOfDate;

    @Column
    private LocalDateTime creationDate;

}
