package ai.ecma.server.repository.projection;

import ai.ecma.server.entity.Brand;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "brandRepositoryDto",types = Brand.class)
public interface BrandRepositoryDto {
    Integer getId();
    String getName();
    String getDescription();
}
