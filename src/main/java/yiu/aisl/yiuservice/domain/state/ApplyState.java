package yiu.aisl.yiuservice.domain.state;

import lombok.Getter;

@Getter
public enum ApplyState {
    CANCELED(0), // 취소(신청자가 취소)
    WAITING(1), // 대기
    ACCEPTED(2), // 수락
    REJECTED(3), // 거절
    FINISHED(4); // 모집 마감

    private final int state;

    ApplyState(Integer state) {
        this.state = state;
    }
}
