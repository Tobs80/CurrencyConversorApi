package CurrencyConversorApi.example.crud;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TestingWebApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_conversion_with_date_data() throws Exception {
        this.mockMvc.perform(get("/conversion/20230508-EUR-USD"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string( "1.1035907"));
    }

    @Test
    void test_conversion_without_date_data() throws Exception {
        //Should return data from 2023/05/05 as there is no data from 2023/05/07
        this.mockMvc.perform(get("/conversion/20230507-EUR-USD"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string( "1.1026868"));
    }
    @Test
    void test_lower_case_currency() throws Exception {
        this.mockMvc.perform(get("/conversion/20230508-eur-usd"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string( "1.1035907"));
    }
    @Test
    void test_other_urls() throws Exception {
        this.mockMvc.perform(get("/conversion/"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("Invalid URI format. Expected /date(yyyymmdd)-Currency base-Currency_objective" ));
        this.mockMvc.perform(get("/conversion/home"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("Invalid URI format. Expected /date(yyyymmdd)-Currency base-Currency_objective" ));
    }
    @Test
    void test_url_with_extra_parameters() throws Exception {
        this.mockMvc.perform(get("/conversion/date-Currency1-Currency2-param4"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Invalid URI format. Expected /date(yyyymmdd)-Currency base-Currency_objective" ));
    }


}
