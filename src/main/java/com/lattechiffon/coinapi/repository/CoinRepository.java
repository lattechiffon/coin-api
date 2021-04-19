package com.lattechiffon.coinapi.repository;

import com.lattechiffon.coinapi.domain.Coin;
import com.lattechiffon.coinapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    List<Coin> findByCoinnameAndIsCoinNotExpiredTrue(String coinname);
    List<Coin> findByUserAndIsCoinNotExpiredTrue(User user);
}
