package com.gautier.api.infrastructure.integration.mockimplementation;

import com.gautier.api.application.domain.exception.QueryNotFoundException;
import com.gautier.api.application.domain.model.Quote;
import com.gautier.api.application.port.FinanceRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("test")
public class YahooRepositoryTest implements FinanceRepository {

    @Override
    public List<Quote> find(String query) throws QueryNotFoundException {
        return List.of(
                new Quote("PAR", "Apple", "ETF", "APL"),
                new Quote("USA", "Mouse", "EQUITY", "MSE"));
    }
}
