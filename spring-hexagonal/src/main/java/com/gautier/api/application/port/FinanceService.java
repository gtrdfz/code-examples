package com.gautier.api.application.port;

import com.gautier.api.application.domain.exception.QueryNotFoundException;
import com.gautier.api.application.domain.model.Quote;
import com.gautier.api.application.domain.model.SearchRequest;

import java.util.List;

public interface FinanceService {

    List<Quote> search(SearchRequest searchRequest) throws QueryNotFoundException;
}
