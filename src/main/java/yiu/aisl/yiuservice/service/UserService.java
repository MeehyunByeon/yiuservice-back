package yiu.aisl.yiuservice.service;

import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.dto.UserJoinRequestDto;


// TermProject => vo 역할
public interface UserService {
    //
    public Boolean join(UserJoinRequestDto requestDto) throws Exception;

//    User findUser(Long studentId);
}
