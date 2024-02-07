package yiu.aisl.yiuservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.yiuservice.config.CustomUserDetails;
import yiu.aisl.yiuservice.domain.Delivery;
import yiu.aisl.yiuservice.dto.DeliveryRequest;
import yiu.aisl.yiuservice.dto.DeliveryResponse;
import yiu.aisl.yiuservice.repository.UserRepository;
import yiu.aisl.yiuservice.service.DeliveryService;
import yiu.aisl.yiuservice.service.TokenService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    // 전체 모집글 조회 [all]
    @GetMapping
    public ResponseEntity<List> getList() throws Exception {
        System.out.println("API - /delivery");
        return new ResponseEntity<List>(deliveryService.getList(), HttpStatus.OK);
    }

    // 배달모집글 상세조회 [all]
    @PostMapping(value = "/detail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<DeliveryResponse> getDetail(DeliveryRequest.DetailDTO request) throws Exception {
        System.out.println("API - /delivery/detail");
        return new ResponseEntity<DeliveryResponse>(deliveryService.getDetail(request), HttpStatus.OK);
    }

    // 배달모집글 작성 [writer]
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> create(@AuthenticationPrincipal CustomUserDetails user, DeliveryRequest.CreateDTO request) throws Exception {
        System.out.println("API - /delivery/create");
        return new ResponseEntity<Boolean>(deliveryService.create(user.getStudentId(), request), HttpStatus.OK);
    }

    // 배달모집글 수정 [writer]
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> update(@AuthenticationPrincipal CustomUserDetails user, DeliveryRequest.UpdateDTO request) throws Exception {
        System.out.println("API - /delivery/update");
        return new ResponseEntity<Boolean>(deliveryService.update(user.getStudentId(), request), HttpStatus.OK);
    }

    // 배달모집글 삭제 [writer]
    @PostMapping(value = "/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> delete(@AuthenticationPrincipal CustomUserDetails user, DeliveryRequest.dIdDTO request) throws Exception {
        System.out.println("API - /delivery/delete");
        return new ResponseEntity<Boolean>(deliveryService.delete(user.getStudentId(), request), HttpStatus.OK);
    }

    // 배달모집글 마감 [writer]
    @PostMapping(value = "/finish", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> finish(@AuthenticationPrincipal CustomUserDetails user, DeliveryRequest.dIdDTO request) throws Exception {
        System.out.println("API - /delivery/finish");
        return new ResponseEntity<Boolean>(deliveryService.finish(user.getStudentId(), request), HttpStatus.OK);
    }

    // 배달모집 신청 [applicant]
    @PostMapping(value = "/apply", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> apply(@AuthenticationPrincipal CustomUserDetails user, DeliveryRequest.ApplyDTO request) throws Exception {
        System.out.println("API - /delivery/apply");
        return new ResponseEntity<Boolean>(deliveryService.apply(user.getStudentId(), request), HttpStatus.OK);
    }

    // 배달모집 신청 취소 [applicant]
    @PostMapping(value = "/cancel", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> cancel(@AuthenticationPrincipal CustomUserDetails user, DeliveryRequest.dcIdDTO request) throws Exception {
        System.out.println("API - /delivery/cancel");
        return new ResponseEntity<Boolean>(deliveryService.cancel(user.getStudentId(), request), HttpStatus.OK);
    }

    // 배달모집 신청 수락 [writer]
    @PostMapping(value = "/accept", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> accept(@AuthenticationPrincipal CustomUserDetails user, DeliveryRequest.dcIdDTO request) throws Exception {
        System.out.println("API - /delivery/accept");
        return new ResponseEntity<Boolean>(deliveryService.accept(user.getStudentId(), request), HttpStatus.OK);
    }

    // 배달모집 신청 거부 [writer]
    @PostMapping(value = "/reject", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> reject(@AuthenticationPrincipal CustomUserDetails user, DeliveryRequest.dcIdDTO request) throws Exception {
        System.out.println("API - /delivery/reject");
        return new ResponseEntity<Boolean>(deliveryService.reject(user.getStudentId(), request), HttpStatus.OK);
    }
}
