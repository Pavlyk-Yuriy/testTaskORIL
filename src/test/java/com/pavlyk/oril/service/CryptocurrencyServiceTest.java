package com.pavlyk.oril.service;

import com.pavlyk.oril.entity.Cryptocurrency;
import com.pavlyk.oril.repository.CryptocurrencyRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CryptocurrencyServiceTest {
    @InjectMocks
    private CryptocurrencyServiceImpl service;

    @Mock
    private CryptocurrencyRepository repository;

    @Test
    public void saveUserTest(){
        Cryptocurrency cryptocurrency = new Cryptocurrency("id","BTC","USD",124.6);
        service.save(cryptocurrency);
        verify(repository,times(1)).save(eq(cryptocurrency));
    }

    @Test
    public void saveUserNullTest(){
        Cryptocurrency cryptocurrency = null;
        service.save(cryptocurrency);
        verify(repository,times(0)).save(cryptocurrency);
    }

    @Test
    public void getCryptoMinPriceTest(){
        String name = "BTC";
        Cryptocurrency expected = new Cryptocurrency("id","BTC","USD",124.6);
        when(repository.findFirstByCurrencyName1OrderByPrice(name)).thenReturn(expected);
        Cryptocurrency actual = service.getCryptoRecordWithMinPrice(name);
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void getCryptoMaxPriceTest(){
        String name = "BTC";
        Cryptocurrency expected = new Cryptocurrency("id","BTC","USD",124.6);
        when(repository.findFirstByCurrencyName1OrderByPriceDesc(name)).thenReturn(expected);
        Cryptocurrency actual = service.getCryptoRecordWithMaxPrice(name);
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void getAllTest(){
        List<Cryptocurrency> expectedList = List.of(new Cryptocurrency("id1","BTC","USD",124.6),
                new Cryptocurrency("id2","XRP","USD",1240.6));
        when(repository.findAll()).thenReturn(expectedList);
        List<Cryptocurrency> actual = service.getAll();
        Assert.assertEquals(expectedList,actual);
    }
}
