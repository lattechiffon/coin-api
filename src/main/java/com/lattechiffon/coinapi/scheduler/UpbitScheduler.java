package com.lattechiffon.coinapi.scheduler;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.lattechiffon.coinapi.dto.CoinDTO;
import com.lattechiffon.coinapi.dto.MarketDTO;
import com.lattechiffon.coinapi.service.CoinService;
import com.lattechiffon.coinapi.service.MarketService;
import com.lattechiffon.coinapi.service.NotificationService;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UpbitScheduler {

    private final CoinService coinService;
    private final MarketService marketService;
    private final NotificationService notificationService;

    private final Set<String> markets = new HashSet<>();
    private HttpGet httpGet;

    @PostConstruct
    private void initUpbitScheduler() {
        updateHttpUri();
    }

    public void updateHttpUri() {
        markets.addAll(marketService.getAllMarkets().stream()
                .map(MarketDTO::getMarketname)
                .collect(Collectors.toSet()));

        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("https://api.upbit.com/v1/ticker?markets=");

            for (String marketname : markets) {
                urlBuilder.append(marketname);
                urlBuilder.append(',');
            }

            if (markets.size () < 1) {
                urlBuilder.append("KRW-BTC");
            } else {
                urlBuilder.deleteCharAt(urlBuilder.length() - 1);
            }

            URIBuilder uriBuilder = new URIBuilder(urlBuilder.toString());
            httpGet = new HttpGet(uriBuilder.build());

            httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpGet.addHeader("Accept", "application/json");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0/3 * * * * *", zone = "Asia/Seoul")
    @Transactional(readOnly = true)
    void checkTickerPrice() {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        System.out.println(httpGet.getURI());
        try {
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);

            if (closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
                JSONParser jsonParser = new JSONParser();
                JSONArray jsonArray = (JSONArray) jsonParser.parse(json);

                for (Object o : jsonArray) {
                    double tradePrice = Double.parseDouble(((JSONObject) o).get("trade_price").toString());

                    List<CoinDTO> coinDTOs = coinService.getCoins(((JSONObject) o).get("market").toString());

                    for (CoinDTO coinDTO : coinDTOs) {
                        if (coinDTO.getTargetPrice() >= tradePrice) {
                            coinService.setUserCoinExpired(coinDTO);

                            try {
                                notificationService.sendAll(notificationService.getNotificationTokens(coinDTO.getUsername()),
                                        "지정가 도달: " + coinDTO.getMarket().getCoinname(),
                                        " - 평균 매수가: " + coinDTO.getBreakEvenPrice()
                                                + "\n - 알림 지정가: " + coinDTO.getTargetPrice()
                                                + "\n - 업비트 시장가: " + tradePrice);
                            } catch (FirebaseMessagingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
