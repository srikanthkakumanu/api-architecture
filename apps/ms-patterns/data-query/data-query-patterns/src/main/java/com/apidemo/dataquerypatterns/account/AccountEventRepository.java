package com.apidemo.dataquerypatterns.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountEventRepository extends JpaRepository<AccountEventEntity, UUID> {
    List<AccountEventEntity> findByAccountIdOrderBySequenceNumberAsc(UUID accountId);

    long countByAccountId(UUID accountId);
}
