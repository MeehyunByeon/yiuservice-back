package yiu.aisl.yiuservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import yiu.aisl.yiuservice.domain.Comment_Delivery;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.dto.DeliveryRequest;
import yiu.aisl.yiuservice.dto.DeliveryResponse;
import yiu.aisl.yiuservice.repository.Comment_DeliveryRepository;
import yiu.aisl.yiuservice.repository.DeliveryRepository;
import yiu.aisl.yiuservice.repository.UserRepository;
import yiu.aisl.yiuservice.security.TokenProvider;

import java.rmi.UnexpectedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
    public DeliveryResponse getDetail(DeliveryRequest.DetailDTO request) throws Exception {
        try {
            if(request.getDId().describeConstable().isEmpty()) throw new Exception("dId가 없습니다");
            else {
                Delivery delivery = deliveryRepository.findBydId(request.getDId()).orElseThrow(() -> {
                    throw new IllegalArgumentException("해당 글을 찾을 수 없습니다.");
                });
                DeliveryResponse response = DeliveryResponse.GetDeliveryDTO(delivery);
                return response;
            }
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
            throw new Exception("데이터가 부족합니다.");
        }
        return true;
    }

    // 배달모집글 수정 [writer]
    @Transactional
    public Boolean update(Long studentId, DeliveryRequest.UpdateDTO request) throws Exception{

        User user = findByStudentId(studentId);

        try {
            if(request.getDId().describeConstable().isEmpty()) throw new Exception("dId가 없습니다");
            else {
                Optional<Delivery> optDelivery = deliveryRepository.findBydId(request.getDId());

                if(optDelivery.isEmpty() || !optDelivery.get().getUser().equals(user)) {
                    throw new UnexpectedException("권한이 없습니다.");
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
        Optional<Comment_Delivery> optCommentDelivery = comment_deliveryRepository.findByDcId(request.getDId());

        User user = findByStudentId(studentId);

        Byte deletedState = 0;
        Byte watingState = 1;

        try {
            if(request.getDId().describeConstable().isEmpty()) throw new Exception("dId가 없습니다");
            else {
                if(optCommentDelivery.isEmpty() || optCommentDelivery.stream().allMatch(s -> !s.equals(watingState))) {
                    if(optDelivery.isEmpty() || !optDelivery.get().getUser().equals(user)) {
                        throw new UnexpectedException("권한이 없습니다.");
                    }
                    else {
                        Delivery delivery = optDelivery.get();
                        delivery.setState(deletedState);
                        deliveryRepository.save(delivery);
                        return true;
                    }
                }
                else throw new Exception("아직 진행중인 신청이 있습니다.");
            }
        }
        catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }
    }

    // 배달모집글 마감 [writer]
    @Transactional
    public Boolean finish(Long studentId, DeliveryRequest.dIdDTO request) throws Exception{

        User user = findByStudentId(studentId);
        Byte finishState = 2;

        try {
            Optional<Delivery> optDelivery = deliveryRepository.findBydId(request.getDId());

            if(optDelivery.isEmpty() || !optDelivery.get().getUser().equals(user)) {
                throw new UnexpectedException("권한이 없습니다.");
            }
            else {
                if(optDelivery.get().getState() == 1) {
                    Delivery delivery = optDelivery.get();
                    delivery.setState(finishState);
                    deliveryRepository.save(delivery);
                    return true;
                }
                else throw new IllegalArgumentException("이미 삭제된 글입니다.");
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
            Optional<Comment_Delivery> optCommentDelivery = comment_deliveryRepository.findByUserAndDelivery(user, delivery);
            System.out.println("있냐? " + optCommentDelivery);
            if(optCommentDelivery.isPresent() && (optCommentDelivery.get().getState() == 1 || optCommentDelivery.get().getState() == 2)) {
                throw new Exception("이미 활성화 신청 글이 있습니다");
            }
            else {
                Comment_Delivery comment = Comment_Delivery.builder()
                        .user(user)
                        .delivery(delivery)
                        .contents(request.getContents())
                        .details(request.getDetails())
                        .state(request.getState())
                        .build();
                comment_deliveryRepository.save(comment);
            }
        }
        catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    // 배달모집 신청 취소 [applicant]
    public Boolean cancel(Long studentId, DeliveryRequest.dcIdDTO request) throws Exception {

        User user = findByStudentId(studentId);

        Byte cancelState = 0;

        try {
            Optional<Comment_Delivery> optComment_Delivery = comment_deliveryRepository.findByDcId(request.getDcId());

            if(optComment_Delivery.isEmpty() || !optComment_Delivery.get().getUser().equals(user)) {
                throw new Exception("권한이 업습니다.");
            }
            else {
                if(optComment_Delivery.get().getState() == 2) throw new Exception("이미 수락된 글입니다. 신청 취소가 불가합니다.");
                else {
                    Comment_Delivery comment_delivery = optComment_Delivery.get();
                    comment_delivery.setState(cancelState);
                    comment_deliveryRepository.save(comment_delivery);
                    return true;
                }
            }
        }
        catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }
    }

    // 배달모집 신청 수락 [writer]
    public Boolean accept(Long studentId, DeliveryRequest.dcIdDTO request) throws Exception {

        User user = findByStudentId(studentId);

        Byte acceptState = 2;

        try {
            Optional<Comment_Delivery> optComment_Delivery = comment_deliveryRepository.findByDcId(request.getDcId());
            Delivery delivery = findByDId(optComment_Delivery.get().getDelivery().getDId());

            if(optComment_Delivery.get().getState() != 1)  throw new Exception("활성화 신청 글이 아닙니다");
            else {
                if(!delivery.getUser().equals(user)) throw new Exception("권한이 없습니다");
                else {
                    Comment_Delivery comment_delivery = optComment_Delivery.get();
                    comment_delivery.setState(acceptState);
                    comment_deliveryRepository.save(comment_delivery);
                    return true;
                }
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
            if(optComment_Delivery.get().getState() != 1 || !delivery.getUser().equals(user)) {
                throw new Exception("권한이 업습니다.");
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
