package com.apidemo.dataquerypatterns.web;

import com.apidemo.dataquerypatterns.account.AccountEventSourcingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountEventSourcingService accounts;

    public AccountController(AccountEventSourcingService accounts) {
        this.accounts = accounts;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestDtos.AccountResponse open(@Valid @RequestBody RestDtos.OpenAccountRequest request) {
        return RestDtos.AccountResponse.from(accounts.open(request.openingBalance()));
    }

    @PostMapping("/{id}/deposits")
    public RestDtos.AccountResponse deposit(@PathVariable UUID id, @Valid @RequestBody RestDtos.MoneyRequest request) {
        return RestDtos.AccountResponse.from(accounts.deposit(id, request.amount()));
    }

    @PostMapping("/{id}/withdrawals")
    public RestDtos.AccountResponse withdraw(@PathVariable UUID id, @Valid @RequestBody RestDtos.MoneyRequest request) {
        return RestDtos.AccountResponse.from(accounts.withdraw(id, request.amount()));
    }

    @GetMapping("/{id}")
    public RestDtos.AccountResponse get(@PathVariable UUID id) {
        return RestDtos.AccountResponse.from(accounts.get(id));
    }
}
