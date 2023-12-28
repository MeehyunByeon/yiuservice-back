package yiu.aisl.yiuservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class TaxiRequest {
    @Getter
    @Setter
    public static class tIdDTO {
        private Long tId;

    }

    @Getter
    @Setter
    public static class tcIdDTO {
        private Long tcId;

    }


    @Getter
    @Setter
    public static class DetailDTO {
        private Long tId;

    }

    @Getter
    @Setter
    public static class CreateDTO {

        private String title;

        private String contents;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime due;

//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
//        private LocalDateTime createDate;
//

        private String start;

        private Long startCode;

        private String end;

        private Long endCode;

        private Integer current = 1;

        private Integer max;

        private Byte state = 1;

    }

    @Getter
    @Setter
    public static class UpdateDTO {

        private Long tId;

        private Long studentId;

        private String title;

        private String contents;

//        @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
//        private LocalDateTime due;

//        @DateTimeFormat(pattern = "yyyy-MM-dd kk:mm:ss")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd kk:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime due;

        private String start;

        private Long startCode;

        private String end;

        private Long endCode;

        private Integer current;

        private Integer max;

        private Byte state;
    }

    @Getter
    @Setter
    public static class ApplyDTO {

        private Long tId;

        private Long studentId;

        private String contents;

        private String details;

        private Integer number;

        private Byte state = 1;
    }
}
