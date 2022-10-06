package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ConfirmRequestDto;
import org.example.dto.ExchangeRateDto;
import org.example.dto.RequestDto;
import org.example.dto.ResponseDto;
import org.example.exception.EntityNotFoundException;
import org.example.exception.OperdayIsAlreadyOpenException;
import org.example.exception.OperdayIsNotOpenException;
import org.example.exception.PrivatExchangeRateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

/**
 * REST-контроллер
 */
@RestController
public class WebController {

    @Autowired
    ExchangerImpl exchanger;


    /**
     * Открыть опердень
     * @return result:ok Операция опердня успешно выполнена<br/>
     * result:PrivatExchangeRateException Опердень не открыт. Проблемы при получении курсов из Приватбанка<br/>
     * result:OperdayIsAlreadyOpenException Опердень уже открыт
     */
    @PutMapping("/open")
    public String openOperday() {
        try {
            exchanger.open();
            return "{\"result\":\"ok\"}";
        } catch (PrivatExchangeRateException e) {
            return "{\"result\":\"PrivatExchangeRateException\"}";
        } catch (OperdayIsAlreadyOpenException e) {
            return "{\"result\":\"OperdayIsAlreadyOpenException\"}";
        }
    }

    /**
     * Закрыть опердень
     * @return Сериализированный в json объект {@link org.example.dto.FinalReportDto FinalReportDto} - финальный отчет(статистика за день)<br/>
     * result: OperdayIsNotOpenException Опердень не открыт. Выполнение операции закрытия невозможно.
     */
    @PutMapping("/close")
    public String closeOperday() {
        try {
            exchanger.close();
            return toJson(exchanger.makeFinalReport());
        } catch (OperdayIsNotOpenException e) {
            return "{\"result\":\"OperdayIsNotOpenException\"}";
        }
    }


    /**
     * Принудительно установить курсы валют
     * @param exchangeRateDto Объект {@link org.example.dto.ExchangeRateDto ExchangeRateDto} - курсы, которые необходимо установить
     * @return Сериализированный в json объект {@link org.example.dto.ExchangeRateDto ExchangeRateDto} - курсы, которые были установлены.<br/>
     * result: OperdayIsNotOpenException Опердень не открыт. Выполнение операции закрытия невозможно.
     */
    @PostMapping("/setrate")
    public String setRate(@RequestBody ExchangeRateDto exchangeRateDto) {
        try {
            return toJson(exchanger.setExchangeRate(exchangeRateDto));
        } catch (OperdayIsNotOpenException e) {
            return "{\"result\":\"OperdayIsNotOpenException\"}";
        }
    }


    /**
     * Продажа валюты банком
     * @param request Объект, описывающий заявку на продажу банком валюты
     * @return Сериализированный в json объект {@link org.example.dto.ResponseDto ResponseDto}<br/>
     * result: OperdayIsNotOpenException Опердень не открыт. Выполнение операции закрытия невозможно.
     */
    @PostMapping("/bankbuy")
    public String bankBuy(@RequestBody RequestDto request) {
        try {
            return toJson(exchanger.requestBankBuy(request));
        } catch (OperdayIsNotOpenException e) {
            return "{\"result\":\"OperdayIsNotOpenException\"}";
        }
    }

    /**
     * Покупка банком валюты
     * @param request Объект, описывающий заявку на покупку банком валюты
     * @return Сериализированный в json объект {@link org.example.dto.ResponseDto ResponseDto}<br/>
     * result: OperdayIsNotOpenException Опердень не открыт. Выполнение операции закрытия невозможно.
     */
    @PostMapping("/banksale")
    public String bankSale(@RequestBody RequestDto request) {
        try {
            return toJson(exchanger.requestBankSale(request));
        } catch (OperdayIsNotOpenException e) {
            return "{\"result\":\"OperdayIsNotOpenException\"}";
        }
    }


    /**
     * Подтверждение покупки/продажи валюты
     * @param confirmRequestDto Объект с номером телефона и otp-паролем
     * @return Сериализированный в json объект {@link org.example.dto.ConfirmResponseDto ConfirmResponseDto}<br/>
     * result: OperdayIsNotOpenException Опердень не открыт. Выполнение операции закрытия невозможно.
     */
    @PutMapping("/confirm")
    public String confirm(@RequestBody ConfirmRequestDto confirmRequestDto) {
        try {
            return toJson(exchanger.confirm(confirmRequestDto));
        } catch (OperdayIsNotOpenException e) {
            return "{\"result\":\"OperdayIsNotOpenException\"}";
        }
    }


    /**
     * Получить все заявки в статусе "Новая" для заданного телефона клиента
     * @param tel Номер телефона
     * @return Список объектов {@link org.example.dto.ResponseDto ResponseDto}
     */
    @GetMapping("/getnewrequset")
    public List<ResponseDto> getNewRequestByTel(@RequestParam String tel) {
        return exchanger.getNewRequestByTel(tel);
    }

    /**
     * Удалить заявку по Id
     * @param id Уникальный идентификатор заявки
     * @return Объект {@link org.example.dto.ResponseDto ResponseDto}, описывающий удаленную заявку<br/>
     * result: OperdayIsNotOpenException Опердень не открыт. Выполнение операции закрытия невозможно.<br/>
     * result: EntityNotFoundException Заявка с заданным Id не найдена
     */
    @DeleteMapping("/delete")
    public String delete(@RequestParam Integer id) {
        try {
            return toJson(exchanger.delete(id));
        } catch (OperdayIsNotOpenException e) {
            return "{\"result\":\"OperdayIsNotOpenException\"}";
        } catch (EntityNotFoundException e) {
            return "{\"result\":\"EntityNotFoundException\"}";
        }
    }


    /**
     * Отчет "Заявки за период"
     * @param ccy Валюта
     * @param beginDate Начало периода отчета
     * @param endDate Конец периода отчета
     * @return Список объектов {@link org.example.dto.ResponseDto ResponseDto}
     */
    @GetMapping("/getrequestrange")
    public List<ResponseDto> makeDateRangeRequestReport(@RequestParam String ccy, @RequestParam Date beginDate, @RequestParam Date endDate) {
        return exchanger.makeDateRangeRequestReport(ccy, beginDate, endDate);
    }


    /** Сериализация объекта любого типа в Json-строку */
    private String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{\"result\":\"JsonProcessingException\"}";
        }
    }

}
