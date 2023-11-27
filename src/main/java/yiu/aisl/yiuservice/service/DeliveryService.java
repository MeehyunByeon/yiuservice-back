package yiu.aisl.yiuservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.dto.DeliveryRequest;
import yiu.aisl.yiuservice.repository.Comment_DeliveryRepository;
import yiu.aisl.yiuservice.repository.DeliveryRepository;
import yiu.aisl.yiuservice.repository.UserRepository;
import yiu.aisl.yiuservice.security.TokenProvider;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final Comment_DeliveryRepository comment_deliveryRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public Boolean create(Long studentId, DeliveryRequest.CreateDTO request) throws Exception{

        User user = userRepository.findByStudentId(studentId).orElseThrow(
                () -> new RuntimeException("유저 정보가 없습니다"));
        try {
            Delivery delivery = Delivery.builder()
                    .user(user)
                    .title(request.getTitle())
                    .contents(request.getContents())
                    .due(request.getDue())
                    .food(request.getFood())
                    .location(request.getLocation())
                    .link(request.getLink())
                    .state(request.getState())
                    .build();
            deliveryRepository.save(delivery);
        }
        catch (Exception e) {
//            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }
}
