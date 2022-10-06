package org.example;

import org.example.dto.*;
import org.example.exception.EntityNotFoundException;
import org.example.exception.OperdayIsAlreadyOpenException;
import org.example.exception.OperdayIsNotOpenException;
import org.example.exception.PrivatExchangeRateException;

import java.sql.Date;
import java.util.List;

/**
 * Интерфейс валютообменного киоска
 */
public interface Exchanger {
    /**
     * Открыть опердень
     * @throws OperdayIsAlreadyOpenException    Попытка открытия уже открытого опердня
     * @throws PrivatExchangeRateException      Проблемы при выкачке курсов валют с ресурса Приват банка
     */
    void open() throws OperdayIsAlreadyOpenException, PrivatExchangeRateException;

    /**
     * Закрыть опердень
     * @throws OperdayIsNotOpenException Если опердень не открыт, выполнение операции невозможно
     */
    void close() throws OperdayIsNotOpenException;

    /**
     * Принудительно установить курсы покупки/продажи по заданной валюте
     * @param exchangeRate Объект, через который передается валюта, курс покупки, курс продажи
     * @return Объект, в котором фиксируется то что было сохранено в базе
     * @throws OperdayIsNotOpenException Если опердень не открыт, выполнение операции невозможно
     */
    ExchangeRateDto setExchangeRate(ExchangeRateDto exchangeRate) throws OperdayIsNotOpenException;

    /**
     * Заявка на покупку валюты(Банк покупает - Клиент продает)
     * @param request Какую валюту банк покупает, сумма валюты, Фамилия и Имя клиента, Телефон клиента
     * @return Тип выполненной операции(покупка/продажа), валюта, сумма валюты, эквивалент в гривне, курс(покупки/продажи), телефон клиента, otp-пароль
     * @throws OperdayIsNotOpenException Если опердень не открыт, выполнение операции невозможно
     */
    ResponseDto requestBankBuy(RequestDto request) throws OperdayIsNotOpenException;

    /**
     * Заявка на продажу валюты(Банк продает - Клиент покупает)
     * @param request Какую валюту банк продает, сумма валты, Фамилия и Имя клиента, Телефон клиента
     * @return Тип выполненной операции(покупка/продажа), валюта, сумма валюты, эквивалент в гривне, курс(покупки/продажи), телефон клиента, otp-пароль
     * @throws OperdayIsNotOpenException Если опердень не открыт, выполнение операции невозможно
     */
    ResponseDto requestBankSale(RequestDto request) throws OperdayIsNotOpenException;

    /**
     * Подтверждение заявки продажи/покупки
     * @param confirmDto Объект, содержащий телефон и пароль, которые передает клиент для подтверждения заявки
     * @return Объект, содержащий информацию о корректности пароля, который передал клиент и количестве обработанных заявок
     * @throws OperdayIsNotOpenException Если опердень не открыт, выполнение операции невозможно
     */
    ConfirmResponseDto confirm(ConfirmRequestDto confirmDto) throws OperdayIsNotOpenException;

    /**
     * Получить заявки в статусе "Новый" по заданному номеру телефона.
     * Метод используется как часть операции удаления заявки.
     * @param tel Телефон
     * @return Список заявок
     */
    List<ResponseDto> getNewRequestByTel(String tel);

    /**
     * Удаление заявки по id
     * @param Id Уникальный идентификатор удаляемой записи
     * @return Объект, описывающий удаленную запись
     * @throws OperdayIsNotOpenException Если опердень не открыт, выполнение операции невозможно
     * @throws EntityNotFoundException Заявка с Id на существует
     */
    ResponseDto delete(Integer Id) throws OperdayIsNotOpenException, EntityNotFoundException;

    /**
     * Сформировать отчет о заявках за период
     * @param ccy Отбираемая валюта
     * @param beginDate Начальная дата отчета
     * @param endDate Конечная дата отчета
     * @return Список заявок
     */
    List<ResponseDto> makeDateRangeRequestReport(String ccy, Date beginDate, Date endDate);
}
