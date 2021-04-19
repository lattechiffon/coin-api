package com.lattechiffon.coinapi.dto;

import com.lattechiffon.coinapi.domain.Coin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CoinDTO {
    private Long id;
    private String username;
    private String coinname;
    private Double breakEvenPrice;
    private Double targetPrice;

    public CoinDTO(Coin coin) {
        this.setId(coin.getId());
        this.setCoinname(coin.getCoinname());
        this.setUsername(coin.getUser().getUsername());
        this.setBreakEvenPrice(coin.getBreakEvenPrice());
        this.setTargetPrice(coin.getTargetPrice());
    }
}
