package ai.ecma.server.repository;

import ai.ecma.server.entity.MissedOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;
//@RepositoryRestResource(path = "missed")
public interface MissedRepository extends JpaRepository<MissedOrder, UUID> {
        boolean
        existsByCarIdAndOrderId(UUID car_id, UUID order_id);
}
