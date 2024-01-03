package yiu.aisl.yiuservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import yiu.aisl.yiuservice.domain.Comment_Delivery;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.domain.state.ApplyState;
import yiu.aisl.yiuservice.domain.state.PostState;
import yiu.aisl.yiuservice.dto.DeliveryRequest;
import yiu.aisl.yiuservice.dto.DeliveryResponse;
import yiu.aisl.yiuservice.exception.CustomException;
import yiu.aisl.yiuservice.exception.ErrorCode;
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
        try {
            List<Delivery> delivery = deliveryRepository.findAll();
            List<DeliveryResponse> getListDTO = new ArrayList<>();
            delivery.forEach(s -> getListDTO.add(DeliveryResponse.GetDeliveryDTO(s)));
            return getListDTO;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 배달모집글 상세조회 [all]
    public DeliveryResponse getDetail(DeliveryRequest.DetailDTO request) throws Exception {
        try {
            // 400 - 데이터 없음
            if(request.getDId().describeConstable().isEmpty()) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
            else {
                Delivery delivery = deliveryRepository.findBydId(request.getDId()).orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_EXIST);
                });
                DeliveryResponse response = DeliveryResponse.GetDeliveryDetailDTO(delivery);
                return response;
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }


    // 배달모집글 작성 [writer]
    @Transactional
    public Boolean create(Long studentId, DeliveryRequest.CreateDTO request) throws Exception{
        // 400 - 데이터 없음
        if(request.getTitle() == null || request.getContents() == null || request.getDue() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        try {
            // 유저 확인 404 포함
            User user = findByStudentId(studentId);

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
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // 배달모집글 수정 [writer]
    @Transactional
    public Boolean update(Long studentId, DeliveryRequest.UpdateDTO request) throws Exception{
        try {
            // 유저 확인 404 포함
            User user = findByStudentId(studentId);

            // 400 - 데이터 없음
            if(request.getDId().describeConstable().isEmpty() || request.getTitle() == null || request.getContents() == null || request.getDue() == null)
                throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
            else {
                Optional<Delivery> optDelivery = deliveryRepository.findBydId(request.getDId());

                // 404 - 글이 존재하지 않음
                if(optDelivery.isEmpty()) {
                    throw new CustomException(ErrorCode.NOT_EXIST);
                }

                // 403 - 권한 없음
                if(!optDelivery.get().getUser().equals(user)) {
                    throw new CustomException(ErrorCode.ACCESS_NO_AUTH);
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
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // 배달모집글 삭제 [writer]
    @Transactional
    public Boolean delete(Long studentId, DeliveryRequest.dIdDTO request) throws Exception{
        try {

            // 400 - 데이터 없음
            if(request.getDId() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

            // 유저 확인 404 포함
            User user = findByStudentId(studentId);

            Optional<Delivery> optDelivery = deliveryRepository.findBydId(request.getDId());
            Optional<Comment_Delivery> optCommentDelivery = comment_deliveryRepository.findByDcId(request.getDId());

            // 404 - 글 존재하지 않음
            if(optDelivery.isEmpty()) throw new CustomException(ErrorCode.NOT_EXIST);

            // 403 - 권한 없음(작성인 != 삭제요청인)
            if(!optDelivery.get().getUser().equals(user)) throw new CustomException(ErrorCode.ACCESS_NO_AUTH);

            // 409 - 아직 진행 중인 신청이 있으면 임의로 삭제 안됨
            if(!optCommentDelivery.stream().allMatch(s -> !s.equals(ApplyState.WAITING)))
                throw new CustomException(ErrorCode.CONFLICT);

            Delivery delivery = optDelivery.get();
            delivery.setState(PostState.DELETED);
            deliveryRepository.save(delivery);
            return true;

//            else {
//                if(optCommentDelivery.isEmpty() || optCommentDelivery.stream().allMatch(s -> !s.equals(ApplyState.WAITING))) {
//                    if(optDelivery.isEmpty() || !optDelivery.get().getUser().equals(user)) {
//                        throw new UnexpectedException("권한이 없습니다.");
//                    }
//                    else {
//                        Delivery delivery = optDelivery.get();
//                        delivery.setState(PostState.DELETED);
//                        deliveryRepository.save(delivery);
//                        return true;
//                    }
//                }
//                else throw new Exception("아직 진행중인 신청이 있습니다.");
//            }
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 배달모집글 마감 [writer]
    @Transactional
    public Boolean finish(Long studentId, DeliveryRequest.dIdDTO request) throws Exception{
        try {
            // 400 - 데이터 없음
            if(request.getDId() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

            // 유저 확인 404 포함
            User user = findByStudentId(studentId);
            Optional<Delivery> optDelivery = deliveryRepository.findBydId(request.getDId());

            // 404 - 글 존재하지 않음
            if(optDelivery.isEmpty()) throw new CustomException(ErrorCode.NOT_EXIST);

            // 403 - 권한 없음(작성인 != 마감요청인)
            if(!optDelivery.get().getUser().equals(user)) throw new CustomException(ErrorCode.ACCESS_NO_AUTH);

            // 409 - 이미 글이 마감된 상태 or 삭제된 상태
            if(optDelivery.get().getState().equals(PostState.FINISHED) || optDelivery.get().getState().equals(PostState.DELETED)) throw new CustomException(ErrorCode.CONFLICT);

            Delivery delivery = optDelivery.get();
            delivery.setState(PostState.FINISHED);
            deliveryRepository.save(delivery);

            // ### 마감할 때 신청글을 모두 FINISHED 처리 ###

            return true;
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 배달모집 신청 [applicant]
    @Transactional
    public Boolean apply(Long studentId, DeliveryRequest.ApplyDTO request) throws Exception{
        try {

            // 400 - 데이터 없음
            if(request.getDId() == null || request.getContents() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

            // 유저 확인 404 포함
            User user = findByStudentId(studentId);
            Delivery delivery = findByDId(request.getDId());
            Optional<Comment_Delivery> optCommentDelivery = comment_deliveryRepository.findByUserAndDelivery(user, delivery);

            // 404 - 글 존재하지 않음
            if(optCommentDelivery.isEmpty()) throw new CustomException(ErrorCode.NOT_EXIST);

            // 403 - 권한 없음 => 자신의 글에 신청한 경우(작성인 == 신청인)
            if(delivery.getUser().equals(user)) throw new CustomException(ErrorCode.ACCESS_NO_AUTH);

            // 409 - 신청 => 대기 상태 OR 수락 상태
            if(optCommentDelivery.get().getState() == ApplyState.WAITING || optCommentDelivery.get().getState() == ApplyState.ACCEPTED) {
                throw new CustomException(ErrorCode.CONFLICT);
            }

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
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // 배달모집 신청 취소 [applicant]
    public Boolean cancel(Long studentId, DeliveryRequest.dcIdDTO request) throws Exception {
        try {
            // 400 - 데이터 없음
            if(request.getDcId() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

            // 유저 확인 404 포함
            User user = findByStudentId(studentId);
            Optional<Comment_Delivery> optComment_Delivery = comment_deliveryRepository.findByDcId(request.getDcId());

            // 404 - 해당 신청 글 없음
            if(optComment_Delivery.isEmpty()) throw new CustomException(ErrorCode.NOT_EXIST);

            // 403 - 권한 없음(신청자 != 신청글 삭제 요청인)
            if(!optComment_Delivery.get().getUser().equals(user)) throw new CustomException(ErrorCode.ACCESS_NO_AUTH);

            // 409 - 취소 불가 => 수락 상태 OR 마감 상태
            if(optComment_Delivery.get().getState().equals(ApplyState.ACCEPTED) || optComment_Delivery.get().getState().equals(ApplyState.FINISHED))
                throw new CustomException(ErrorCode.CONFLICT);

            Comment_Delivery comment_delivery = optComment_Delivery.get();
            comment_delivery.setState(ApplyState.CANCELED);
            comment_deliveryRepository.save(comment_delivery);
            return true;
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 배달모집 신청 수락 [writer]
    public Boolean accept(Long studentId, DeliveryRequest.dcIdDTO request) throws Exception {
        try {
            // 400 - 데이터 없음
            if(request.getDcId() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

            // 유저 확인 404 포함
            User user = findByStudentId(studentId);
            Optional<Comment_Delivery> optComment_Delivery = comment_deliveryRepository.findByDcId(request.getDcId());

            // 배달글 404 포함
            Delivery delivery = findByDId(optComment_Delivery.get().getDelivery().getDId());

            // 403 - 신청 수락 권한 없음
            if(!delivery.getUser().equals(user)) throw new CustomException(ErrorCode.ACCESS_NO_AUTH);

            // 409 - 글이 활성화 상태가 아님 OR 신청이 대기 상태가 아님(삭제, 거절 등)
            if(delivery.getState().equals(PostState.ACTIVE) || optComment_Delivery.get().getState().equals(ApplyState.WAITING)) throw new CustomException(ErrorCode.CONFLICT);

            Comment_Delivery comment_delivery = optComment_Delivery.get();
            comment_delivery.setState(ApplyState.ACCEPTED);
            comment_deliveryRepository.save(comment_delivery);
            return true;
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 배달모집 신청 거부 [writer]
    public Boolean reject(Long studentId, DeliveryRequest.dcIdDTO request) throws Exception {
        try {
            // 400 - 데이터 없음
            if(request.getDcId() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

            Optional<Comment_Delivery> optComment_Delivery = comment_deliveryRepository.findByDcId(request.getDcId());

            // 유저 확인 404 포함
            User user = findByStudentId(studentId);

            // 배달글 404 포함
            Delivery delivery = findByDId(optComment_Delivery.get().getDelivery().getDId());

            // 403 - 거절 수락 권한 없음
            if(!delivery.getUser().equals(user)) throw new CustomException(ErrorCode.ACCESS_NO_AUTH);

            // 409 - 글이 활성화 상태가 아님 OR 신청이 대기 상태가 아님(삭제, 거절 등)
            if(delivery.getState().equals(PostState.ACTIVE) || optComment_Delivery.get().getState().equals(ApplyState.WAITING)) throw new CustomException(ErrorCode.CONFLICT);

            Comment_Delivery comment_delivery = optComment_Delivery.get();
            comment_delivery.setState(ApplyState.REJECTED);
            comment_deliveryRepository.save(comment_delivery);
            return true;
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 학번으로 유저의 정보를 가져오는 메서드
    public User findByStudentId(Long studentId) {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_EXIST));
    }

    // dId로 배달 모집 글 정보를 가져오는 메서드
    public Delivery findByDId(Long dId) {
        return deliveryRepository.findBydId(dId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
    }

    // dcId로 배달 신청 정보를 가져오는 메서드
    public Comment_Delivery findByDcId(Long dcId) {
        return comment_deliveryRepository.findByDcId(dcId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
    }
}
