package yiu.aisl.yiuservice.domain.state;

import lombok.Getter;

@Getter
public enum EntityCode {
    NOTICE(0), // 공지사항
    DELIVERY(1), // 배달
    DELIVERY_CMT(2), // 배달 신청
    TAXI(3), // 택시
    TAXI_CMT(4); // 택시 신청
    private final int state;

    EntityCode(Integer state) {
        this.state = state;
    }

    public static EntityCode fromInt(int value) {
        for (EntityCode entityState : EntityCode.values()) {
            if (entityState.getState() == value) {
                return entityState;
            }
        }
        throw new IllegalArgumentException("Invalid ApplyState value: " + value);
    }
}
