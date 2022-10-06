package org.example.dto;

import lombok.Data;
import org.example.enumtype.OperationType;

import java.math.BigDecimal;

/** dto-объект отображающий финальный отчет при закрытии дня */
@Data
public class FinalReportDto {
    /** Валюта(группировка) */
    private String ccy;
    /** Тип операции(группировка)*/
    private OperationType type;
    /** Количество операций(агрегация) */
    private Integer count;
    /** Сумма в валюте(агрегация) */
    private BigDecimal ccySum;
    /** Эквивалент в гривне(агрегация) */
    private BigDecimal uahSum;
}
