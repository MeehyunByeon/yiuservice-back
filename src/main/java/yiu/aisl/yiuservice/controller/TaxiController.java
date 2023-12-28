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
import yiu.aisl.yiuservice.dto.TaxiRequest;
import yiu.aisl.yiuservice.dto.TaxiResponse;
import yiu.aisl.yiuservice.repository.UserRepository;
import yiu.aisl.yiuservice.service.DeliveryService;
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
}
