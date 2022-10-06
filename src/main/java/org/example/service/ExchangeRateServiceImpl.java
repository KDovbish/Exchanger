package org.example.service;

import org.example.entity.ExchangeRateEntity;
import org.example.repositary.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService{

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @Override
    public ExchangeRateEntity add(String ccy, BigDecimal buy, BigDecimal sale) {
        ExchangeRateEntity rate = new ExchangeRateEntity();
        rate.setCcy(ccy);
        rate.setBuy(buy);
        rate.setSale(sale);
        rate.setTs( new Timestamp(System.currentTimeMillis()) );
        return exchangeRateRepository.save(rate);
    }

    @Override
    public ExchangeRateEntity getLast(String ccy) {
        return exchangeRateRepository.findByCcyLast(ccy);
    }
}
