package com.gautier.api.application.domain.model;

import java.util.Optional;

public record SearchRequest(
        String query,
        Optional<QuoteType> type) {
}
