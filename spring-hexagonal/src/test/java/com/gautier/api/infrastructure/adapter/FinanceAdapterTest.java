package com.gautier.api.infrastructure.adapter;


import com.gautier.api.application.domain.model.QuoteType;
import com.gautier.api.application.domain.model.SearchRequest;
import com.gautier.api.infrastructure.dto.QuoteTypeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class FinanceAdapterTest {

    private final FinanceAdapter financeAdapter = new FinanceAdapter();

    @Test
    void should_map_to_domain_search_request_with_all_type() {
        Assertions.assertTrue(Arrays
                .stream(QuoteTypeDTO.values())
                .allMatch(typeDto -> financeAdapter.toDomain("", typeDto).equals(new SearchRequest("", match(typeDto)))
                ));
    }

    private Optional<QuoteType> match(QuoteTypeDTO typeDto) {
        List<QuoteType> types = Arrays.stream(QuoteType.values()).toList();
        return types.stream().filter(type -> type.name().equalsIgnoreCase(typeDto.name())).findFirst();
    }
}