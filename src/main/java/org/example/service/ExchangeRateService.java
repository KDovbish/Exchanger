package org.example.service;

import org.example.entity.ExchangeRateEntity;

import java.math.BigDecimal;

/** Интерфейс для работы с таблицей "Курсы валют"(exchangerate) */
public interface ExchangeRateService {
    /**
     * Добавить в БД курс покупки/продажи заданной валюты
     * @param ccy   Код валюты
     * @param buy   Курс покупки
     * @param sale  Курс продаж
     * @return  экземляр объекта, описывающий добавленную в БД запись
     */
    ExchangeRateEntity add(String ccy, BigDecimal buy, BigDecimal sale);

    /**
     * Получить последний курс покупки/продажи по заданной валюте
     * @return  экземпляр объекта, описывающий запись из БД с последним курсом
     */
    ExchangeRateEntity getLast(String ccy);
}
