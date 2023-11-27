package yiu.aisl.yiuservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.Comment_Delivery;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.dto.DeliveryRequest;
import yiu.aisl.yiuservice.dto.DeliveryResponse;
import yiu.aisl.yiuservice.repository.Comment_DeliveryRepository;
import yiu.aisl.yiuservice.repository.DeliveryRepository;
import yiu.aisl.yiuservice.repository.UserRepository;
import yiu.aisl.yiuservice.security.TokenProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final Comment_DeliveryRepository comment_deliveryRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    // 전체 배달모집글 조회 [all]
    @Transactional
    public List<DeliveryResponse> getList() throws Exception {
        List<Delivery> delivery = deliveryRepository.findAll();
        List<DeliveryResponse> getListDTO = new ArrayList<>();
        delivery.forEach(s -> getListDTO.add(DeliveryResponse.GetDeliveryDTO(s)));
        return getListDTO;
    }

    // 배달모집글 상세조회 [all]
    public DeliveryResponse getDetail(DeliveryRequest.DetailDTO request) throws Exception{
        Delivery delivery = deliveryRepository.findBydId(request.getDId()).orElseThrow(() -> {
            return new IllegalArgumentException("해당 글을 찾을 수 없습니다.");
        });

        DeliveryResponse response = DeliveryResponse.GetDeliveryDTO(delivery);
        return response;
    }


    // 배달모집글 작성 [writer]
    @Transactional
    public Boolean create(Long studentId, DeliveryRequest.CreateDTO request) throws Exception{

        User user = findByStudentId(studentId);

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

    // 배달모집글 수정 [writer]
    @Transactional
    public Boolean update(Long studentId, DeliveryRequest.UpdateDTO request) throws Exception{

        Optional<Delivery> optDelivery = deliveryRepository.findBydId(request.getDId());

        User user = findByStudentId(studentId);

        try {

            if(optDelivery.isEmpty() || !optDelivery.get().getUser().equals(user)) {
                return false;
            }
            else {
                Delivery delivery = Delivery.builder()
                        .user(user)
                        .dId(request.getDId())
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
        }
        catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    // 배달모집글 삭제 [writer]
    @Transactional
    public Boolean delete(Long studentId, DeliveryRequest.DeleteDTO request) throws Exception{

        Optional<Delivery> optDelivery = deliveryRepository.findBydId(request.getDId());

        User user = findByStudentId(studentId);

        // 1. if 신청이 없으면 삭제 가능 => 204 + state = 0
        // 2. if 신청이 있으면
        //      1) 모두 거절인 상태 => 204 + state = 0
        //      2) 아직 진행 중 => 404
        try {
            if(optDelivery.isEmpty() || !optDelivery.get().getUser().equals(user)) {
                return false;
            }
            else {
                deliveryRepository.delete(optDelivery.get());
                return true;
            }
        }
        catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }
    }

    // 배달모집 신청 [applicant]
    @Transactional
    public Boolean apply(Long studentId, DeliveryRequest.ApplyDTO request) throws Exception{

        User user = findByStudentId(studentId);
        Delivery delivery = findByDId(request.getDId());

        try {
            Comment_Delivery comment = Comment_Delivery.builder()
                    .user(user)
                    .delivery(delivery)
                    .contents(request.getContents())
                    .details(request.getDetails())
                    .state(request.getState())
                    .build();
            comment_deliveryRepository.save(comment);
        }
        catch (Exception e) {
//            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    // 배달모집 신청 취소 [applicant]

    // 배달모집 신청 수락 [writer]

    // 배달모집 신청 거부 [writer]

    // 학번으로 유저의 정보를 가져오는 메서드
    public User findByStudentId(Long studentId) {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));
    }

    public Delivery findByDId(Long dId) {
        return deliveryRepository.findBydId(dId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글"));
    }

}
