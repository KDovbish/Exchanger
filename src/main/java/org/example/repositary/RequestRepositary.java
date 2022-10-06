package org.example.repositary;

import org.example.entity.RequestEntity;
import org.example.enumtype.OperationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/** Репозитарий для таблицы "Заявки"(request) */
@Repository
public interface RequestRepositary extends JpaRepository<RequestEntity, Integer> {
    /**  Отдать заявки по номеру телефона и статусу заявки */
    List<RequestEntity> findByTelAndStatus(String tel, OperationStatus status);

    /**  Подсчет суммарного количества заявок, сумм в разрезе по валютам и типам операций */
    @Query(value = "select ccy, type, count(*) as count, sum(ccysum) as ccysum, sum(uahsum) as uahsum from request where status =  1 group by ccy, type", nativeQuery = true)
    List<FinalReport> makeFinalReport();

    /**  Получить все заявки по заданной валюте за заданный период */
    List<RequestEntity> findByCcyAndTsBetween(String ccy, Timestamp beginDate, Timestamp endDate);
}
