package com.poseidon.api.model;

import com.poseidon.api.repositories.customconfig.Identifiable;
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
@Getter
@Setter
public class CurvePoint implements Identifiable<Long> {

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

    public CurvePoint(Long id, Long curveId, Double term, Double value) {
        this.id = id;
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }

    @Override
    public Long getId() {
        return id;
    }
}
