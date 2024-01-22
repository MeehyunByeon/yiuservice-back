package yiu.aisl.yiuservice.dto;

import lombok.Getter;
import lombok.Setter;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.domain.state.EntityCode;
import yiu.aisl.yiuservice.domain.state.PostState;

public class ReportRequest {
    @Getter
    @Setter
    public static class CreateDTO {
        private Long fromId;

        private Long toId;

        private String contents;

        private Long id;

        private Integer type;
    }
}
