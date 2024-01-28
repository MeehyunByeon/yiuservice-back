package yiu.aisl.yiuservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.*;
import yiu.aisl.yiuservice.domain.state.ApplyState;
import yiu.aisl.yiuservice.domain.state.EntityCode;
import yiu.aisl.yiuservice.domain.state.PostState;
import yiu.aisl.yiuservice.dto.*;
import yiu.aisl.yiuservice.exception.CustomException;
import yiu.aisl.yiuservice.exception.ErrorCode;
import yiu.aisl.yiuservice.repository.*;

import java.time.LocalDateTime;
import java.util.*;
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

    // <API> 내 정보 조회
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

    // <API> 내 활성화 글 조회
    @Transactional
    public List<ActiveEntity> getMyActiveList(Long studentId) throws Exception {
        User user = findByStudentId(studentId);

        try {
            LocalDateTime currentTime = LocalDateTime.now();
            Comparator<ActiveEntity> comparator = Comparator.comparing(
                    ActiveEntity::getCreatedAt).reversed();

            // Delivery
            List<Delivery> listDeliveryActive = deliveryRepository.findByUser(user);
            for(Delivery delivery : listDeliveryActive) {
                if(delivery.getDue().isBefore(currentTime)) {
                    delivery.setState(PostState.FINISHED);
                    deliveryRepository.save(delivery);
                    List<Comment_Delivery> comments = comment_deliveryRepository.findByDelivery(delivery);
                    for(Comment_Delivery comment : comments) {
                        comment.setState(ApplyState.FINISHED);
                        comment_deliveryRepository.save(comment);
                    }
                }
            }
            List<ActiveEntity> deliveryGetListDTO = listDeliveryActive.stream()
                    .filter(delivery -> delivery.getState() == PostState.ACTIVE)
                    .map(DeliveryResponse::GetDeliveryDTO)
                    .sorted(comparator)
                    .collect(Collectors.toList());

            // Comment_Delivery
            List<Comment_Delivery> listCommentDelivery = comment_deliveryRepository.findByUser(user);
            List<Comment_Delivery> listCommentDeliveryWaiting = listCommentDelivery.stream()
                    .filter(comment -> comment.getState() == ApplyState.WAITING)
                    .collect(Collectors.toList());
            List<Comment_Delivery> listCommentDeliveryAccepted = listCommentDelivery.stream()
                    .filter(comment -> comment.getState() == ApplyState.ACCEPTED && comment.getDelivery().getDue().isAfter(currentTime))
                    .collect(Collectors.toList());
            List<ActiveEntity> commentDeliveryGetListDTO = Stream.concat(
                    listCommentDeliveryWaiting.stream(),
                    listCommentDeliveryAccepted.stream()
            ).map(comment -> {
                Comment_DeliveryResponse dto = Comment_DeliveryResponse.GetCommentDeliveryDTO(comment);
                // Convert Delivery to DeliveryResponse and set it
                dto.setDelivery(DeliveryResponse.GetDeliveryDTO(comment.getDelivery()));
                return dto;
            }).sorted(comparator).collect(Collectors.toList());

            // Taxi
            List<Taxi> listTaxiActive = taxiRepository.findByUser(user);
            for(Taxi taxi : listTaxiActive) {
                if(taxi.getDue().isBefore(currentTime)) {
                    taxi.setState(PostState.FINISHED);
                    taxiRepository.save(taxi);
                    List<Comment_Taxi> comments = comment_taxiRepository.findByTaxi(taxi);
                    for(Comment_Taxi comment : comments) {
                        comment.setState(ApplyState.FINISHED);
                        comment_taxiRepository.save(comment);
                    }
                }
            }
            List<ActiveEntity> taxiGetListDTO = listTaxiActive.stream()
                    .filter(taxi -> taxi.getState() == PostState.ACTIVE)
                    .map(TaxiResponse::GetTaxiDTO)
                    .sorted(comparator)
                    .collect(Collectors.toList());

            // Comment_Taxi
            List<Comment_Taxi> listCommentTaxi = comment_taxiRepository.findByUser(user);
            List<Comment_Taxi> listCommentTaxiWaiting = listCommentTaxi.stream()
                    .filter(comment -> comment.getState() == ApplyState.WAITING)
                    .collect(Collectors.toList());
            List<Comment_Taxi> listCommentTaxiAccepted = listCommentTaxi.stream()
                    .filter(comment -> comment.getState() == ApplyState.ACCEPTED && comment.getTaxi().getDue().isAfter(currentTime))
                    .collect(Collectors.toList());
            List<ActiveEntity> commentTaxiGetListDTO = Stream.concat(
                            listCommentTaxiWaiting.stream(),
                            listCommentTaxiAccepted.stream()
            ).map(comment -> {
                Comment_TaxiResponse dto = Comment_TaxiResponse.GetCommentTaxiDTO(comment);
                // Convert Taxi to TaxiResponse and set it
                dto.setTaxi(TaxiResponse.GetTaxiDTO(comment.getTaxi()));
                return dto;
            }).sorted(comparator).collect(Collectors.toList());

            // Combine all lists into one
            List<ActiveEntity> combinedList = Stream.of(deliveryGetListDTO, commentDeliveryGetListDTO, taxiGetListDTO, commentTaxiGetListDTO)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            // Sort the combined list
            combinedList.sort(comparator);

            return combinedList;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // <API> 내 모든 글 조회
    @Transactional
    public List<ActiveEntity> getMyAllPostList(Long studentId) throws Exception {
        User user = findByStudentId(studentId);

        try {
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime weekAgo = currentTime.minusDays(7);
            Comparator<ActiveEntity> comparator = Comparator.comparing(
                    ActiveEntity::getCreatedAt).reversed();

            // Delivery
            List<Delivery> listDeliveryActive = deliveryRepository.findByUser(user);
            for(Delivery delivery : listDeliveryActive) {
                if(delivery.getDue().isBefore(currentTime)) {
                    delivery.setState(PostState.FINISHED);
                    deliveryRepository.save(delivery);
                    List<Comment_Delivery> comments = comment_deliveryRepository.findByDelivery(delivery);
                    for(Comment_Delivery comment : comments) {
                        comment.setState(ApplyState.FINISHED);
                        comment_deliveryRepository.save(comment);
                    }
                }
            }

            // Taxi
            List<Taxi> listTaxiActive = taxiRepository.findByUser(user);
            for(Taxi taxi : listTaxiActive) {
                if(taxi.getDue().isBefore(currentTime)) {
                    taxi.setState(PostState.FINISHED);
                    taxiRepository.save(taxi);
                    List<Comment_Taxi> comments = comment_taxiRepository.findByTaxi(taxi);
                    for(Comment_Taxi comment : comments) {
                        comment.setState(ApplyState.FINISHED);
                        comment_taxiRepository.save(comment);
                    }
                }
            }

            // All lists
            List<ActiveEntity> allDeliveryList = deliveryRepository.findByUser(user).stream()
                    .filter(deliveryResponse -> deliveryResponse.getState() != PostState.DELETED)
                    .map(DeliveryResponse::GetDeliveryDTO)
                    .collect(Collectors.toList());
            List<ActiveEntity> allCommentDeliveryList = comment_deliveryRepository.findByUser(user).stream()
                    .filter(commentDeliveryResponse -> commentDeliveryResponse.getState() != ApplyState.CANCELED)
                    .map(Comment_DeliveryResponse::GetCommentDeliveryDTO)
                    .collect(Collectors.toList());
            List<ActiveEntity> allTaxiList = taxiRepository.findByUser(user).stream()
                    .filter(taxiResponse -> taxiResponse.getState() != PostState.DELETED)
                    .map(TaxiResponse::GetTaxiDTO)
                    .collect(Collectors.toList());
            List<ActiveEntity> allCommentTaxiList = comment_taxiRepository.findByUser(user).stream()
                    .filter(commentTaxiResponse -> commentTaxiResponse.getState() != ApplyState.CANCELED)
                    .map(Comment_TaxiResponse::GetCommentTaxiDTO)
                    .collect(Collectors.toList());

            // Combine all lists into one
            List<ActiveEntity> allList = Stream.of(allDeliveryList, allCommentDeliveryList, allTaxiList, allCommentTaxiList)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            // Filter the list to include only the posts from the last 7 days
            List<ActiveEntity> recentList = allList.stream()
                    .filter(entity -> entity.getCreatedAt().isAfter(weekAgo))
                    .collect(Collectors.toList());

            // Sort the list
            recentList.sort(comparator);

            return recentList;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }





    // <API> 닉네임 재설정
    @Transactional
    public Boolean changeNickname(Long studentId, ChangeNicknameRequestDTO request) {
        // 400 - 데이터 없음
        if(request.getNickname().isBlank())
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 401 - 유저 존재 확인
        User user = userRepository.findByStudentId(studentId).orElseThrow(()
                -> new CustomException(ErrorCode.MEMBER_NOT_EXIST));

        // 409 - 닉네임 이미 존재
        if (userRepository.findByNickname(request.getNickname()).isPresent())
            throw new CustomException(ErrorCode.DUPLICATE);

        try {
            user.setNickname(request.getNickname());
            userRepository.save(user);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // 학번으로 유저의 정보를 가져오는 메서드
    public User findByStudentId(Long studentId) {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_EXIST));
    }
}
