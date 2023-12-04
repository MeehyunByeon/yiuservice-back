package yiu.aisl.yiuservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.dto.DeliveryResponse;
import yiu.aisl.yiuservice.dto.UserJoinRequestDto;
import yiu.aisl.yiuservice.dto.UserResponse;
import yiu.aisl.yiuservice.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse getMyInfo(Long studentId) throws Exception {
        try {
            User user =  findByStudentId(studentId);
            UserResponse response = UserResponse.builder()
                    .studentId(user.getStudentId())
                    .nickname(user.getNickname())
                    .build();
            return response;

        } catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }
    }

    // 학번으로 유저의 정보를 가져오는 메서드
    public User findByStudentId(Long studentId) {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));
    }
}
