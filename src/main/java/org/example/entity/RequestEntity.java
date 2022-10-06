package org.example.entity;

import lombok.Data;
import org.example.enumtype.OperationStatus;
import org.example.enumtype.OperationType;
import org.hibernate.annotations.NotFound;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/** Сущность, описывающая запись в таблице request(заявки на покупку/продажу валюты)*/
@Entity
@Table(name = "request")
@Data
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotFound
    private Integer id;
    private OperationType type;
    private String ccy;
    private BigDecimal rate;
    @Column(name = "ccysum")
    private BigDecimal ccySum;
    @Column(name = "uahsum")
    private BigDecimal uahSum;
    private OperationStatus status;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    private String tel;
    private String otp;
    private Timestamp ts;
}
