package com.gautier.api.infrastructure.dto;

import java.util.List;

public record QuoteDto(
        List<QuoteResult> data) {

    public record QuoteResult(
            String exchange,
            String name,
            String type,
            String symbol) {
    }
}