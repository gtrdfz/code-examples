package com.gautier.api.application.port;

import com.gautier.api.application.domain.exception.QueryNotFoundException;
import com.gautier.api.application.domain.model.Quote;

import java.util.List;

public interface FinanceRepository {

    List<Quote> find(String query) throws QueryNotFoundException;
}
