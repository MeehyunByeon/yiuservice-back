package yiu.aisl.yiuservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.yiuservice.config.CustomUserDetails;
import yiu.aisl.yiuservice.dto.DeliveryRequest;
import yiu.aisl.yiuservice.dto.UserResponse;
import yiu.aisl.yiuservice.repository.UserRepository;
import yiu.aisl.yiuservice.service.MainService;
import yiu.aisl.yiuservice.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    // 내 정보 조회
    @PostMapping(value = "/mypage", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails user) throws Exception {
        return new ResponseEntity<UserResponse>(userService.getMyInfo(user.getStudentId()), HttpStatus.OK);
    }

    // 내 활성화 글 조회
    @GetMapping("/main")
    public ResponseEntity<Map<String, List<?>>> getActiveList() throws Exception {
        return new ResponseEntity<Map<String, List<?>>>(userService.getMyActiveList(), HttpStatus.OK);
    }

}
