package com.apidemo.dataquerypatterns.inbox;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class InboxService {
    private final InboxMessageRepository inbox;

    public InboxService(InboxMessageRepository inbox) {
        this.inbox = inbox;
    }

    public boolean recordIfFirst(UUID messageId, String consumerName, String messageType) {
        if (inbox.existsByMessageIdAndConsumerName(messageId, consumerName)) {
            return false;
        }
        var message = new InboxMessageEntity();
        message.messageId = messageId;
        message.consumerName = consumerName;
        message.messageType = messageType;
        message.status = "CONSUMED";
        message.receivedAt = Instant.now();
        inbox.save(message);
        return true;
    }
}
