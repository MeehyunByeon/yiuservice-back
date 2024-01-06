package yiu.aisl.yiuservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import yiu.aisl.yiuservice.domain.state.ApplyState;
import yiu.aisl.yiuservice.domain.state.PostState;

import java.time.LocalDateTime;

public class DeliveryRequest {

    @Getter
    @Setter
    public static class dIdDTO {
        private Long dId;

    }

    @Getter
    @Setter
    public static class dcIdDTO {
        private Long dcId;
    }


    @Getter
    @Setter
    public static class DetailDTO {
        private Long dId;
    }

    @Getter
    @Setter
    public static class CreateDTO {
        private String title;

        private String contents;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime due;

        private Long food;

        private Long location;

        private String link;

        private PostState state = PostState.ACTIVE;
    }

    @Getter
    @Setter
    public static class UpdateDTO {

        private Long dId;

        private Long studentId;

        private String title;

        private String contents;

        @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
        private LocalDateTime due;

        private Long food;

        private Long location;

        private String link;

        private int state = 1;

        public PostState getPostState() {
            return PostState.fromInt(state);
        }
    }

    @Getter
    @Setter
    public static class ApplyDTO {
        private Long dId;

        private Long studentId;

        private String contents;

        private String details;

        private ApplyState state = ApplyState.WAITING;
    }

}
