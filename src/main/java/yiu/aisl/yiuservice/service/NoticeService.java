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

    // 전체 공지사항 조회 [all]
    @Transactional
    public List<NoticeResponse> getList() throws Exception {
        List<Notice> notice = noticeRepository.findAllByOrderByCreatedAtDesc();
        List<NoticeResponse> getListDTO = new ArrayList<>();
        notice.forEach(s -> getListDTO.add(NoticeResponse.GetNoticeDTO(s)));
        return getListDTO;
    }

    // 공지사항 상세조회 [all]
    public NoticeResponse getDetail(NoticeRequest.DetailDTO request) throws Exception {
        // 400 - 데이터 없음
        if(request.getNoticeId().describeConstable().isEmpty()) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 404 - 글 존재하지 않음
        Notice notice = findBynNoticeId(request.getNoticeId());

        try {
            NoticeResponse response = NoticeResponse.GetNoticeDetailDTO(notice);
            return response;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }


    // 공지사항 작성 [admin]
    @Transactional
    public Boolean create(NoticeRequest.CreateDTO request) throws Exception{
        // 400 - 데이터 없음
        if(request.getTitle() == null || request.getContents() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        try {
            Notice notice = Notice.builder()
                    .title(request.getTitle())
                    .contents(request.getContents())
                    .build();
            noticeRepository.save(notice);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // 공지사항 수정 [admin]
    @Transactional
    public Boolean update(NoticeRequest.UpdateDTO request) throws Exception{

        // 400 - 데이터 없음
        if(request.getNoticeId() == null || request.getTitle() == null || request.getContents() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 404 - 글 존재하지 않음
        Notice notice = findBynNoticeId(request.getNoticeId());


        try {
            Optional<Notice> optNotice = noticeRepository.findByNoticeId(request.getNoticeId());
            Notice existingNotice = optNotice.get();

            existingNotice.setTitle(request.getTitle());
            existingNotice.setContents(request.getContents());
            noticeRepository.save(existingNotice);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // 공지사항 삭제 [admin]
    @Transactional
    public Boolean delete(NoticeRequest.noticeIdDTO request) throws Exception{

        // 400 - 데이터 없음
        if(request.getNoticeId() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 404 - 글 존재하지 않음
        Notice notice = findBynNoticeId(request.getNoticeId());

        try {
            noticeRepository.deleteById(request.getNoticeId());
            return true;
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // noticeId로 공지사항 글 정보를 가져오는 메서드
    public Notice findBynNoticeId(Long noticeId) {
        return noticeRepository.findByNoticeId(noticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
    }
}
