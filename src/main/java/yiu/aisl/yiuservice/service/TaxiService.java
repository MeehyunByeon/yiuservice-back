package yiu.aisl.yiuservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.*;
import yiu.aisl.yiuservice.dto.DeliveryRequest;
import yiu.aisl.yiuservice.dto.DeliveryResponse;
import yiu.aisl.yiuservice.dto.TaxiRequest;
import yiu.aisl.yiuservice.dto.TaxiResponse;
import yiu.aisl.yiuservice.exception.CustomException;
import yiu.aisl.yiuservice.exception.ErrorCode;
import yiu.aisl.yiuservice.repository.*;
import yiu.aisl.yiuservice.security.TokenProvider;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaxiService {

    private final TaxiRepository taxiRepository;
    private final Comment_TaxiRepository comment_taxiRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    // 전체 배달모집글 조회 [all]
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

    // 배달모집글 상세조회 [all]
    public TaxiResponse getDetail(TaxiRequest.DetailDTO request) throws Exception {
        try {
            if(request.getTId().describeConstable().isEmpty()) throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
            else {
                Taxi taxi = taxiRepository.findBytId(request.getTId()).orElseThrow(() -> {
                    throw new CustomException(ErrorCode.NOT_EXIST);
                });
                TaxiResponse response = TaxiResponse.GetTaxiDTO(taxi);
                return response;
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }


    // 배달모집글 작성 [writer]
    @Transactional
    public Boolean create(Long studentId, TaxiRequest.CreateDTO request) throws Exception{

        // 400 - 데이터 없음
        if(request.getTitle() == null || request.getContents() == null || request.getDue() == null || request.getMax() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        User user = findByStudentId(studentId);

        try {
            Taxi taxi = Taxi.builder()
                    .user(user)
                    .title(request.getTitle())
                    .contents(request.getContents())
                    .due(request.getDue())
                    .start(request.getStart())
                    .startCode(request.getStartCode())
                    .end(request.getEnd())
                    .endCode(request.getEndCode())
                    .max(request.getMax())
                    .current(request.getCurrent())
                    .state(request.getState())
                    .build();
            taxiRepository.save(taxi);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    // 학번으로 유저의 정보를 가져오는 메서드
    public User findByStudentId(Long studentId) {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.VALID_NOT_STUDENT_ID));
    }

    // tId로 택시 모집 글 정보를 가져오는 메서드
    public Taxi findByTId(Long tId) {
        return taxiRepository.findBytId(tId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글"));
    }

    // tcId로 택시 신청 정보를 가져오는 메서드
    public Comment_Taxi findByTcId(Long tcId) {
        return comment_taxiRepository.findByTcId(tcId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청"));
    }
}
