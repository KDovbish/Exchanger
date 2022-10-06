package org.example.service;

import org.example.entity.RequestEntity;
import org.example.repositary.FinalReport;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

/** Интерфейс для работы с таблицей "Заявки"(request) */
public interface RequestService {
     /**
      * Заявка на покупку(Банком покупает - Клиент продает)
      * @param ccy Валюта покупки
      * @param ccySum Сумма валюты
      * @param firstName Имя клиента
      * @param lastName Фамилия клиента
      * @param tel Телефон клиента
      * @return Запись заявки в БД
      */
     RequestEntity bankBuy(String ccy, BigDecimal ccySum, String firstName, String lastName, String tel);


     /**
      * Заявка на продажу(Банко продает - Клиент покупает)
      * @param ccy Валюта продажи
      * @param ccySum Сумма валюты
      * @param firstName Имя клиента
      * @param lastName Фамилия клиента
      * @param tel Телефон клиента
      * @return Запись заявки в БД
      */
     RequestEntity bankSale(String ccy, BigDecimal ccySum, String firstName, String lastName, String tel);


     /**
      * Подтверждение покупки/продажи
      * @param tel Клиент предоставляет свой телефон, под которым была зарегистрирована новая заявка
      * @param otp Клиент предоставляет пароль, полученный как результат формирования заявки
      * @return Заявка, которая была либо подтверждена(и переведена в состояние DONE), либо не подтверждена(и переведена в CANCELLED).
      */
     List<RequestEntity> confirm(String tel, String otp);




     /**
      * Получить все завки в статусе "Новый" по номеру телефона
      * @param tel Номер телефона
      * @return Список заявок
      */
     List<RequestEntity> getNewRequestByTel(String tel);

     /**
      * Удалить запись заявки по Id
      * @param id Уникальный идентификатор заявки
      * @return Объект, описывающий удаленную запись, в обертке Optional
      */
     Optional<RequestEntity> delete(Integer id);

     /**
      * Выполнить запрос с агрегацией данных к таблице заявок для получения суммарных данных за опердень
      * @return Список объектов, реализующих интерфейс FinalReport, который содержит методы для получения результатов запроса с агрегацией
      */
     List<FinalReport> makeFinalReport();

     List<RequestEntity> makeDateRangeRequestReport(String ccy, Date beginDate, Date endDate);


}
