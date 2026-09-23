package com.apidemo.dataquerypatterns.inbox;

import java.io.Serializable;
import java.util.UUID;

public class InboxMessageId implements Serializable {
    public UUID messageId;
    public String consumerName;
}
