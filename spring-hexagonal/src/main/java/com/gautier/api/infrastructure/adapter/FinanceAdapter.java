package com.gautier.api.infrastructure.adapter;

import com.gautier.api.application.domain.model.Quote;
import com.gautier.api.application.domain.model.QuoteType;
import com.gautier.api.application.domain.model.SearchRequest;
import com.gautier.api.infrastructure.dto.QuoteDto;
import com.gautier.api.infrastructure.dto.QuoteTypeDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FinanceAdapter {

    public QuoteDto toDto(List<Quote> quotes) {
        return new QuoteDto(quotes
                .stream()
                .map(quote -> new QuoteDto.QuoteResult(quote.exchange(), quote.shortname(), quote.quoteType(), quote.symbol()))
                .toList());
    }

    public SearchRequest toDomain(String search, QuoteTypeDTO type) {
        return new SearchRequest(search, map(type));
    }

    private Optional<QuoteType> map(QuoteTypeDTO type) {
        return switch (type) {
            case ETF -> Optional.of(QuoteType.ETF);
            case EQUITY -> Optional.of(QuoteType.EQUITY);
            case null -> Optional.empty();
        };
    }
}
