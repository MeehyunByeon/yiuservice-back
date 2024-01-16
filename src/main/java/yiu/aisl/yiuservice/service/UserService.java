package yiu.aisl.yiuservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.*;
import yiu.aisl.yiuservice.domain.state.ApplyState;
import yiu.aisl.yiuservice.domain.state.PostState;
import yiu.aisl.yiuservice.dto.*;
import yiu.aisl.yiuservice.exception.CustomException;
import yiu.aisl.yiuservice.exception.ErrorCode;
import yiu.aisl.yiuservice.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final Comment_DeliveryRepository comment_deliveryRepository;
    private final TaxiRepository taxiRepository;
    private final Comment_TaxiRepository comment_taxiRepository;

    // 내 정보 조회
    @Transactional
    public UserResponse getMyInfo(Long studentId) throws Exception {
        try {
            User user =  findByStudentId(studentId);
            UserResponse response = UserResponse.builder()
                    .studentId(user.getStudentId())
                    .nickname(user.getNickname())
                    .build();
            return response;

        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 내 활성화 글 조회
    @Transactional
    public Map<String, List<?>> getMyActiveList(Long studentId) throws Exception {
        User user = findByStudentId(studentId);

        try {
            // Delivery
            List<Delivery> listDeliveryActive = deliveryRepository.findByUserAndState(user, PostState.ACTIVE);
            List<DeliveryResponse> deliveryGetListDTO = listDeliveryActive.stream().map(DeliveryResponse::GetDeliveryDTO).collect(Collectors.toList());

            // Comment_Delivery
            List<Comment_Delivery> listCommentDeliveryWaiting = comment_deliveryRepository.findByUserAndState(user, ApplyState.WAITING);
            LocalDateTime currentTime = LocalDateTime.now();
            List<Comment_Delivery> listCommentDeliveryAccepted = comment_deliveryRepository.findByUserAndStateAndDueAfter(user, ApplyState.ACCEPTED, currentTime);
            List<Comment_DeliveryResponse> commentDeliveryGetListDTO = Stream.concat(
                    listCommentDeliveryWaiting.stream(),
                    listCommentDeliveryAccepted.stream()
            ).map(Comment_DeliveryResponse::GetCommentDeliveryDTO).collect(Collectors.toList());

            // Taxi
            List<Taxi> listTaxiActive = taxiRepository.findByUserAndState(user, PostState.ACTIVE);
            List<TaxiResponse> taxiGetListDTO = listTaxiActive.stream().map(TaxiResponse::GetTaxiDTO).collect(Collectors.toList());

            // Comment_Taxi
            List<Comment_Taxi> listCommentTaxiWaiting = comment_taxiRepository.findByUserAndState(user, ApplyState.WAITING);
            List<Comment_Taxi> listCommentTaxiAccepted = comment_taxiRepository.findByUserAndStateAndDueAfter(user, ApplyState.ACCEPTED, currentTime);
            List<Comment_TaxiResponse> commentTaxiGetListDTO = Stream.concat(
                    listCommentTaxiWaiting.stream(),
                    listCommentTaxiAccepted.stream()
            ).map(Comment_TaxiResponse::GetCommentTaxiDTO).collect(Collectors.toList());

            // result
            UserActiveDTO result = new UserActiveDTO();
            result.setDeliveryPosts(deliveryGetListDTO);
            result.setDeliveryComments(commentDeliveryGetListDTO);
            result.setTaxiPosts(taxiGetListDTO);
            result.setTaxiComments(commentTaxiGetListDTO);

            return result;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 학번으로 유저의 정보를 가져오는 메서드
    public User findByStudentId(Long studentId) {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_EXIST));
    }
}
