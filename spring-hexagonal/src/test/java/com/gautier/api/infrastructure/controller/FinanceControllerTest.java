package com.gautier.api.infrastructure.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gautier.api.application.domain.exception.QueryNotFoundException;
import com.gautier.api.application.domain.model.Quote;
import com.gautier.api.application.domain.model.QuoteType;
import com.gautier.api.application.domain.model.SearchRequest;
import com.gautier.api.application.port.FinanceService;
import com.gautier.api.infrastructure.adapter.FinanceAdapter;
import com.gautier.api.infrastructure.configuration.SecurityConfig;
import com.gautier.api.infrastructure.dto.QuoteDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinanceController.class)
@Import({FinanceAdapter.class, SecurityConfig.class})
public class FinanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FinanceService financeService;

    @Test
    @WithMockUser(roles = "READ")
    void should_search_quotes() throws Exception {
        // arrange
        SearchRequest searchRequest = new SearchRequest("apple", Optional.of(QuoteType.EQUITY));

        Mockito.when(financeService.search(searchRequest)).thenReturn(
                List.of(new Quote("NMS", "Apple Inc.", "EQUITY", "AAPL"),
                        new Quote("NYQ", "Apple Hospitality REIT, Inc.", "EQUITY", "APLE")));

        QuoteDto expected = new QuoteDto(List.of(
                new QuoteDto.QuoteResult("NMS", "Apple Inc.", "EQUITY", "AAPL"),
                new QuoteDto.QuoteResult("NYQ", "Apple Hospitality REIT, Inc.", "EQUITY", "APLE")));

        String expectedJson = objectMapper.writeValueAsString(expected);

        // act-assert
        mockMvc.perform(get("/finance/search")
                        .param("search", "apple")
                        .param("type", "EQUITY"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(financeService, Mockito.times(1)).search(searchRequest);
    }

    @Test
    @WithMockUser(roles = "READ")
    void should_search_quotes_without_type() throws Exception {
        // arrange
        SearchRequest searchRequest = new SearchRequest("apple", Optional.empty());

        Mockito.when(financeService.search(searchRequest)).thenReturn(
                List.of(new Quote("NMS", "Apple Inc.", "EQUITY", "AAPL"),
                        new Quote("NYQ", "Apple Hospitality REIT, Inc.", "EQUITY", "APLE")));

        QuoteDto expected = new QuoteDto(List.of(
                new QuoteDto.QuoteResult("NMS", "Apple Inc.", "EQUITY", "AAPL"),
                new QuoteDto.QuoteResult("NYQ", "Apple Hospitality REIT, Inc.", "EQUITY", "APLE")));

        String expectedJson = objectMapper.writeValueAsString(expected);

        // act-assert
        mockMvc.perform(get("/finance/search")
                        .param("search", "apple"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        Mockito.verify(financeService, Mockito.times(1)).search(searchRequest);
    }

    @Test
    @WithMockUser(roles = "READ")
    void should_failed_when_search_quotes_with_empty_query() throws Exception {
        mockMvc.perform(get("/finance/search")
                        .param("search", "")
                        .param("type", "EQUITY"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "READ")
    void should_failed_when_query_not_found() throws Exception {
        SearchRequest searchRequest = new SearchRequest("ssss", Optional.empty());

        Mockito.when(financeService.search(searchRequest)).thenThrow(QueryNotFoundException.class);

        mockMvc.perform(get("/finance/search")
                        .param("search", "ssss"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "WRITE")
    void should_failed_roles_forbidden() throws Exception {
        mockMvc.perform(get("/finance/search")
                        .param("search", "ssss"))
                .andExpect(status().isForbidden());
    }
}