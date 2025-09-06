package com.gautier.api.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gautier.api.application.domain.exception.QueryNotFoundException;
import com.gautier.api.application.domain.model.Quote;
import okhttp3.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class YahooFinanceAPITest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    OkHttpClient okHttpClient;
    @InjectMocks
    YahooFinanceAPI yahooFinanceAPI;

    @Test
    void should_retrieve_information_from_yahoo_api() throws QueryNotFoundException, IOException {
        // arrange
        Call callMock = Mockito.mock(Call.class);

        YahooFinanceAPI.YahooSearchResponse expected = new YahooFinanceAPI.YahooSearchResponse(List.of(
                new YahooFinanceAPI.YahooQuote("NMS", "Apple", "EQUITY", "AAPL")));

        String expectedJson = objectMapper.writeValueAsString(expected);

        ResponseBody body = ResponseBody.create(expectedJson, null);
        Response response = new Response.Builder()
                .request(new Request.Builder().url(getUrl("apple")).build())
                .protocol(okhttp3.Protocol.HTTP_1_1)
                .code(200)
                .body(body)
                .message("OK")
                .build();

        Mockito.when(okHttpClient.newCall(Mockito.any())).thenReturn(callMock);
        Mockito.when(callMock.execute()).thenReturn(response);

        // act
        List<Quote> result = yahooFinanceAPI.find("apple");

        // assert
        Assertions.assertThat(result).containsExactly(
                new Quote("NMS", "Apple", "EQUITY", "AAPL")
        );
    }

    @Test
    void should_throw_exception_when_retrieve_information_from_yahoo_api_failed() throws QueryNotFoundException, IOException {
        // arrange
        Call callMock = Mockito.mock(Call.class);
        Mockito.when(okHttpClient.newCall(Mockito.any())).thenReturn(callMock);
        Mockito.when(callMock.execute()).thenThrow(new IOException());

        // act - assert
        Assertions.assertThatThrownBy(() -> yahooFinanceAPI.find("apple"))
                .isInstanceOf(QueryNotFoundException.class)
                .hasMessageContaining("Unable to retrieve information from Yahoo Finance.");
    }

    @Test
    void should_throw_exception_when_retrieve_information_from_yahoo_api_return_empty_body() throws QueryNotFoundException, IOException {
        // arrange
        Call callMock = Mockito.mock(Call.class);
        Mockito.when(okHttpClient.newCall(Mockito.any())).thenReturn(callMock);
        Mockito.when(callMock.execute()).thenThrow(new NullPointerException());

        // act - assert
        Assertions.assertThatThrownBy(() -> yahooFinanceAPI.find("apple"))
                .isInstanceOf(QueryNotFoundException.class)
                .hasMessageContaining("Unable to parse yahoo finance api response.");
    }


    private String getUrl(String query) {
        return "https://query1.finance.yahoo.com/v1/finance/search?quotesCount=100" +
                "&q=" + query + "&lang=en-US&region=US&newsCount=0&listsCount=0&enableFuzzyQuery=false" +
                "&quotesQueryId=tss_match_phrase_query&multiQuoteQueryId=multi_quote_single_token_query" +
                "&newsQueryId=news_cie_vespa&enableCb=false&enableNavLinks=false&enableEnhancedTrivialQuery=true" +
                "&enableResearchReports=false&enableCulturalAssets=true&enableLogoUrl=true&enableLists=false" +
                "&recommendCount=5&enablePrivateCompany=true";
    }
}