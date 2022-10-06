package org.example.service;


import org.example.entity.OperdayEntity;
import org.example.repositary.OperdayRepositary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
public class OperdayServiceImplTest {

    @TestConfiguration
    static class OperdayServiceTestConfiguration {
        @Bean
        public OperdayService operdayService() {
            return new OperdayServiceImpl();
        }
    }

    @MockBean
    private OperdayRepositary operdayRepositary;

    @Autowired
    private OperdayService operdayService;

    @Test
    public void open() {
        //  Состояние записи в таблице operday перед открытием опердня
        Mockito.when(operdayRepositary.getOne(1)).thenReturn(new OperdayEntity(Mockito.anyInt(), false, false));
        assertTrue(operdayService.open());
    }

    @Test
    public void close() {
        //  Состояние записи в таблице operday перед закрытием опердня
        Mockito.when(operdayRepositary.getOne(1)).thenReturn(new OperdayEntity(Mockito.anyInt(), true, false));
        assertTrue(operdayService.close());
    }


}



