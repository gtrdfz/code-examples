package com.gautier.api.application.domain.model;

public record Quote(
        String exchange,
        String shortname,
        String quoteType,
        String symbol
) {
}
