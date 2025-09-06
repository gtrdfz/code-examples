package com.gautier.api.infrastructure.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gautier.api.application.port.FinanceRepository;
import com.gautier.api.infrastructure.dto.QuoteDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FinanceIntegrationHexagonalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FinanceRepository financeRepository;

    @Test
    void should_search_quotes() throws Exception {
        // arrange
        QuoteDto expected = new QuoteDto(List.of(
                new QuoteDto.QuoteResult("PAR", "Apple", "ETF", "APL")));
        String expectedJson = objectMapper.writeValueAsString(expected);

        // act-assert
        mockMvc.perform(get("/finance/search")
                        .param("search", "apple equity")
                        .param("type", "ETF"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

}

