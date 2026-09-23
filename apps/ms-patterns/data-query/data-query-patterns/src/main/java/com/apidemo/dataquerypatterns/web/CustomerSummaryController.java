package com.apidemo.dataquerypatterns.web;

import com.apidemo.dataquerypatterns.materializedview.CustomerOrderSummaryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/summaries/customers")
public class CustomerSummaryController {
    private final CustomerOrderSummaryRepository summaries;

    public CustomerSummaryController(CustomerOrderSummaryRepository summaries) {
        this.summaries = summaries;
    }

    @GetMapping("/{customerId}")
    public RestDtos.CustomerSummaryResponse get(@PathVariable String customerId) {
        return RestDtos.CustomerSummaryResponse.from(summaries.findById(customerId).orElseThrow(() -> new IllegalArgumentException("Customer summary not found")));
    }
}
