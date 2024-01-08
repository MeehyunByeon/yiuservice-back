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
import yiu.aisl.yiuservice.dto.TaxiRequest;
import yiu.aisl.yiuservice.dto.TaxiResponse;
import yiu.aisl.yiuservice.repository.UserRepository;
import yiu.aisl.yiuservice.service.TaxiService;
import yiu.aisl.yiuservice.service.TokenService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/taxi")
public class TaxiController {
    private final TaxiService taxiService;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    // 전체 택시모집글 조회 [all]
    @GetMapping
    public ResponseEntity<List> getList() throws Exception {
        return new ResponseEntity<List>(taxiService.getList(), HttpStatus.OK);
    }

    // 택시모집글 상세조회 [all]
    @PostMapping(value = "/detail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<TaxiResponse> getDetail(TaxiRequest.DetailDTO request) throws Exception {
        return new ResponseEntity<TaxiResponse>(taxiService.getDetail(request), HttpStatus.OK);
    }

    // 택시모집글 작성 [writer]
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> create(@AuthenticationPrincipal CustomUserDetails user, TaxiRequest.CreateDTO request) throws Exception {

        return new ResponseEntity<Boolean>(taxiService.create(user.getStudentId(), request), HttpStatus.OK);
    }

    // 택시모집글 수정 [writer]
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> update(@AuthenticationPrincipal CustomUserDetails user, TaxiRequest.UpdateDTO request) throws Exception {
        return new ResponseEntity<Boolean>(taxiService.update(user.getStudentId(), request), HttpStatus.OK);
    }

    // 택시모집글 삭제 [writer]
    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> delete(@AuthenticationPrincipal CustomUserDetails user, TaxiRequest.tIdDTO request) throws Exception {
        return new ResponseEntity<Boolean>(taxiService.delete(user.getStudentId(), request), HttpStatus.OK);
    }

    // 택시모집글 마감 [writer]
    @PostMapping(value = "/finish", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> finish(@AuthenticationPrincipal CustomUserDetails user, TaxiRequest.tIdDTO request) throws Exception {
        return new ResponseEntity<Boolean>(taxiService.finish(user.getStudentId(), request), HttpStatus.OK);
    }

    // 택시모집 신청 [applicant]
    @PostMapping(value = "/apply", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> apply(@AuthenticationPrincipal CustomUserDetails user, TaxiRequest.ApplyDTO request) throws Exception {
        return new ResponseEntity<Boolean>(taxiService.apply(user.getStudentId(), request), HttpStatus.OK);
    }

    // 택시모집 신청 취소 [applicant]
    @PostMapping(value = "/cancel", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> cancel(@AuthenticationPrincipal CustomUserDetails user, TaxiRequest.tcIdDTO request) throws Exception {
        return new ResponseEntity<Boolean>(taxiService.cancel(user.getStudentId(), request), HttpStatus.OK);
    }

    // 택시모집 신청 수락 [writer]
    @PostMapping(value = "/accept", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> accept(@AuthenticationPrincipal CustomUserDetails user, TaxiRequest.tcIdDTO request) throws Exception {
        return new ResponseEntity<Boolean>(taxiService.accept(user.getStudentId(), request), HttpStatus.OK);
    }

    // 택시모집 신청 거부 [writer]
    @PostMapping(value = "/reject", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> reject(@AuthenticationPrincipal CustomUserDetails user, TaxiRequest.tcIdDTO request) throws Exception {
        return new ResponseEntity<Boolean>(taxiService.reject(user.getStudentId(), request), HttpStatus.OK);
    }
}
