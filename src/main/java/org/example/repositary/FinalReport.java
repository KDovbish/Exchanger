package org.example.repositary;

import org.example.enumtype.OperationType;

import java.math.BigDecimal;

/**
 * Интерфейс используемый для получения результатов агрегированного запроса в репозитарии RequstRepositary.
 * Для финального отчета при закрытии опердня.
 */
public interface FinalReport {
    String getCcy();
    OperationType getType();
    Integer getCount();
    BigDecimal getCcySum();
    BigDecimal getUahSum();
}
