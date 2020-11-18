package ai.ecma.server.repository;

import ai.ecma.server.entity.Model;
import ai.ecma.server.repository.projection.CustomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "model", collectionResourceRel = "list", excerptProjection = CustomModel.class)
public interface ModelRepository extends JpaRepository<Model, Integer> {
    boolean existsByName(String name);

}
