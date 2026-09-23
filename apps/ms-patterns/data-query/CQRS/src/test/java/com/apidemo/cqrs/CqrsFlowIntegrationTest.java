package com.apidemo.cqrs;

import com.apidemo.cqrs.events.DomainEventRepository;
import com.apidemo.cqrs.query.OrderReadModelRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CqrsFlowIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired DomainEventRepository events;
    @Autowired OrderReadModelRepository readModels;

    @Test
    void commandsWriteEventsAndProjectionBuildsQueryModel() throws Exception {
        var createBody = """
                {
                  "customerId": "customer-7",
                  "items": [
                    {"sku": "BOOK-1", "quantity": 2, "unitPrice": 15.50, "currency": "USD"},
                    {"sku": "PEN-1", "quantity": 3, "unitPrice": 2.00, "currency": "USD"}
                  ]
                }
                """;

        var createResult = mvc.perform(post("/commands/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andReturn();

        UUID orderId = UUID.fromString(json(createResult.getResponse().getContentAsString()).get("orderId").asText());
        assertThat(events.findAll()).hasSize(1);
        assertThat(readModels.findById(orderId)).isEmpty();

        mvc.perform(get("/queries/orders/{id}", orderId))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/internal/projections/orders/run"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projected").value(1));

        mvc.perform(get("/queries/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.itemCount").value(2))
                .andExpect(jsonPath("$.version").value(1));

        mvc.perform(patch("/commands/orders/{id}/status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"PAID\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));

        mvc.perform(get("/queries/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.version").value(1));

        mvc.perform(post("/internal/projections/orders/run"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projected").value(1));

        mvc.perform(get("/queries/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.version").value(2));
    }

    private JsonNode json(String body) throws Exception {
        return objectMapper.readTree(body);
    }
}
