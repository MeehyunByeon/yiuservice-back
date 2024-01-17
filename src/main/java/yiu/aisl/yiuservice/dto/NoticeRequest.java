package yiu.aisl.yiuservice.dto;

import lombok.Getter;
import lombok.Setter;

public class NoticeRequest {
    @Getter
    @Setter
    public static class noticeIdDTO {
        private Long noticeId;

    }

    @Getter
    @Setter
    public static class DetailDTO {
        private Long noticeId;
    }

    @Getter
    @Setter
    public static class CreateDTO {
        private String title;

        private String contents;
    }

    @Getter
    @Setter
    public static class UpdateDTO {

        private Long noticeId;

        private String title;

        private String contents;
    }
}
