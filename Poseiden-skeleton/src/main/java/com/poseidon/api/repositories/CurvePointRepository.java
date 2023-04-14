package com.poseidon.api.repositories;

import com.poseidon.api.model.CurvePoint;
import com.poseidon.api.repositories.customconfig.Identifiable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CurvePointRepository extends JpaRepository<CurvePoint, Long> {
}
