package com.gautier.api.application.domain.service;

import com.gautier.api.application.domain.exception.QueryNotFoundException;
import com.gautier.api.application.domain.model.Quote;
import com.gautier.api.application.domain.model.QuoteType;
import com.gautier.api.application.domain.model.SearchRequest;
import com.gautier.api.application.port.FinanceRepository;
import com.gautier.api.application.port.FinanceService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FinanceServiceImplTest {

    @Mock
    FinanceRepository financeRepository;

    @InjectMocks
    private FinanceServiceImpl financeService;

    @Test
    void should_retrieve_quote() throws QueryNotFoundException {
        // arrange
        SearchRequest searchRequest = new SearchRequest("apple", Optional.empty());

        Mockito.when(financeRepository.find("apple")).thenReturn(
                List.of(new Quote("NMS", "Apple Inc.", "EQUITY", "AAPL"),
                        new Quote("NYQ", "Apple Hospitality REIT, Inc.", "ETF", "APLE"))
        );

        // act
        List<Quote> result = financeService.search(searchRequest);

        // assert
        Assertions.assertThat(result).containsExactly(
                new Quote("NMS", "Apple Inc.", "EQUITY", "AAPL"),
                new Quote("NYQ", "Apple Hospitality REIT, Inc.", "ETF", "APLE"));
    }

    @Test
    void should_retrieve_quote_with_filter() throws QueryNotFoundException {
        // arrange
        SearchRequest searchRequest = new SearchRequest("apple", Optional.of(QuoteType.EQUITY));

        Mockito.when(financeRepository.find("apple")).thenReturn(
                List.of(new Quote("NMS", "Apple Inc.", "EQUITY", "AAPL"),
                        new Quote("NYQ", "Apple Hospitality REIT, Inc.", "ETF", "APLE"))
        );

        // act
        List<Quote> result = financeService.search(searchRequest);

        // assert
        Assertions.assertThat(result).containsExactly(new Quote("NMS", "Apple Inc.", "EQUITY", "AAPL"));
    }
}