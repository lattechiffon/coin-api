package com.lattechiffon.coinapi.repository;

import com.lattechiffon.coinapi.domain.Market;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarketRepository extends JpaRepository<Market, Long> {
    Optional<Market> findByMarketname(String marketname);
    List<Market> findAll();
}
