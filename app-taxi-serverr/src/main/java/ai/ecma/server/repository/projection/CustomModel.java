package ai.ecma.server.repository.projection;

import ai.ecma.server.entity.Brand;
import ai.ecma.server.entity.Model;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "customModel", types = Model.class)
public interface CustomModel {
    Integer getId();

    String getName();

    String getDescription();

    Brand getBrand();
}
