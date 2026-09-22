package com.example.ecommerce.shipping.infrastructure;

import com.example.ecommerce.shipping.domain.Shipment;
import com.example.ecommerce.shipping.domain.ShipmentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter: implements the domain-owned {@link ShipmentRepository} port using
 * Spring Data JPA, translating between the pure {@link Shipment} aggregate and
 * its {@link ShipmentJpaEntity} persistence shadow.
 */
@Repository
public class JpaShipmentRepository implements ShipmentRepository {

    private final ShipmentSpringDataRepository springDataRepository;

    public JpaShipmentRepository(ShipmentSpringDataRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Optional<Shipment> findById(String shipmentId) {
        return springDataRepository.findById(shipmentId).map(ShipmentJpaEntity::toDomain);
    }

    @Override
    public void save(Shipment shipment) {
        springDataRepository.save(ShipmentJpaEntity.fromDomain(shipment));
    }

    /**
     * Query capability beyond the domain-owned {@link ShipmentRepository} port,
     * offered directly on this adapter for read-side use cases (e.g. "show me
     * the shipment(s) created for order X") that don't belong on the write-side
     * aggregate repository interface.
     */
    public List<Shipment> findAllBySourceOrderId(String sourceOrderId) {
        return springDataRepository.findAllBySourceOrderId(sourceOrderId).stream()
                .map(ShipmentJpaEntity::toDomain)
                .toList();
    }
}
