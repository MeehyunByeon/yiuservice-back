package yiu.aisl.yiuservice.domain.state;

import lombok.Getter;

@Getter
public enum PostState {
    DELETED(0), // 삭제
    ACTIVE(1), // 모집중
    FINISHED(2); // 마감

    private final int state;

    PostState(int state) {
        this.state = state;
    }
}
