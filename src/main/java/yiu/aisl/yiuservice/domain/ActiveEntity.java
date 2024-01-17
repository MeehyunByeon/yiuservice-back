package yiu.aisl.yiuservice.domain;

import yiu.aisl.yiuservice.domain.state.ApplyState;
import yiu.aisl.yiuservice.domain.state.PostState;
import yiu.aisl.yiuservice.domain.state.State;

import java.time.LocalDateTime;

public interface ActiveEntity {
    LocalDateTime getCreatedAt();
    State getState();
}
