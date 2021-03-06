package com.lattechiffon.coinapi.service;

import com.lattechiffon.coinapi.dto.CoinDTO;

import java.util.List;

public interface CoinService {
    List<CoinDTO> getCoins(String coinname);
    List<CoinDTO> getUserCoins(String username);
    void setUserCoin(String username, CoinDTO coinDTO);
    void setUserCoinExpired(CoinDTO coinDTO);
}
