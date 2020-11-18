package ai.ecma.server.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableDto {
    private Object object;
    private long totalElements;
    private int totalPages;
    private int size;
    private int currentPage;
}
