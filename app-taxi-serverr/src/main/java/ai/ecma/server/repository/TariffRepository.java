package ai.ecma.server.repository;

import ai.ecma.server.entity.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * BY SIROJIDDIN on 10.11.2020
 */


@RepositoryRestResource(path = "tariff")
public interface TariffRepository extends JpaRepository<Tariff, Integer> {
    Optional<Tariff> findAllById(Integer id);
}
