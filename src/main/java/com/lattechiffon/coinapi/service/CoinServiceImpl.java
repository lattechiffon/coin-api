package com.lattechiffon.coinapi.service;

import com.lattechiffon.coinapi.domain.Coin;
import com.lattechiffon.coinapi.dto.CoinDTO;
import com.lattechiffon.coinapi.repository.CoinRepository;
import com.lattechiffon.coinapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {

    private final UserRepository userRepository;
    private final CoinRepository coinRepository;

    @Override
    public List<CoinDTO> getUserCoins(String username) throws UsernameNotFoundException {
        List<Coin> coins = coinRepository.findByUserAndIsCoinNotExpiredFalse(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The username is not found.")));

        List<CoinDTO> coinDTOs = new ArrayList<>();

        for (Coin coin : coins) {
            CoinDTO coinDTO = new CoinDTO();
            coinDTO.setCoinname(coin.getCoinname());
            coinDTO.setUsername(coin.getUser().getUsername());
            coinDTO.setBreakEvenPrice(coin.getBreakEvenPrice());
            coinDTO.setTargetPrice(coin.getTargetPrice());

            coinDTOs.add(coinDTO);
        }

        return coinDTOs;
    }

    @Override
    public void setUserCoin(String username, CoinDTO coinDTO) throws Exception {
        Coin coin = new Coin();
        coin.setUser(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The username is not found.")));
        coin.setCoinname(coinDTO.getCoinname());
        coin.setBreakEvenPrice(coinDTO.getBreakEvenPrice());
        coin.setTargetPrice(coinDTO.getTargetPrice());
        coin.setIsCoinNotExpired(false);
        coinRepository.save(coin);
    }
}
