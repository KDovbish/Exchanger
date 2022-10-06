package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exception.OperdayIsAlreadyOpenException;
import org.example.exception.PrivatExchangeRateException;
import org.example.service.ExchangeRateServiceImpl;
import org.example.service.OperdayServiceImpl;
import org.example.service.RequestServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
public class ExchangerImplTest {

    @TestConfiguration
    static class ExchangerConfiguration {
        @Bean
        ExchangerImpl getExchanger() {
            return new ExchangerImpl();
        }
    }

    @MockBean
    OperdayServiceImpl operdayService;
    @MockBean
    ExchangeRateServiceImpl exchangeRateService;
    @MockBean
    RequestServiceImpl requestService;

    @Autowired
    ExchangerImpl exchanger;

    @Test
    public void getExchangeRateFromPrivat() throws URISyntaxException, JsonProcessingException {
        String body = exchanger.getExchangeRateFromPrivat();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonTree = mapper.readTree(body);
        assertTrue(jsonTree.findValues("ccy").stream().anyMatch(e -> e.asText().equals("USD")));
        assertTrue(jsonTree.findValues("ccy").stream().anyMatch(e -> e.asText().equals("EUR")));
    }

    @Test
    public void open() throws OperdayIsAlreadyOpenException, PrivatExchangeRateException {
        Mockito.when(operdayService.isOpen()).thenReturn(true);
        assertThrows(OperdayIsAlreadyOpenException.class, exchanger::open);
    }

}