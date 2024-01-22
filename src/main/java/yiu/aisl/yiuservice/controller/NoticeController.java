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
import yiu.aisl.yiuservice.dto.DeliveryRequest;
import yiu.aisl.yiuservice.dto.DeliveryResponse;
import yiu.aisl.yiuservice.dto.NoticeRequest;
import yiu.aisl.yiuservice.dto.NoticeResponse;
import yiu.aisl.yiuservice.service.DeliveryService;
import yiu.aisl.yiuservice.service.NoticeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    // 전체 공지사항 조회 [all]
    @GetMapping
    public ResponseEntity<List> getList() throws Exception {
        return new ResponseEntity<List>(noticeService.getList(), HttpStatus.OK);
    }

    // 공지사항 상세조회 [all]
    @PostMapping(value = "/detail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<NoticeResponse> getDetail(NoticeRequest.DetailDTO request) throws Exception {
        return new ResponseEntity<NoticeResponse>(noticeService.getDetail(request), HttpStatus.OK);
    }

    // 공지사항 작성 [writer]
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> create(@AuthenticationPrincipal CustomUserDetails user, NoticeRequest.CreateDTO request) throws Exception {
        return new ResponseEntity<Boolean>(noticeService.create(request), HttpStatus.OK);
    }

    // 공지사항 수정 [writer]
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> update(@AuthenticationPrincipal CustomUserDetails user, NoticeRequest.UpdateDTO request) throws Exception {
        return new ResponseEntity<Boolean>(noticeService.update(request), HttpStatus.OK);
    }

    // 공지사항 삭제 [writer]
    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> delete(@AuthenticationPrincipal CustomUserDetails user, NoticeRequest.noticeIdDTO request) throws Exception {
        return new ResponseEntity<Boolean>(noticeService.delete(request), HttpStatus.OK);
    }
}
