package yiu.aisl.yiuservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.Notice;
import yiu.aisl.yiuservice.domain.Notice;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.domain.state.ApplyState;
import yiu.aisl.yiuservice.domain.state.PostState;
import yiu.aisl.yiuservice.dto.NoticeRequest;
import yiu.aisl.yiuservice.dto.NoticeResponse;
import yiu.aisl.yiuservice.dto.NoticeRequest;
import yiu.aisl.yiuservice.dto.NoticeResponse;
import yiu.aisl.yiuservice.exception.CustomException;
import yiu.aisl.yiuservice.exception.ErrorCode;
import yiu.aisl.yiuservice.repository.NoticeRepository;
import yiu.aisl.yiuservice.repository.NoticeRepository;
import yiu.aisl.yiuservice.repository.UserRepository;
import yiu.aisl.yiuservice.security.TokenProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

//    // 전체 공지사항 조회 [all]
//    @Transactional
//    public List<NoticeResponse> getList() throws Exception {
//        List<Notice> notice = noticeRepository.findAll();
//        List<NoticeResponse> getListDTO = new ArrayList<>();
//        notice.forEach(s -> getListDTO.add(NoticeResponse.GetNoticeDTO(s)));
//        return getListDTO;
//    }
//
//    // 공지사항 상세조회 [all]
//    public NoticeResponse getDetail(NoticeRequest.DetailDTO request) throws Exception {
//        // 400 - 데이터 없음
//        if(request.getNoticeId().describeConstable().isEmpty()) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
//
//        // 404 - 글 존재하지 않음
//        Notice notice = noticeRepository.findByNoticeId(request.getNoticeId()).orElseThrow(() -> {
//            throw new CustomException(ErrorCode.NOT_EXIST);
//        });
//
//        try {
//            NoticeResponse response = NoticeResponse.GetNoticeDetailDTO(notice);
//            return response;
//        } catch (Exception e) {
//            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//
//
//    // 공지사항 작성 [writer]
//    @Transactional
//    public Boolean create(Long studentId, NoticeRequest.CreateDTO request) throws Exception{
//        // 400 - 데이터 없음
//        if(request.getTitle() == null || request.getContents() == null)
//            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
//
//        if(!studentId.equals(202033013))
//
//            throw new CustomException(ErrorCode.ACCESS_NO_AUTH);
//
//        try {
//            // 유저 확인 404 포함
//            User user = findByStudentId(studentId);
//
//            Notice Notice = Notice.builder()
//                    .user(user)
//                    .title(request.getTitle())
//                    .contents(request.getContents())
//                    .due(request.getDue())
//                    .food(request.getFood())
//                    .foodCode(request.getFoodCode())
//                    .location(request.getLocation())
//                    .locationCode(request.getLocationCode())
//                    .link(request.getLink())
//                    .state(request.getState())
//                    .build();
//            NoticeRepository.save(Notice);
//        }
//        catch (Exception e) {
//            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
//        }
//        return true;
//    }
//
//    // 공지사항 수정 [writer]
//    @Transactional
//    public Boolean update(Long studentId, NoticeRequest.UpdateDTO request) throws Exception{
//        // 유저 확인 404 포함
//        User user = findByStudentId(studentId);
//
//        // 400 - 데이터 없음
//        if(request.getDId() == null || request.getTitle() == null || request.getContents() == null || request.getDue() == null)
//            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
//
//        // 400 - 음식, 위치
//        if((request.getFood() == null || request.getFood().isEmpty()) && request.getFoodCode() == null)
//            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
//        // 400 - 위치
//        if((request.getLocation() == null || request.getLocation().isEmpty()) && request.getLocationCode() == null)
//            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
//
//        Optional<Notice> optNotice = NoticeRepository.findBydId(request.getDId());
//
//        // 404 - 글이 존재하지 않음
//        if(optNotice.isEmpty()) {
//            throw new CustomException(ErrorCode.NOT_EXIST);
//        }
//
//        // 403 - 권한 없음
//        Notice existingNotice = optNotice.get();
//        if(!existingNotice.getUser().equals(user)) {
//            throw new CustomException(ErrorCode.ACCESS_NO_AUTH);
//        }
//
//        try {
//            existingNotice.setTitle(request.getTitle());
//            existingNotice.setContents(request.getContents());
//            existingNotice.setDue(request.getDue());
//            existingNotice.setFood(request.getFood());
//            existingNotice.setFoodCode(request.getFoodCode());
//            existingNotice.setLocation(request.getLocation());
//            existingNotice.setLocationCode(request.getLocationCode());
//            existingNotice.setLink(request.getLink());
//            existingNotice.setState(request.getPostState());
//            NoticeRepository.save(existingNotice);
//        }
//        catch (Exception e) {
//            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
//        }
//        return true;
//    }
//
//    // 공지사항 삭제 [writer]
//    @Transactional
//    public Boolean delete(Long studentId, NoticeRequest.dIdDTO request) throws Exception{
//        // 400 - 데이터 없음
//        if(request.getDId() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
//
//        // 유저 확인 404 포함
//        User user = findByStudentId(studentId);
//
//        Notice Notice = findByDId(request.getDId());
//        Optional<Notice> optNotice = NoticeRepository.findBydId(request.getDId());
//        Optional<Comment_Notice> optCommentNotice = comment_NoticeRepository.findByDcId(request.getDId());
//        List<Comment_Notice> listCommentNotice = comment_NoticeRepository.findByNotice(Notice);
//
//        // 404 - 글 존재하지 않음
//        if(optNotice.isEmpty()) throw new CustomException(ErrorCode.NOT_EXIST);
//
//        // 403 - 권한 없음(작성인 != 삭제요청인)
//        if(!optNotice.get().getUser().equals(user)) throw new CustomException(ErrorCode.ACCESS_NO_AUTH);
//
//        // 409 - 아직 진행 중인 신청이 있으면 임의로 삭제 안됨
//        if(listCommentNotice.stream().anyMatch(s -> !(s.getState().equals(ApplyState.CANCELED) || s.getState().equals(ApplyState.REJECTED))))
//            throw new CustomException(ErrorCode.CONFLICT);
//
//        try {
//            Notice existingNotice = optNotice.get();
//            existingNotice.setState(PostState.DELETED);
//            NoticeRepository.save(existingNotice);
//
//            return true;
//        }
//        catch (Exception e) {
//            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
//        }
//    }

    // 학번으로 유저의 정보를 가져오는 메서드
    public User findByStudentId(Long studentId) {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_EXIST));
    }

    // noticeId로 공지사항 글 정보를 가져오는 메서드
    public Notice findBynNoticeId(Long dId) {
        return noticeRepository.findByNoticeId(dId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
    }
}
