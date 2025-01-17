package com.poseidon.api.repository;

import com.poseidon.api.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

}
