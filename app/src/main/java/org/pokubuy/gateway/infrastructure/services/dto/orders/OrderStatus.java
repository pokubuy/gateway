package org.pokubuy.gateway.infrastructure.services.dto.orders;

public enum OrderStatus {
    CREATED,
    PENDING_PAYMENT,
    PAID,
    IN_PROGRESS,
    COMPLETED_BY_SELLER,
    FINISHED,
    DISPUTE,
    CANCELED,
    REFUNDED
}
