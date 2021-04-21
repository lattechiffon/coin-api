package com.lattechiffon.coinapi.service;

import com.lattechiffon.coinapi.domain.Coin;
import com.lattechiffon.coinapi.domain.Market;
import com.lattechiffon.coinapi.dto.CoinDTO;
import com.lattechiffon.coinapi.repository.CoinRepository;
import com.lattechiffon.coinapi.repository.MarketRepository;
import com.lattechiffon.coinapi.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {

    private final UserRepository userRepository;
    private final CoinRepository coinRepository;
    private final MarketRepository marketRepository;

    @Getter @Setter
    private Boolean isMarketChanged = true;

    @Override
    public List<CoinDTO> getCoins(String marketname) {
        Optional<Market> market = marketRepository.findByMarketname(marketname);

        if (!market.isPresent()) {
            return new ArrayList<>();
        }

        List<Coin> coins = coinRepository.findByMarketAndIsCoinNotExpiredTrue(market.get());
        List<CoinDTO> coinDTOs = new ArrayList<>();

        for (Coin coin : coins) {
            CoinDTO coinDTO = new CoinDTO(coin);

            coinDTOs.add(coinDTO);
        }

        return coinDTOs;
    }

    @Override
    public List<CoinDTO> getUserCoins(String username) throws UsernameNotFoundException {
        List<Coin> coins = coinRepository.findByUserAndIsCoinNotExpiredTrue(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The username is not found.")));

        List<CoinDTO> coinDTOs = new ArrayList<>();

        for (Coin coin : coins) {
            CoinDTO coinDTO = new CoinDTO(coin);

            coinDTOs.add(coinDTO);
        }

        return coinDTOs;
    }

    @Override
    public void setUserCoin(String username, CoinDTO coinDTO) {
        Coin coin = new Coin();
        coin.setUser(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The username is not found.")));
        coin.setMarket(marketRepository.findByMarketname(coinDTO.getMarket().getMarketname())
                .orElseThrow(IllegalArgumentException::new));
        coin.setBreakEvenPrice(coinDTO.getBreakEvenPrice());
        coin.setTargetPrice(coinDTO.getTargetPrice());
        coin.setIsCoinNotExpired(true);
        coinRepository.save(coin);
    }

    @Override
    public void setUserCoinExpired(CoinDTO coinDTO) {
        coinRepository.findById(coinDTO.getId()).ifPresent(value -> {
            value.setIsCoinNotExpired(false);
            coinRepository.save(value);
        });
    }
}
