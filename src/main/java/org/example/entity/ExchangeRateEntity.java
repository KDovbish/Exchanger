package org.example.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/** Сущность, описывающая запись в таблице exchangerate(курсы валют) */
@Entity
@Table(name = "exchangerate")
@Data
public class ExchangeRateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String ccy;
    private BigDecimal buy;
    private BigDecimal sale;
    private Timestamp ts;
}
