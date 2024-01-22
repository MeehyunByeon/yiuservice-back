package yiu.aisl.yiuservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.Notice;
import yiu.aisl.yiuservice.domain.Report;
import yiu.aisl.yiuservice.dto.NoticeResponse;
import yiu.aisl.yiuservice.dto.ReportResponse;
import yiu.aisl.yiuservice.repository.ReportRepository;
import yiu.aisl.yiuservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    // 전체 신고 조회 [all]
//    @Transactional
//    public List<ReportResponse> getList() throws Exception {
//        List<Report> report = reportRepository.findAll();
//        List<ReportResponse> getListDTO = new ArrayList<>();
//        report.forEach(s -> getListDTO.add(ReportResponse.GetReportDTO(s)));
//        return getListDTO;
//    }
}
