package com.apidemo.dataquerypatterns.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class AccountEventSourcingService {
    private final AccountEventRepository events;

    public AccountEventSourcingService(AccountEventRepository events) {
        this.events = events;
    }

    @Transactional
    public AccountView open(BigDecimal openingBalance) {
        var accountId = UUID.randomUUID();
        append(accountId, "AccountOpened", openingBalance);
        return get(accountId);
    }

    @Transactional
    public AccountView deposit(UUID accountId, BigDecimal amount) {
        append(accountId, "MoneyDeposited", amount);
        return get(accountId);
    }

    @Transactional
    public AccountView withdraw(UUID accountId, BigDecimal amount) {
        var current = get(accountId);
        if (current.balance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        append(accountId, "MoneyWithdrawn", amount);
        return get(accountId);
    }

    @Transactional(readOnly = true)
    public AccountView get(UUID accountId) {
        var stream = events.findByAccountIdOrderBySequenceNumberAsc(accountId);
        if (stream.isEmpty()) {
            throw new IllegalArgumentException("Account event stream not found");
        }
        var balance = BigDecimal.ZERO;
        for (var event : stream) {
            balance = switch (event.eventType) {
                case "AccountOpened", "MoneyDeposited" -> balance.add(event.amount);
                case "MoneyWithdrawn" -> balance.subtract(event.amount);
                default -> throw new IllegalStateException("Unknown account event " + event.eventType);
            };
        }
        return new AccountView(accountId, balance, stream.size());
    }

    private void append(UUID accountId, String eventType, BigDecimal amount) {
        var event = new AccountEventEntity();
        event.id = UUID.randomUUID();
        event.accountId = accountId;
        event.sequenceNumber = events.countByAccountId(accountId) + 1;
        event.eventType = eventType;
        event.amount = amount;
        event.createdAt = Instant.now();
        events.save(event);
    }

    public record AccountView(UUID accountId, BigDecimal balance, int version) {
    }
}
