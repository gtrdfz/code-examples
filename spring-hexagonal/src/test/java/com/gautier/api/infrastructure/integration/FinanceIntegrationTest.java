package com.gautier.api.infrastructure.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gautier.api.application.domain.model.Quote;
import com.gautier.api.application.port.FinanceRepository;
import com.gautier.api.infrastructure.dto.QuoteDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FinanceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FinanceRepository financeRepository;

    @Test
    void should_search_quotes() throws Exception {
        // arrange
        Mockito.when(financeRepository.find("apple equity")).thenReturn(List.of(
                new Quote("NYQ", "Apple Hospitality REIT, Inc.", "ETF", "APLE"),
                new Quote("NMS", "Apple", "EQUITY", "AAPL")));

        QuoteDto expected = new QuoteDto(List.of(
                new QuoteDto.QuoteResult("NMS", "Apple", "EQUITY", "AAPL")));
        String expectedJson = objectMapper.writeValueAsString(expected);

        // act-assert
        mockMvc.perform(get("/finance/search")
                        .param("search", "apple equity")
                        .param("type", "EQUITY"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @TestConfiguration
    static class MockRepositoryConfig {
        @Bean
        @Primary
        FinanceRepository financeRepository() {
            return Mockito.mock(FinanceRepository.class);
        }
    }
}

