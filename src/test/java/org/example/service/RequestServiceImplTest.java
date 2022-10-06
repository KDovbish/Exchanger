package org.example.service;

import org.example.entity.ExchangeRateEntity;
import org.example.entity.RequestEntity;
import org.example.enumtype.OperationStatus;
import org.example.repositary.RequestRepositary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class RequestServiceImplTest {

    @TestConfiguration
    static class RequestServiceConfiguration {
        @Bean
        RequestService getRequestService() {
            return new RequestServiceImpl();
        }
    }

    @MockBean
    private ExchangeRateServiceImpl exchangeRateService;
    @MockBean
    private RequestRepositary requestRepositary;

    @Autowired
    private RequestService requestService;

    @Test
    public void bankBuy() {
        //  Имитация получаения курса покупки
        ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity();
        exchangeRateEntity.setBuy(BigDecimal.valueOf(40.005));
        Mockito.when(exchangeRateService.getLast(Mockito.anyString())).thenReturn(exchangeRateEntity);
        //  Покупка
        RequestEntity request = requestService.bankBuy("USD", BigDecimal.valueOf(100.00d), "Ivan", "Ivanov", "0501111111");
        assertEquals(4000.50d, request.getUahSum().doubleValue());
        assertEquals(OperationStatus.NEW, request.getStatus());
    }

    @Test
    public void confirm() {
        //  Имитация того, что отдаст таблица request в методе confirm()
        RequestEntity request = new RequestEntity();
        request.setTel("0501111111");
        request.setOtp("1234");
        request.setStatus(OperationStatus.NEW);
        List<RequestEntity> requestEntityList = List.of(request);
        Mockito.when(requestRepositary.findByTelAndStatus(Mockito.anyString(), Mockito.eq(OperationStatus.NEW))).thenReturn(requestEntityList);

        List<RequestEntity> confirmList;
        confirmList = requestService.confirm("0501111111", "1234");
        assertEquals(OperationStatus.DONE, confirmList.get(0).getStatus());
        confirmList = requestService.confirm("0501111111", "0000");
        assertEquals(OperationStatus.CANCELLED, confirmList.get(0).getStatus());
    }

    @Test
    public void generateOneTimePassword() {
        RequestServiceImpl requestService = new RequestServiceImpl();
        for (int i = 1; i <= 50; i++) {
            System.out.print(requestService.generateOneTimePassword() + " ");
        }
        System.out.println();
    }

}