package yiu.aisl.yiuservice.domain.state;

import lombok.Getter;

@Getter
public enum ApplyState {
    CANCELED(0),
    WAITING(1),
    ACCEPTED(2),
    REJECTED(3),
    FINISHED(4);

    private final Integer state;

    ApplyState(Integer state) {
        this.state = state;
    }
}
