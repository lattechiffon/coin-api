package com.lattechiffon.coinapi.service;

import com.lattechiffon.coinapi.domain.Market;
import com.lattechiffon.coinapi.dto.MarketDTO;
import com.lattechiffon.coinapi.repository.MarketRepository;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;

    @Override
    public List<MarketDTO> getAllMarkets() {
        List<MarketDTO> marketDTOs = new ArrayList<>();

        for (Market market : marketRepository.findAll()) {
            marketDTOs.add(new MarketDTO(market));
        }

        return marketDTOs;
    }

    @Override
    public Optional<MarketDTO> getMarket(String marketname) {
        Optional<Market> market = marketRepository.findByMarketname(marketname);

        if (!market.isPresent()) {
            return Optional.empty();
        }

        MarketDTO marketDTO = new MarketDTO();
        marketDTO.setId((market.get().getId()));
        marketDTO.setMarketname(market.get().getMarketname());
        marketDTO.setCoinname(market.get().getMarketname());

        return Optional.of(marketDTO);
    }

    @Override
    public void setMarket(MarketDTO marketDTO) {
        Market market = new Market();
        market.setMarketname(marketDTO.getMarketname());
        market.setCoinname(marketDTO.getCoinname());
        market.setIsMarketNotExpired(true);
        marketRepository.save(market);
    }

    @Override
    public void initAllMarkets() {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        try {
            URIBuilder uriBuilder = new URIBuilder("https://api.upbit.com/v1/market/all?isDetails=false");
            HttpGet httpGet = new HttpGet(uriBuilder.build());

            httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpGet.addHeader("Accept", "application/json");
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);

            if (closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
                JSONParser jsonParser = new JSONParser();
                JSONArray jsonArray = (JSONArray) jsonParser.parse(json);

                for (Object o : jsonArray) {
                    MarketDTO marketDTO = new MarketDTO();
                    marketDTO.setMarketname(((JSONObject) o).get("market").toString());
                    marketDTO.setCoinname(((JSONObject) o).get("korean_name").toString());

                    setMarket(marketDTO);
                }
            }
        } catch (URISyntaxException | IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
