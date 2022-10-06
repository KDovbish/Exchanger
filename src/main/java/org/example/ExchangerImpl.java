package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.*;
import org.example.entity.ExchangeRateEntity;
import org.example.entity.RequestEntity;
import org.example.enumtype.OperationStatus;
import org.example.exception.EntityNotFoundException;
import org.example.exception.OperdayIsAlreadyOpenException;
import org.example.exception.OperdayIsNotOpenException;
import org.example.exception.PrivatExchangeRateException;
import org.example.repositary.FinalReport;
import org.example.service.ExchangeRateServiceImpl;
import org.example.service.OperdayServiceImpl;
import org.example.service.RequestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация интерфейса валютообменного киоска
 */
@Service
public class ExchangerImpl implements Exchanger{

    @Autowired
    private OperdayServiceImpl operdayService;
    @Autowired
    private ExchangeRateServiceImpl exchangeRateService;
    @Autowired
    private RequestServiceImpl requestService;

    @Override
    public void open() throws OperdayIsAlreadyOpenException, PrivatExchangeRateException {
        try {
            if (operdayService.isOpen() == false) {
                String bodyResponse = getExchangeRateFromPrivat();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonTree = mapper.readTree(bodyResponse);

                for (JsonNode node : jsonTree) {
                    if ((node.get("ccy").asText().toUpperCase().equals("USD") || node.get("ccy").asText().toUpperCase().equals("EUR")) &&
                            node.get("base_ccy").asText().toUpperCase().equals("UAH")) {

                        exchangeRateService.add(node.get("ccy").asText(),
                                BigDecimal.valueOf(node.get("buy").asDouble()),
                                BigDecimal.valueOf(node.get("sale").asDouble()));

                    }
                }
                operdayService.open();
            } else {
                throw new OperdayIsAlreadyOpenException();
            }
        } catch (URISyntaxException e) {
            throw new PrivatExchangeRateException();
        } catch (JsonProcessingException e) {
            throw new PrivatExchangeRateException();
        }
    }

    @Override
    public void close() throws OperdayIsNotOpenException {
        if (operdayService.isOpen()) {
            operdayService.close();
        } else {
            throw new OperdayIsNotOpenException();
        }
    }

    @Override
    public ExchangeRateDto setExchangeRate(ExchangeRateDto exchangeRate)  throws OperdayIsNotOpenException {
        if (operdayService.isOpen()) {
            //  Сохраняем в БД новые курсы покупки/продажи по зданной валюте
            ExchangeRateEntity exchangeRateEntity = exchangeRateService.add(exchangeRate.getCcy(), exchangeRate.getBuy(), exchangeRate.getSale());
            //  Возвращам то, что было сохранено
            return new ExchangeRateDto(exchangeRateEntity.getCcy(), exchangeRateEntity.getBuy(), exchangeRateEntity.getSale());
        } else {
            throw new OperdayIsNotOpenException();
        }
    }

    @Override
    public ResponseDto requestBankBuy(RequestDto request) throws OperdayIsNotOpenException {
        if (operdayService.isOpen()) {
            //  Формируем заявку в БД
            RequestEntity requestEntity = requestService.bankBuy(request.getCcy(),
                    request.getCcySum(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getTel());

            //  Формируем ответ на заявку
            ResponseDto response = new ResponseDto();
            response.setId(requestEntity.getId());
            response.setType(requestEntity.getType());
            response.setCcy(requestEntity.getCcy());
            response.setCcySum(requestEntity.getCcySum());
            response.setUshSum(requestEntity.getUahSum());
            response.setRate(requestEntity.getRate());
            response.setTel(requestEntity.getTel());
            response.setOtp(requestEntity.getOtp());
            response.setStatus(requestEntity.getStatus());
            response.setTs(requestEntity.getTs());

            return response;
        } else {
            throw new OperdayIsNotOpenException();
        }
    }

    @Override
    public ResponseDto requestBankSale(RequestDto request) throws OperdayIsNotOpenException {
        if (operdayService.isOpen()) {
            //  Формируем заявку в БД
            RequestEntity requestEntity = requestService.bankSale(request.getCcy(),
                    request.getCcySum(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getTel());

            //  Формируем ответ на заявку
            ResponseDto response = new ResponseDto();
            response.setId(requestEntity.getId());
            response.setType(requestEntity.getType());
            response.setCcy(requestEntity.getCcy());
            response.setCcySum(requestEntity.getCcySum());
            response.setUshSum(requestEntity.getUahSum());
            response.setRate(requestEntity.getRate());
            response.setTel(requestEntity.getTel());
            response.setOtp(requestEntity.getOtp());
            response.setStatus(requestEntity.getStatus());
            response.setTs(requestEntity.getTs());

            return response;
        } else {
            throw new OperdayIsNotOpenException();
        }
    }

    @Override
    public ConfirmResponseDto confirm(ConfirmRequestDto confirmRequestDto) throws OperdayIsNotOpenException{
        if (operdayService.isOpen()) {
            //  Проводим подтверждение заявки
            List<RequestEntity> list = requestService.confirm(confirmRequestDto.getTel(), confirmRequestDto.getOtp());
            //  Формируется ответ подтверждения...
            ConfirmResponseDto confirmResponseDto = new ConfirmResponseDto();
            //  ...Количество обработанных заявок(в 99% случае - одна)
            confirmResponseDto.setRequestQuantityProcessed(list.size());
            //  ...Корректность пароля предоставлнного пользователем
            if (list.size() > 0) {
                if (list.get(0).getStatus() == OperationStatus.DONE) confirmResponseDto.setOtpCorrect(true);
                    else confirmResponseDto.setOtpCorrect(false);
            }
            return confirmResponseDto;
        } else {
            throw new OperdayIsNotOpenException();
        }
    }

    @Override
    public List<ResponseDto> getNewRequestByTel(String tel) {
        List<RequestEntity> requstList =  requestService.getNewRequestByTel(tel);
        List<ResponseDto> responseList = new ArrayList<>();
        if (requstList.size() > 0) {
            requstList.forEach( e -> {
                ResponseDto response = new ResponseDto();
                response.setId(e.getId());
                response.setType(e.getType());
                response.setCcy(e.getCcy());
                response.setCcySum(e.getCcySum());
                response.setUshSum(e.getUahSum());
                response.setRate(e.getRate());
                response.setTel(e.getTel());
                responseList.add(response);
            });
        }
        return responseList;
    }

    @Override
    public ResponseDto delete(Integer id) throws OperdayIsNotOpenException, EntityNotFoundException {
        if (operdayService.isOpen()) {
            Optional<RequestEntity> request = requestService.delete(id);
            if (request.isEmpty() == false) {
                ResponseDto response = new ResponseDto();
                response.setId(request.get().getId());
                response.setType(request.get().getType());
                response.setCcy(request.get().getCcy());
                response.setCcySum(request.get().getCcySum());
                response.setUshSum(request.get().getUahSum());
                response.setRate(request.get().getRate());
                response.setTel(request.get().getTel());
                return response;
            } else {
                throw new EntityNotFoundException();
            }
        } else {
            throw new OperdayIsNotOpenException();
        }
    }

    @Override
    public List<ResponseDto> makeDateRangeRequestReport(String ccy, Date beginDate, Date endDate) {
        List<RequestEntity> requestList = requestService.makeDateRangeRequestReport(ccy, beginDate, endDate);
        List<ResponseDto> responseList = new ArrayList<>();
        requestList.forEach(e -> {
            ResponseDto response = new ResponseDto();
            response.setId(e.getId());
            response.setType(e.getType());
            response.setCcy(e.getCcy());
            response.setCcySum(e.getCcySum());
            response.setUshSum(e.getUahSum());
            response.setRate(e.getRate());
            response.setTel(e.getTel());
            response.setStatus(e.getStatus());
            response.setTs(e.getTs());
            responseList.add(response);
        });
        return responseList;
    }

    /**
     * Сформировать итоговый отчет
     * @return Список объектов описывающих результаты агрегированного запроса к таблице заявок.
     */
    public List<FinalReportDto> makeFinalReport() {
        List<FinalReport> requst = requestService.makeFinalReport();
        List<FinalReportDto> response = new ArrayList<>();
        requst.forEach( e -> {
            FinalReportDto finalReportDto = new FinalReportDto();
            finalReportDto.setCcy(e.getCcy());
            finalReportDto.setType(e.getType());
            finalReportDto.setCount(e.getCount());
            finalReportDto.setCcySum(e.getCcySum());
            finalReportDto.setUahSum(e.getUahSum());
            response.add(finalReportDto);
        });
        return response;
    }

    /**
     * Получить курсы валют из сервиса Приватбанка
     * @return json с курсами валют
     * @throws URISyntaxException http-клиент не смог обработать адрес конечной точки
     */
    public String getExchangeRateFromPrivat() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(new URI("https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5"), String.class);
        return responseEntity.getBody();
    }
}
