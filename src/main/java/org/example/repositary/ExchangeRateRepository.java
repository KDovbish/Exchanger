package org.example.repositary;

import org.example.entity.ExchangeRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** Репозитарий для таблицы "Курсы валют"(exchangerate) */
@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRateEntity, Integer> {
    //  Получить последние курсы по заданной валюте
    @Query(value = "select * from exchangerate where ts in (select max(ts) from exchangerate where ccy = ?1)", nativeQuery = true)
    ExchangeRateEntity findByCcyLast(String ccy);
}
