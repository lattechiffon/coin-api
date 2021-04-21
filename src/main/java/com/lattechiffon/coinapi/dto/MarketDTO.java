package com.lattechiffon.coinapi.dto;

import com.lattechiffon.coinapi.domain.Market;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MarketDTO {
    private Long id;
    private String marketname;
    private String coinname;

    public MarketDTO(Market market) {
        this.setId(market.getId());
        this.setMarketname(market.getMarketname());
        this.setCoinname(market.getCoinname());
    }
}
