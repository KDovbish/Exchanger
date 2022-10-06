package example;

import org.example.ExchangerImpl;
import org.example.WebController;
import org.example.exception.OperdayIsAlreadyOpenException;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@WebMvcTest(WebController.class)
public class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangerImpl exchanger;

    @Test
    public void openOperday() throws Exception {
        mockMvc.perform(put("/open"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", Matchers.is("ok")));

        Mockito.doThrow(new OperdayIsAlreadyOpenException()).when(exchanger).open();
        mockMvc.perform(put("/open"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", Matchers.is("OperdayIsAlreadyOpenException")));
    }
}