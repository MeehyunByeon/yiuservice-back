package yiu.aisl.yiuservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.yiuservice.config.CustomUserDetails;
import yiu.aisl.yiuservice.dto.NoticeRequest;
import yiu.aisl.yiuservice.dto.ReportRequest;
import yiu.aisl.yiuservice.service.NoticeService;
import yiu.aisl.yiuservice.service.ReportService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    // 전체 신고 조회 [admin]
    @GetMapping
    public ResponseEntity<List> getList() throws Exception {
        return new ResponseEntity<List>(reportService.getList(), HttpStatus.OK);
    }

    // 신고 작성 [user]
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> create(@AuthenticationPrincipal CustomUserDetails user, ReportRequest.CreateDTO request) throws Exception {
        return new ResponseEntity<Boolean>(reportService.create(user.getStudentId(), request), HttpStatus.OK);
    }
}
