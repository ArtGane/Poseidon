package com.poseidon.api.repository;

import com.poseidon.api.model.CurvePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CurvePointRepository extends JpaRepository<CurvePoint, Long> {
}
