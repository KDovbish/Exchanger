package org.example.dto;

import lombok.Data;

import java.math.BigDecimal;

/** Заявка на покупку/продажу валюты */
@Data
public class RequestDto {
    /** Валюта */
    private String ccy;
    /** Сумма в валюте */
    private BigDecimal ccySum;
    /** Имя клиента */
    private String firstName;
    /** Фамилия клиента */
    private String lastName;
    /** Телефон клиента */
    private String tel;
}
