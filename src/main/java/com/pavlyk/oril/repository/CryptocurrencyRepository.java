package com.pavlyk.oril.repository;

import com.pavlyk.oril.entity.Cryptocurrency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptocurrencyRepository extends MongoRepository<Cryptocurrency, String>,
        PagingAndSortingRepository<Cryptocurrency, String> {

    Cryptocurrency findFirstByCurrencyName1OrderByPrice(String currencyName1);

    Cryptocurrency findFirstByCurrencyName1OrderByPriceDesc(String currencyName1);

    Page<Cryptocurrency> getCryptocurrencyByCurrencyName1(Pageable pageable,String name);

    Cryptocurrency getCryptocurrencyByCurrencyName1(String name);
}
