package yiu.aisl.yiuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yiu.aisl.yiuservice.domain.ActiveEntity;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.Taxi;
import yiu.aisl.yiuservice.domain.state.ApplyState;
import yiu.aisl.yiuservice.domain.state.PostState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxiResponse implements ActiveEntity {
    private Long tId;

    private Long studentId;

    private String nickname;

    private String title;

    private String contents;

    private LocalDateTime due;

    private PostState state;

    private String start;

    private Long startCode;

    private String end;

    private Long endCode;

    private Integer current;

    private Integer max;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<CommentDto> comment;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentDto {
        private Long tcId;

        private Long studentId;

        private String nickname;

        private String contents;

        private String details;

        private Integer number;

        private ApplyState state;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
    }

    public static TaxiResponse GetTaxiDTO(Taxi taxi) {
        return new TaxiResponse(
                taxi.getTId(),
                taxi.getUser().getStudentId(),
                taxi.getUser().getNickname(),
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
                taxi.getUpdatedAt(),
                null
        );
    }

    public static TaxiResponse GetTaxiDetailDTO(Taxi taxi) {
        return new TaxiResponse(
                taxi.getTId(),
                taxi.getUser().getStudentId(),
                taxi.getUser().getNickname(),
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
                taxi.getUpdatedAt(),
                taxi.getComments().stream()
                        .map(comment -> new TaxiResponse.CommentDto(
                                comment.getTcId(),
                                comment.getUser().getStudentId(),
                                comment.getUser().getNickname(),
                                comment.getContents(),
                                comment.getDetails(),
                                comment.getNumber(),
                                comment.getState(),
                                comment.getCreatedAt(),
                                comment.getUpdatedAt()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
