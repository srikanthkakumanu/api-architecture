package com.apidemo.dataquerypatterns.materializedview;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderSummaryRepository extends JpaRepository<CustomerOrderSummaryEntity, String> {
}
