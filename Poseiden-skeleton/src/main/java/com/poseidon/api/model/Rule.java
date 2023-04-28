package com.poseidon.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "rule")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Rule {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Name is mandatory")
    String name;

    @Column
    String description;

    @Column
    String json;

    @Column
    String template;

    @Column(name = "sql_str")
    String sqlStr;

    @Column(name = "sql_part")
    String sqlPart;


}
