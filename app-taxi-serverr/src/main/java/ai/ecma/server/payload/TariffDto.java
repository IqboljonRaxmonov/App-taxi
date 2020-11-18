package ai.ecma.server.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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
