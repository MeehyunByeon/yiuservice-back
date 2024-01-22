package yiu.aisl.yiuservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.*;
import yiu.aisl.yiuservice.domain.state.EntityCode;
import yiu.aisl.yiuservice.dto.NoticeRequest;
import yiu.aisl.yiuservice.dto.NoticeResponse;
import yiu.aisl.yiuservice.dto.ReportRequest;
import yiu.aisl.yiuservice.dto.ReportResponse;
import yiu.aisl.yiuservice.exception.CustomException;
import yiu.aisl.yiuservice.exception.ErrorCode;
import yiu.aisl.yiuservice.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    private final DeliveryRepository deliveryRepository;
    private final Comment_DeliveryRepository comment_deliveryRepository;
    private final TaxiRepository taxiRepository;
    private final Comment_TaxiRepository comment_taxiRepository;


    // 전체 신고 조회 [admin]
    @Transactional
    public List<ReportResponse> getList() throws Exception {
        List<Report> report = reportRepository.findAllByOrderByCreatedAtDesc();
        List<ReportResponse> getListDTO = new ArrayList<>();
        report.forEach(s -> getListDTO.add(ReportResponse.GetReportDTO(s)));
        return getListDTO;
    }

    // 신고 작성 [user]
    @Transactional
    public Boolean create(Long studentId, ReportRequest.CreateDTO request) throws Exception{

        // 400 - 데이터 없음
        if(request.getToId() == null
                || request.getContents() == null
                || request.getType() == null
                || request.getId() == null)
            throw new CustomException(ErrorCode.INSUFFICIENT_DATA);

        User fromId = findByStudentId(studentId);
        User toId = findByStudentId(request.getToId());

        // 409 - 자기 자신 신고
        if(fromId.equals(toId)) throw new CustomException(ErrorCode.CONFLICT);

        // 409 - 중복 신고 확인
        Optional<Report> existingReport = reportRepository.findByFromIdAndToIdAndTypeAndId(fromId, toId, request.getType(), request.getId());
        if (existingReport.isPresent()) throw new CustomException(ErrorCode.DUPLICATE);

        try {
            Report report = Report.builder()
                    .fromId(fromId)
                    .toId(toId)
                    .contents(request.getContents())
                    .type(request.getType())
                    .id(request.getId())
                    .build();
            reportRepository.save(report);
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
