package com.pavlyk.oril.service;

import com.pavlyk.oril.entity.Cryptocurrency;
import com.pavlyk.oril.repository.CryptocurrencyRepository;
import com.pavlyk.oril.service.api.CryptocurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CryptocurrencyServiceImpl implements CryptocurrencyService {
    private final CryptocurrencyRepository cryptocurrencyRepository;

    @Override
    public void save(Cryptocurrency cryptocurrency) {
        if (cryptocurrency != null) {
            cryptocurrencyRepository.save(cryptocurrency);
        }
    }

    @Override
    public Cryptocurrency getCryptoRecordWithMinPrice(String name) {
        return cryptocurrencyRepository.findFirstByCurrencyName1OrderByPrice(name);
    }

    @Override
    public Cryptocurrency getCryptoRecordWithMaxPrice(String name) {
        return cryptocurrencyRepository.findFirstByCurrencyName1OrderByPriceDesc(name);
    }

    @Override
    public List<Cryptocurrency> getAll() {
        return cryptocurrencyRepository.findAll();
    }

    @Override
    public List<Cryptocurrency> getPaginatedRecords(String name, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("price"));
        Page<Cryptocurrency> pageResult = cryptocurrencyRepository.getCryptocurrencyByCurrencyName1(pageable, name);
        if(pageResult.hasContent()){
            return pageResult.getContent();
        }
        return new ArrayList<>();
    }
}
