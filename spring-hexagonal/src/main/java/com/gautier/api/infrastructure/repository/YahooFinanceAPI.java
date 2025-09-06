package com.gautier.api.infrastructure.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gautier.api.application.domain.exception.QueryNotFoundException;
import com.gautier.api.application.domain.model.Quote;
import com.gautier.api.application.port.FinanceRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;


@Repository
@Profile({"dev", "prod"})
public class YahooFinanceAPI implements FinanceRepository {

    private final OkHttpClient client;

    private final ObjectMapper mapper = new ObjectMapper();

    public YahooFinanceAPI(OkHttpClient client) {
        this.client = client;
    }

    public List<Quote> find(String query) throws QueryNotFoundException {
        String urlSearchYahooApi = "https://query1.finance.yahoo.com/v1/finance/search?quotesCount=100" +
                "&q=" + query + "&lang=en-US&region=US&newsCount=0&listsCount=0&enableFuzzyQuery=false" +
                "&quotesQueryId=tss_match_phrase_query&multiQuoteQueryId=multi_quote_single_token_query" +
                "&newsQueryId=news_cie_vespa&enableCb=false&enableNavLinks=false&enableEnhancedTrivialQuery=true" +
                "&enableResearchReports=false&enableCulturalAssets=true&enableLogoUrl=true&enableLists=false" +
                "&recommendCount=5&enablePrivateCompany=true";

        Request request = new Request.Builder()
                .url(urlSearchYahooApi)
                .get()
                .build();
        YahooSearchResponse yahooSearchResponse = new YahooSearchResponse(List.of());
        try {
            Response response = this.client.newCall(request).execute();
            String jsonString = response.body().string();
            yahooSearchResponse = mapper.readValue(jsonString, YahooSearchResponse.class);
        } catch (IOException e) {
            throw new QueryNotFoundException("Unable to retrieve information from Yahoo Finance.");
        } catch (NullPointerException e) {
            throw new QueryNotFoundException("Unable to parse yahoo finance api response.");
        }

        return yahooSearchResponse.quotes.stream()
                .map(yahooQuote -> new Quote(yahooQuote.exchange, yahooQuote.shortname, yahooQuote.quoteType, yahooQuote.symbol))
                .toList();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record YahooSearchResponse(List<YahooQuote> quotes) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record YahooQuote(String exchange, String shortname, String quoteType, String symbol) {
    }

}
