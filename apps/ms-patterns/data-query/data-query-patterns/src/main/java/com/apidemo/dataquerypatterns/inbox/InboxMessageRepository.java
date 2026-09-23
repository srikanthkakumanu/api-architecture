package com.apidemo.dataquerypatterns.inbox;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxMessageRepository extends JpaRepository<InboxMessageEntity, InboxMessageId> {
    boolean existsByMessageIdAndConsumerName(java.util.UUID messageId, String consumerName);
}
