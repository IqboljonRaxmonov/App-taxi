package ai.ecma.server.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * BY SIROJIDDIN on 10.11.2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffDto {
    @NotNull
    private Integer id;
    private String name;
    private Double price;

//    private Double initialPrice;

}
