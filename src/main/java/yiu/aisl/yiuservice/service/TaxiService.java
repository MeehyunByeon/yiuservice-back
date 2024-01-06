package yiu.aisl.yiuservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.*;
import yiu.aisl.yiuservice.domain.state.ApplyState;
import yiu.aisl.yiuservice.domain.state.PostState;
import yiu.aisl.yiuservice.dto.TaxiRequest;
import yiu.aisl.yiuservice.dto.TaxiResponse;
import yiu.aisl.yiuservice.dto.TaxiRequest;
import yiu.aisl.yiuservice.dto.TaxiResponse;
import yiu.aisl.yiuservice.exception.CustomException;
import yiu.aisl.yiuservice.exception.ErrorCode;
import yiu.aisl.yiuservice.repository.*;
import yiu.aisl.yiuservice.security.TokenProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaxiService {

    private final TaxiRepository taxiRepository;
    private final Comment_TaxiRepository comment_taxiRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    // 전체 택시모집글 조회 [all]
    @Transactional
    public List<TaxiResponse> getList() throws Exception {
        try {
            List<Taxi> taxi = taxiRepository.findAll();
            List<TaxiResponse> getListDTO = new ArrayList<>();
            taxi.forEach(s -> getListDTO.add(TaxiResponse.GetTaxiDTO(s)));
            return getListDTO;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 택시모집글 상세조회 [all]
    public TaxiResponse getDetail(TaxiRequest.DetailDTO request) throws Exception {
        // 400 - 데이터 없음
        if(request.getTId().describeConstable().isEmpty()) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 404 - 글 존재하지 않음
        Taxi taxi = taxiRepository.findBytId(request.getTId()).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_EXIST);
        });

        // 409 - 삭제된 글
        if(taxi.getState().equals(PostState.DELETED)) throw new CustomException(ErrorCode.CONFLICT);

        try {
            TaxiResponse response = TaxiResponse.GetTaxiDetailDTO(taxi);
            return response;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }


    // 택시모집글 작성 [writer]
    @Transactional
    public Boolean create(Long studentId, TaxiRequest.CreateDTO request) throws Exception{
        // 400 - 데이터 없음
        if(request.getTitle() == null || request.getContents() == null || request.getDue() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 400 - 위치에 대한 설정 => start와 startCode 둘 다 null인 상황은 안됨
        if(request.getStart() == null && request.getStartCode() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        try {
            // 유저 확인 404 포함
            User user = findByStudentId(studentId);

            Taxi taxi = Taxi.builder()
                    .user(user)
                    .title(request.getTitle())
                    .contents(request.getContents())
                    .due(request.getDue())
                    .current(request.getCurrent())
                    .max(request.getMax())
                    .start(request.getStart())
                    .startCode(request.getStartCode())
                    .end(request.getEnd())
                    .endCode(request.getEndCode())
                    .state(request.getState())
                    .build();
            taxiRepository.save(taxi);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // 택시모집글 수정 [writer]
    @Transactional
    public Boolean update(Long studentId, TaxiRequest.UpdateDTO request) throws Exception{
        // 유저 확인 404 포함
        User user = findByStudentId(studentId);

        // 400 - 데이터 없음
        if(request.getTId() == null || request.getTitle() == null || request.getContents() == null || request.getDue() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 400 - 위치에 대한 설정 => start와 startCode 둘 다 null인 상황은 안됨
        if(request.getStart() == null && request.getStartCode() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        Optional<Taxi> optTaxi = taxiRepository.findBytId(request.getTId());

        // 404 - 글이 존재하지 않음
        if(optTaxi.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_EXIST);
        }

        // 403 - 권한 없음
        Taxi existingTaxi = optTaxi.get();
        if(!existingTaxi.getUser().equals(user)) {
            throw new CustomException(ErrorCode.ACCESS_NO_AUTH);
        }

        try {
            existingTaxi.setTitle(request.getTitle());
            existingTaxi.setContents(request.getContents());
            existingTaxi.setDue(request.getDue());
            existingTaxi.setStart(request.getStart());
            existingTaxi.setStartCode(request.getStartCode());
            existingTaxi.setEnd(request.getEnd());
            existingTaxi.setEndCode(request.getEndCode());
            existingTaxi.setState(request.getPostState());
            taxiRepository.save(existingTaxi);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // 택시모집글 삭제 [writer]
    @Transactional
    public Boolean delete(Long studentId, TaxiRequest.tIdDTO request) throws Exception{
        // 400 - 데이터 없음
        if(request.getTId() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 유저 확인 404 포함
        User user = findByStudentId(studentId);

        Taxi taxi = findBytId(request.getTId());
        Optional<Taxi> optTaxi = taxiRepository.findBytId(request.getTId());
        Optional<Comment_Taxi> optCommentTaxi = comment_taxiRepository.findByTcId(request.getTId());
        List<Comment_Taxi> listCommentTaxi = comment_taxiRepository.findByTaxi(taxi);

        // 404 - 글 존재하지 않음
        if(optTaxi.isEmpty()) throw new CustomException(ErrorCode.NOT_EXIST);

        // 403 - 권한 없음(작성인 != 삭제요청인)
        if(!optTaxi.get().getUser().equals(user)) throw new CustomException(ErrorCode.ACCESS_NO_AUTH);

        // 409 - 아직 진행 중인 신청이 있으면 임의로 삭제 안됨
        if(listCommentTaxi.stream().anyMatch(s -> !(s.getState().equals(ApplyState.CANCELED) || s.getState().equals(ApplyState.REJECTED))))
            throw new CustomException(ErrorCode.CONFLICT);

        try {
            Taxi existingTaxi = optTaxi.get();
            existingTaxi.setState(PostState.DELETED);
            taxiRepository.save(existingTaxi);
            return true;
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 택시모집글 마감 [writer]
    @Transactional
    public Boolean finish(Long studentId, TaxiRequest.tIdDTO request) throws Exception{
        // 400 - 데이터 없음
        if(request.getTId() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 유저 확인 404 포함
        User user = findByStudentId(studentId);
        Optional<Taxi> optTaxi = taxiRepository.findBytId(request.getTId());

        // 404 - 글 존재하지 않음
        if(optTaxi.isEmpty()) throw new CustomException(ErrorCode.NOT_EXIST);

        // 403 - 권한 없음(작성인 != 마감요청인)
        if(!optTaxi.get().getUser().equals(user)) throw new CustomException(ErrorCode.ACCESS_NO_AUTH);

        // 409 - 이미 글이 마감된 상태 or 삭제된 상태
        if(optTaxi.get().getState().equals(PostState.FINISHED) || optTaxi.get().getState().equals(PostState.DELETED)) throw new CustomException(ErrorCode.CONFLICT);

        try {
            Taxi taxi = optTaxi.get();
            taxi.setState(PostState.FINISHED);
            taxiRepository.save(taxi);

            // ### 마감할 때 신청글을 모두 FINISHED 처리 ###

            return true;
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 택시모집 신청 [applicant]
    @Transactional
    public Boolean apply(Long studentId, TaxiRequest.ApplyDTO request) throws Exception {
        // 400 - 데이터 없음
        if(request.getTId() == null || request.getContents() == null || request.getNumber() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 유저 확인 404 포함
        User user = findByStudentId(studentId);
        Taxi taxi = findBytId(request.getTId());
        List<Comment_Taxi> listCommentTaxi = comment_taxiRepository.findByUserAndTaxi(user, taxi);

        // 404 - 글 state가 DELETED OR FINISHED
        if(taxi.getState().equals(PostState.DELETED) || taxi.getState().equals(PostState.FINISHED))
            throw new CustomException(ErrorCode.NOT_EXIST);

        // 403 - 권한 없음 => 자신의 글에 신청한 경우(작성인 == 신청인)
        if(taxi.getUser().equals(user))
            throw new CustomException(ErrorCode.ACCESS_NO_AUTH);

        // 신청 전적이 있는 상태에서
        if(!listCommentTaxi.isEmpty()) {
            // 409 - 신청 => 대기 상태 OR 수락 상태가 1개라도 있으면
            if(listCommentTaxi.stream().anyMatch(s -> s.getState().equals(ApplyState.WAITING) || s.getState().equals(ApplyState.ACCEPTED)))
                throw new CustomException(ErrorCode.CONFLICT);
        }

        try {
            Comment_Taxi comment = Comment_Taxi.builder()
                    .user(user)
                    .taxi(taxi)
                    .contents(request.getContents())
                    .details(request.getDetails())
                    .number(request.getNumber())
                    .state(request.getState())
                    .build();
            comment_taxiRepository.save(comment);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // 택시모집 신청 취소 [applicant]
    public Boolean cancel(Long studentId, TaxiRequest.tcIdDTO request) throws Exception {
        // 400 - 데이터 없음
        if(request.getTcId() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 유저 확인 404 포함
        User user = findByStudentId(studentId);
        Optional<Comment_Taxi> optComment_Taxi = comment_taxiRepository.findByTcId(request.getTcId());

        // 404 - 해당 신청 글 없음
        if(optComment_Taxi.isEmpty()) throw new CustomException(ErrorCode.NOT_EXIST);

        // 403 - 권한 없음(신청자 != 신청글 삭제 요청인)
        if(!optComment_Taxi.get().getUser().equals(user)) throw new CustomException(ErrorCode.ACCESS_NO_AUTH);

        // 409 - 취소 불가 => 대기 상태가 아닌 모든 경우
        if(!optComment_Taxi.get().getState().equals(ApplyState.WAITING))
            throw new CustomException(ErrorCode.CONFLICT);

        try {
            Comment_Taxi comment_Taxi = optComment_Taxi.get();
            comment_Taxi.setState(ApplyState.CANCELED);
            comment_taxiRepository.save(comment_Taxi);
            return true;
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 택시모집 신청 수락 [writer]
    public Boolean accept(Long studentId, TaxiRequest.tcIdDTO request) throws Exception {
        // 400 - 데이터 없음
        if(request.getTcId() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        // 유저 확인 404 포함
        User user = findByStudentId(studentId);
        Optional<Comment_Taxi> optComment_Taxi = comment_taxiRepository.findByTcId(request.getTcId());

        // 택시글 404 포함
        Optional<Taxi> optTaxi = taxiRepository.findBytId(optComment_Taxi.get().getTaxi().getTId());

        // 403 - 신청 수락 권한 없음
        if(!optTaxi.get().getUser().equals(user)) throw new CustomException(ErrorCode.ACCESS_NO_AUTH);

        // 409 - 글이 활성화 상태가 아님 OR 신청이 대기 상태가 아님(수락, 삭제, 거절 등)
        if(!optTaxi.get().getState().equals(PostState.ACTIVE) || !optComment_Taxi.get().getState().equals(ApplyState.WAITING))
            throw new CustomException(ErrorCode.CONFLICT);

        // 409 - max 초과
        if(optTaxi.get().getMax() < optTaxi.get().getCurrent() + optComment_Taxi.get().getNumber())
            throw new CustomException(ErrorCode.EXCESS);

        try {
            Comment_Taxi comment_Taxi = optComment_Taxi.get();
            comment_Taxi.setState(ApplyState.ACCEPTED);
            comment_taxiRepository.save(comment_Taxi);
            return true;
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 택시모집 신청 거부 [writer]
    public Boolean reject(Long studentId, TaxiRequest.tcIdDTO request) throws Exception {
        // 400 - 데이터 없음
        if(request.getTcId() == null) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        Optional<Comment_Taxi> optComment_Taxi = comment_taxiRepository.findByTcId(request.getTcId());

        // 유저 확인 404 포함
        User user = findByStudentId(studentId);

        // 택시글 404 포함
        Taxi taxi = findBytId(optComment_Taxi.get().getTaxi().getTId());

        // 403 - 거절 수락 권한 없음
        if(!taxi.getUser().equals(user)) throw new CustomException(ErrorCode.ACCESS_NO_AUTH);

        // 409 - 글이 활성화 상태가 아님 OR 신청이 대기 상태가 아님(수락, 삭제, 거절 등)
        if(!taxi.getState().equals(PostState.ACTIVE) || !optComment_Taxi.get().getState().equals(ApplyState.WAITING))
            throw new CustomException(ErrorCode.CONFLICT);

        try {
            Comment_Taxi comment_Taxi = optComment_Taxi.get();
            comment_Taxi.setState(ApplyState.REJECTED);
            comment_taxiRepository.save(comment_Taxi);
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

    // tId로 택시 모집 글 정보를 가져오는 메서드
    public Taxi findBytId(Long tId) {
        return taxiRepository.findBytId(tId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
    }

    // tcId로 택시 신청 정보를 가져오는 메서드
    public Comment_Taxi findByTcId(Long tcId) {
        return comment_taxiRepository.findByTcId(tcId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
    }
}
