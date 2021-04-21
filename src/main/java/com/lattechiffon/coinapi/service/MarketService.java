package com.lattechiffon.coinapi.service;

import com.lattechiffon.coinapi.dto.MarketDTO;

import java.util.List;
import java.util.Optional;

public interface MarketService {
    List<MarketDTO> getAllMarkets();
    Optional<MarketDTO> getMarket(String marketname);
    void setMarket(MarketDTO marketDTO);
    void initAllMarkets();
}
