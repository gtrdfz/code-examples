package com.gautier.api.application.domain.service;

import com.gautier.api.application.domain.exception.QueryNotFoundException;
import com.gautier.api.application.domain.model.Quote;
import com.gautier.api.application.domain.model.QuoteType;
import com.gautier.api.application.domain.model.SearchRequest;
import com.gautier.api.application.port.FinanceRepository;
import com.gautier.api.application.port.FinanceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class FinanceServiceImpl implements FinanceService {

    private final FinanceRepository financeRepository;

    public FinanceServiceImpl(FinanceRepository financeRepository) {
        this.financeRepository = financeRepository;
    }

    @Override
    public List<Quote> search(SearchRequest searchRequest) throws QueryNotFoundException {
        return financeRepository
                .find(searchRequest.query())
                .stream()
                .filter(quote -> filterType(quote.quoteType(), searchRequest.type()))
                .toList();
    }

    private boolean filterType(String quote, Optional<QuoteType> quoteType) {
        return quoteType.map(type -> type.name().equalsIgnoreCase(quote)).orElse(true);
    }
}