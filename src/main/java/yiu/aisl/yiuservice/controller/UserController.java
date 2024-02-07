package yiu.aisl.yiuservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.yiuservice.config.CustomUserDetails;
import yiu.aisl.yiuservice.domain.ActiveEntity;
import yiu.aisl.yiuservice.dto.*;
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
    @GetMapping(value = "/mypage")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails user) throws Exception {
        System.out.println("API - /user/mypage");
        return new ResponseEntity<UserResponse>(userService.getMyInfo(user.getStudentId()), HttpStatus.OK);
    }

    // 내 활성화 글 조회
    @GetMapping("/active")
    public ResponseEntity<List <ActiveEntity>> getActiveList(@AuthenticationPrincipal CustomUserDetails user) throws Exception {
        System.out.println("API - /user/active");
        return new ResponseEntity<List <ActiveEntity>>(userService.getMyActiveList(user.getStudentId()), HttpStatus.OK);
    }

    // 내 모든 글 조회
    @GetMapping("/post")
    public ResponseEntity<List <ActiveEntity>> getMyAllPostList(@AuthenticationPrincipal CustomUserDetails user) throws Exception {
        System.out.println("API - /user/post");
        return new ResponseEntity<List <ActiveEntity>>(userService.getMyAllPostList(user.getStudentId()), HttpStatus.OK);
    }

    // 닉네임 변경
    @PostMapping(value = "/changenick", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> changeNickname(@AuthenticationPrincipal CustomUserDetails user, ChangeNicknameRequestDTO request) throws Exception {
        System.out.println("API - /user/changenick");
        return new ResponseEntity<Boolean>(userService.changeNickname(user.getStudentId(), request), HttpStatus.OK);
    }

}
