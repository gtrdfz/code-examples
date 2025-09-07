package com.gautier.api.infrastructure.controller;

import com.gautier.api.application.domain.exception.QueryNotFoundException;
import com.gautier.api.application.domain.model.Quote;
import com.gautier.api.application.port.FinanceService;
import com.gautier.api.infrastructure.adapter.FinanceAdapter;
import com.gautier.api.infrastructure.dto.QuoteDto;
import com.gautier.api.infrastructure.dto.QuoteTypeDTO;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/finance")
public class FinanceController {

    private final FinanceService financeService;
    private final FinanceAdapter financeAdapter;

    public FinanceController(FinanceService financeService, FinanceAdapter financeAdapter) {
        this.financeService = financeService;
        this.financeAdapter = financeAdapter;
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_READ')")
    QuoteDto search(@RequestParam(required = true) @NotBlank String search,
                    @RequestParam(required = false) QuoteTypeDTO type) throws QueryNotFoundException {
        List<Quote> searchResults = this.financeService.search(financeAdapter.toDomain(search, type));
        return financeAdapter.toDto(searchResults);
    }


    @GetMapping("/whoami")
    public Object whoami(Authentication authentication) {
        return authentication.getAuthorities();
    }

}