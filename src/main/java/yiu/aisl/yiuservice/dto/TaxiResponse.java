package yiu.aisl.yiuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.Taxi;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxiResponse {

    private Long tId;

    private Long studentId;

    private String title;

    private String contents;

    private LocalDateTime due;

    private Byte state;

    private String start;

    private Long startCode;

    private String end;

    private Long endCode;

    private Integer current;

    private Integer max;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static TaxiResponse GetTaxiDTO(Taxi taxi) {
        return new TaxiResponse(
                taxi.getTId(),
                taxi.getUser().getStudentId(),
                taxi.getTitle(),
                taxi.getContents(),
                taxi.getDue(),
                taxi.getState(),
                taxi.getStart(),
                taxi.getStartCode(),
                taxi.getEnd(),
                taxi.getEndCode(),
                taxi.getCurrent(),
                taxi.getMax(),
                taxi.getCreatedAt(),
                taxi.getUpdatedAt()
        );
    }
}
