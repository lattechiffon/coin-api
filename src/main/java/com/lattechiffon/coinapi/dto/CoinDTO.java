package com.lattechiffon.coinapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoinDTO {
    private Long id;
    private String username;
    private String coinname;
    private Double breakEvenPrice;
    private Double targetPrice;
}
