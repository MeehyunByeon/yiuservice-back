package yiu.aisl.yiuservice.domain.state;

import lombok.Getter;

@Getter
public enum PostState {
    DELETED(0),
    ACTIVE(1),
    FINISHED(2);

    private final int state;

    PostState(int state) {
        this.state = state;
    }
}
