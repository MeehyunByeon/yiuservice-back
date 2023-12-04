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
        try {
            Delivery delivery = deliveryRepository.findBydId(request.getDId()).orElseThrow(() -> {
                return new IllegalArgumentException("해당 글을 찾을 수 없습니다.");
            });
            DeliveryResponse response = DeliveryResponse.GetDeliveryDTO(delivery);
            return response;
        } catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }

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
    public Boolean delete(Long studentId, DeliveryRequest.dIdDTO request) throws Exception{

        Optional<Delivery> optDelivery = deliveryRepository.findBydId(request.getDId());

        User user = findByStudentId(studentId);

        Byte deletedState = 0;

        // 1. if 신청이 없으면 삭제 가능 => 204 + state = 0
        // 2. if 신청이 있으면
        //      1) 모두 거절인 상태 => 204 + state = 0
        //      2) 아직 진행 중 => 404
        try {
            if(optDelivery.isEmpty() || !optDelivery.get().getUser().equals(user)) {
                return false;
            }
            else {
                Delivery delivery = optDelivery.get();
                delivery.setState(deletedState);
                deliveryRepository.save(delivery);
                return true;
            }
        }
        catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }
    }

    // 배달모집글 마감 [writer]
    @Transactional
    public Boolean finish(Long studentId, DeliveryRequest.dIdDTO request) throws Exception{

        Optional<Delivery> optDelivery = deliveryRepository.findBydId(request.getDId());

        User user = findByStudentId(studentId);
        Byte finishState = 2;

        try {
            if(optDelivery.isEmpty() || !optDelivery.get().getUser().equals(user)) {
                return false;
            }
            else {
                Delivery delivery = optDelivery.get();
                delivery.setState(finishState);
                deliveryRepository.save(delivery);
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
    public Boolean cancel(Long studentId, DeliveryRequest.dcIdDTO request) throws Exception {
        Optional<Comment_Delivery> optComment_Delivery = comment_deliveryRepository.findByDcId(request.getDcId());

        User user = findByStudentId(studentId);

        Byte cancelState = 0;

        try {
            if(optComment_Delivery.isEmpty() || !optComment_Delivery.get().getUser().equals(user)) {
                return false;
            }
            else {
                Comment_Delivery comment_delivery = optComment_Delivery.get();
                comment_delivery.setState(cancelState);
                comment_deliveryRepository.save(comment_delivery);
                return true;
            }
        }
        catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }
    }

    // 배달모집 신청 수락 [writer]
    public Boolean accept(Long studentId, DeliveryRequest.dcIdDTO request) throws Exception {
        Optional<Comment_Delivery> optComment_Delivery = comment_deliveryRepository.findByDcId(request.getDcId());

        Delivery delivery = findByDId(optComment_Delivery.get().getDelivery().getDId());

        User user = findByStudentId(studentId);

        Byte acceptState = 2;

        try {
            if(optComment_Delivery.isEmpty() || !delivery.getUser().equals(user)) {
                return false;
            }
            else {
                Comment_Delivery comment_delivery = optComment_Delivery.get();
                comment_delivery.setState(acceptState);
                comment_deliveryRepository.save(comment_delivery);
                return true;
            }
        }
        catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }
    }

    // 배달모집 신청 거부 [writer]
    public Boolean reject(Long studentId, DeliveryRequest.dcIdDTO request) throws Exception {
        Optional<Comment_Delivery> optComment_Delivery = comment_deliveryRepository.findByDcId(request.getDcId());

        Delivery delivery = findByDId(optComment_Delivery.get().getDelivery().getDId());

        User user = findByStudentId(studentId);

        Byte rejectState = 3;

        try {
            if(optComment_Delivery.isEmpty() || !delivery.getUser().equals(user)) {
                return false;
            }
            else {
                Comment_Delivery comment_delivery = optComment_Delivery.get();
                comment_delivery.setState(rejectState);
                comment_deliveryRepository.save(comment_delivery);
                return true;
            }
        }
        catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }
    }

    // 학번으로 유저의 정보를 가져오는 메서드
    public User findByStudentId(Long studentId) {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));
    }

    // dId로 배달 모집 글 정보를 가져오는 메서드
    public Delivery findByDId(Long dId) {
        return deliveryRepository.findBydId(dId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글"));
    }

    // dcId로 배달 신청 정보를 가져오는 메서드
    public Comment_Delivery findByDcId(Long dcId) {
        return comment_deliveryRepository.findByDcId(dcId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청"));
    }
}
