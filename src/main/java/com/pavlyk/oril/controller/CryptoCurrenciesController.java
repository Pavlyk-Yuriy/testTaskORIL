package com.pavlyk.oril.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavlyk.oril.entity.Cryptocurrency;
import com.pavlyk.oril.service.api.CryptocurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/cryptocurrencies")
@AllArgsConstructor
public class CryptoCurrenciesController {
    private static final String URL1 = "https://cex.io/api/last_price/BTC/USD";
    private static final String URL2 = "https://cex.io/api/last_price/ETH/USD";
    private static final String URL3 = "https://cex.io/api/last_price/XRP/USD";
    public static final String CRYPTO_NAME1 = "BTC";
    public static final String CRYPTO_NAME2 = "XRP";
    public static final String CRYPTO_NAME3 = "ETH";
    private final CryptocurrencyService cryptocurrencyService;

    @GetMapping("/csv")
    public void generateCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=result.csv");
        downloadCsv(response.getWriter(), List.of(CRYPTO_NAME1, CRYPTO_NAME2, CRYPTO_NAME3));
    }

    private void downloadCsv(PrintWriter writer, List<String> cryptocurrencies) {
        writer.write("Cryptocurrency Name, Min Price, Max Price\n");
        for (String name : cryptocurrencies) {
            writer.write(name + "," + cryptocurrencyService.getCryptoRecordWithMinPrice(name).getPrice() + ","
                    + cryptocurrencyService.getCryptoRecordWithMaxPrice(name).getPrice() + "\n");
        }
    }

    @GetMapping("/minprice")
    public Cryptocurrency getCryptocurrencyWithMinPrice(@RequestParam(name = "name") String name) {
        return cryptocurrencyService.getCryptoRecordWithMinPrice(name);
    }

    @GetMapping("/all")
    public List<Cryptocurrency> getByPrice() {
        return cryptocurrencyService.getAll();
    }

    @GetMapping("/maxprice")
    public Cryptocurrency getCryptocurrencyWithMaxPrice(@RequestParam(name = "name") String currencyName) {
        if (CRYPTO_NAME1.equals(currencyName) || CRYPTO_NAME2.equals(currencyName) || CRYPTO_NAME3.equals(currencyName)) {
            return cryptocurrencyService.getCryptoRecordWithMaxPrice(currencyName);
        }
        throw new NoSuchElementException("Request param is not correct...");
    }

    @GetMapping
    public List<Cryptocurrency> getPagination(@RequestParam String name, @RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return cryptocurrencyService.getPaginatedRecords(name, page, size);
    }

    @GetMapping("/start")
    public void start() {
        while (true) {
            Cryptocurrency cryptocurrency1 = getCryptoCurrency(URL1);
            Cryptocurrency cryptocurrency2 = getCryptoCurrency(URL2);
            Cryptocurrency cryptocurrency3 = getCryptoCurrency(URL3);
            cryptocurrencyService.save(cryptocurrency1);
            cryptocurrencyService.save(cryptocurrency2);
            cryptocurrencyService.save(cryptocurrency3);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Cryptocurrency getCryptoCurrency(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (root != null) {
            return Cryptocurrency.builder()
                    .currencyName1(root.path("curr1").toString().replaceAll("\"", ""))
                    .currencyName2(root.path("curr2").toString().replaceAll("\"", ""))
                    .price(root.path("lprice").asDouble())
                    .build();
        }
        return null;
    }
}
