package com.apidemo.dataquerypatterns;

import com.apidemo.dataquerypatterns.account.AccountEventRepository;
import com.apidemo.dataquerypatterns.inbox.InboxMessageRepository;
import com.apidemo.dataquerypatterns.outbox.OutboxMessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DataQueryPatternsIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired OutboxMessageRepository outbox;
    @Autowired InboxMessageRepository inbox;
    @Autowired AccountEventRepository accountEvents;
    @Autowired CacheManager cacheManager;

    @Test
    void demonstratesSagaOutboxInboxMaterializedViewEventSourcingAndCacheAside() throws Exception {
        var createOrder = mvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":\"customer-7\",\"totalAmount\":120.00,\"currency\":\"USD\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.sagaStatus").value("STARTED"))
                .andReturn();

        UUID orderId = UUID.fromString(json(createOrder.getResponse().getContentAsString()).get("id").asText());
        assertThat(outbox.countByStatus("PENDING")).isEqualTo(1);

        mvc.perform(patch("/orders/{id}/advance", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.sagaStatus").value("COMPLETED"));

        assertThat(outbox.countByStatus("PENDING")).isEqualTo(4);

        mvc.perform(post("/internal/outbox/publish"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.published").value(4));

        assertThat(inbox.count()).isEqualTo(4);

        mvc.perform(get("/summaries/customers/customer-7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.confirmedOrderCount").value(1))
                .andExpect(jsonPath("$.confirmedTotal").value("120.00"));

        UUID firstMessageId = outbox.findAll().getFirst().id;
        mvc.perform(post("/internal/outbox/replay/{messageId}", firstMessageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consumed").value(false));

        mvc.perform(get("/summaries/customers/customer-7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.confirmedOrderCount").value(1));

        var account = mvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"openingBalance\":100.00}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.balance").value("100.00"))
                .andExpect(jsonPath("$.version").value(1))
                .andReturn();

        UUID accountId = UUID.fromString(json(account.getResponse().getContentAsString()).get("accountId").asText());

        mvc.perform(post("/accounts/{id}/deposits", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":25.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("125.00"))
                .andExpect(jsonPath("$.version").value(2));

        mvc.perform(post("/accounts/{id}/withdrawals", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":40.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("85.00"))
                .andExpect(jsonPath("$.version").value(3));

        assertThat(accountEvents.findByAccountIdOrderBySequenceNumberAsc(accountId)).hasSize(3);

        mvc.perform(get("/products/BOOK-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value("42.00"));

        assertThat(cacheManager.getCache("products").get("BOOK-1")).isNotNull();

        mvc.perform(patch("/products/BOOK-1/price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"price\":45.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value("45.00"));

        assertThat(cacheManager.getCache("products").get("BOOK-1")).isNull();

        mvc.perform(get("/products/BOOK-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value("45.00"));
    }

    private JsonNode json(String body) throws Exception {
        return objectMapper.readTree(body);
    }
}
