package com.lattechiffon.coinapi.scheduler;

import com.lattechiffon.coinapi.domain.Coin;
import com.lattechiffon.coinapi.repository.CoinRepository;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpbitScheduler {

    private final CoinRepository coinRepository;

    @Scheduled(cron = "0/3 * * * * *", zone = "Asia/Seoul")
    public void checkTickerPrice() {

        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

        try {
            URIBuilder uriBuilder = new URIBuilder("https://api.upbit.com/v1/ticker?markets=KRW-ETH,KRW-BTC");

            HttpGet httpGet = new HttpGet(uriBuilder.build());

            httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpGet.addHeader("Accept", "application/json");

            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);

            if (closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
                JSONParser jsonParser = new JSONParser();
                JSONArray jsonArray = (JSONArray) jsonParser.parse(json);

                for (Object o : jsonArray) {
                    double tradePrice = Double.parseDouble(((JSONObject) o).get("trade_price").toString());

                    List<Coin> coins = coinRepository.findByCoinnameAndIsCoinNotExpiredFalse(((JSONObject) o).get("market").toString());

                    for (Coin coin : coins) {
                        if (coin.getTargetPrice() * 0.9 >= tradePrice) {
                            System.out.println("[notification] Please ready for selling.");
                        } else if (coin.getBreakEvenPrice() <= tradePrice * 1.3) {
                            System.out.println("[notification] Too much low price");
                        }
                    }
                }
            }
        } catch (URISyntaxException | IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
