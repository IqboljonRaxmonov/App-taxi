package ai.ecma.server.repository;

import ai.ecma.server.entity.Order;
import ai.ecma.server.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    boolean existsByIdAndStatus(UUID id, OrderStatus status);
    Optional<Order> findByIdAndStatus(UUID id, OrderStatus status);
}
