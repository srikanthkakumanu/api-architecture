package com.apidemo.dataquerypatterns.inbox;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@IdClass(InboxMessageId.class)
@Table(name = "inbox_messages")
public class InboxMessageEntity {
    @Id
    public UUID messageId;
    @Id
    public String consumerName;
    public String messageType;
    public String status;
    public Instant receivedAt;
}
