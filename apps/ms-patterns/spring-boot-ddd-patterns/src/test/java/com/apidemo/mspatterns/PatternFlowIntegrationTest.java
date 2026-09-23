package com.apidemo.mspatterns;

import com.apidemo.mspatterns.infrastructure.observability.CorrelationId;
import com.apidemo.mspatterns.infrastructure.persistence.AuditLogJpaRepository;
import com.apidemo.mspatterns.infrastructure.persistence.InboxMessageJpaRepository;
import com.apidemo.mspatterns.infrastructure.persistence.OutboxEventJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PatternFlowIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired OutboxEventJpaRepository outbox;
    @Autowired InboxMessageJpaRepository inbox;
    @Autowired AuditLogJpaRepository audit;

    @Test
    void demonstratesOutboxInboxCorrelationHealthAndAudit() throws Exception {
        var correlationId = "demo-correlation-123";
        var createBody = """
                {
                  "customerId": "customer-7",
                  "actor": "api-user",
                  "items": [
                    {"sku": "BOOK-1", "quantity": 2, "unitPrice": 15.50, "currency": "USD"}
                  ]
                }
                """;

        var createResult = mvc.perform(post("/orders")
                        .header(CorrelationId.HEADER, correlationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andExpect(header().string(CorrelationId.HEADER, correlationId))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andReturn();

        UUID orderId = UUID.fromString(json(createResult.getResponse().getContentAsString()).get("id").asText());
        assertThat(outbox.countByStatus("PENDING")).isEqualTo(1);
        assertThat(audit.findByTargetTypeAndTargetIdOrderByCreatedAtAsc("Order", orderId.toString()))
                .extracting(entry -> entry.action)
                .contains("ORDER_CREATED");

        mvc.perform(post("/internal/outbox/publish").header(CorrelationId.HEADER, correlationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.published").value(1));

        assertThat(outbox.countByStatus("PUBLISHED")).isEqualTo(1);
        assertThat(inbox.count()).isEqualTo(1);
        assertThat(audit.findByTargetTypeAndTargetIdOrderByCreatedAtAsc("Order", orderId.toString()))
                .extracting(entry -> entry.action)
                .contains("MESSAGE_CONSUMED");

        mvc.perform(patch("/orders/{id}/status", orderId)
                        .header(CorrelationId.HEADER, correlationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"PAID\", \"actor\": \"api-user\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));

        mvc.perform(post("/internal/outbox/publish").header(CorrelationId.HEADER, correlationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.published").value(1));

        mvc.perform(get("/audit-logs").param("orderId", orderId.toString()).header(CorrelationId.HEADER, correlationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].correlationId").value(correlationId));

        mvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.outboxBacklog.status").value("UP"));
    }

    private JsonNode json(String body) throws Exception {
        return objectMapper.readTree(body);
    }
}
