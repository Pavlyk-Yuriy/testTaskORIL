package com.pavlyk.oril.service.api;

import com.pavlyk.oril.entity.Cryptocurrency;

import java.util.List;

public interface CryptocurrencyService {
    void save(Cryptocurrency cryptocurrency);

    Cryptocurrency getCryptoRecordWithMinPrice(String name);

    Cryptocurrency getCryptoRecordWithMaxPrice(String name);

    List<Cryptocurrency> getAll();

    List<Cryptocurrency> getPaginatedRecords(String name, Integer page, Integer size);
}
