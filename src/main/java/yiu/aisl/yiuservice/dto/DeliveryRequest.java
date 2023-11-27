package yiu.aisl.yiuservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class DeliveryRequest {

    @Getter
    @Setter
    public static class CreateDTO {

        private String title;

        private String contents;

        @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
        private LocalDateTime due;

        private Long food;

        private Long location;

        private String link;

        private Byte state = 1;
    }

}
