package org.example.repositary;

import org.example.entity.ExchangeRateEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ExchangeRateRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @Test
    public void findByCcyLast() throws InterruptedException{
        ExchangeRateEntity usdEntity1 = new ExchangeRateEntity();
        usdEntity1.setCcy("USD"); usdEntity1.setBuy(BigDecimal.valueOf(40.00d)); usdEntity1.setTs( new Timestamp(System.currentTimeMillis()) );
        System.out.println(entityManager.persist(usdEntity1));

        ExchangeRateEntity usdEntity2 = new ExchangeRateEntity();
        usdEntity2.setCcy("USD"); usdEntity2.setBuy(BigDecimal.valueOf(40.05d)); usdEntity2.setTs( new Timestamp(System.currentTimeMillis()) );
        System.out.println(entityManager.persist(usdEntity2));

        ExchangeRateEntity lastRateEntity = exchangeRateRepository.findByCcyLast("USD");
        assertEquals(lastRateEntity.getBuy().doubleValue(), 40.05d, 0);
    }
}