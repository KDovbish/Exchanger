package org.example.dto;

import lombok.Data;
import org.example.enumtype.OperationStatus;
import org.example.enumtype.OperationType;

import java.math.BigDecimal;
import java.sql.Timestamp;

/** Ответ системы на новую заявку покупки/продажи валюты */
@Data
public class ResponseDto {
    /** Уникальный идентификатор заявки */
    private Integer id;
    /** Тип операции: покупка/продажа */
    private OperationType type;
    /** Валюта */
    private String ccy;
    /** Сумма в валюте */
    private BigDecimal ccySum;
    /** Эквивалент в гривне */
    private BigDecimal ushSum;
    /** Курс операции */
    private BigDecimal rate;
    /** Телефон клиента */
    private String tel;
    /** otp-пароль */
    private String otp;
    /** Стаус документа заявки: новая/выполнена/отменена */
    private OperationStatus status;
    /** Время формирования заявки  */
    private Timestamp ts;
}
