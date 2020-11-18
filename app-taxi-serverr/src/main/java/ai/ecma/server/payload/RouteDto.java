package ai.ecma.server.payload;

import ai.ecma.server.entity.Order;
import ai.ecma.server.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto{
    private Integer orderId;
    @NotNull
    private Float fromLat;
    @NotNull
    private Float fromLon;
    private Float toLat;
    private Float toLon;
}

