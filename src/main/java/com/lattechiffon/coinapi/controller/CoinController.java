package com.lattechiffon.coinapi.controller;

import com.lattechiffon.coinapi.config.security.JwtTokenProvider;
import com.lattechiffon.coinapi.dto.CoinDTO;
import com.lattechiffon.coinapi.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/coins")
public class CoinController {

    private final CoinService coinService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping(value = "")
    public List<CoinDTO> getUserCoins(@RequestHeader("Authorization") String token) {
        return coinService.getUserCoins(jwtTokenProvider.getUserIdx(token));
    }

    @PostMapping(value = "")
    public String setUserCoin(@RequestHeader("Authorization") String token, @RequestBody CoinDTO coinDTO) {
        try {
            coinService.setUserCoin(jwtTokenProvider.getUserIdx(token), coinDTO);
        } catch (Exception e) {
            return "fail";
        }

        return "success";
    }
}
