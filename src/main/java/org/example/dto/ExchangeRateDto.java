package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** dto-объект для установки курсов валют */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateDto {
    /** Валюта */
    private String ccy;
    /** Курс покупки */
    private BigDecimal buy;
    /** Курс продажи */
    private BigDecimal sale;
}
