package yiu.aisl.yiuservice.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.yiuservice.dto.*;
import yiu.aisl.yiuservice.repository.UserRepository;
import yiu.aisl.yiuservice.service.TokenService;
import yiu.aisl.yiuservice.service.UserService;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @PostMapping(value = "/join", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> join(UserJoinRequestDto request) throws Exception {
//        System.out.println("join: " + request.getStudentId());
        return new ResponseEntity<Boolean>(userService.join(request), HttpStatus.OK);
    }

    @PostMapping(value = "/mail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> sendMail(SendEmailRequestDTO request) throws MessagingException, UnsupportedEncodingException {
//        System.out.println("받은 mail: " + request.getEmail());
        return new ResponseEntity<String>(userService.sendEmail(request.getEmail()), HttpStatus.OK);
    }

    @PostMapping(value = "/nickcheck", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> checkNickname (CheckNicknameRequestDTO request) throws Exception {
//        System.out.println("nickcheck: " + request.getNickname());
        return new ResponseEntity<Boolean>(userService.checkNickname(request), HttpStatus.OK);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<UserLoginResponseDto> login(UserLoginRequestDto request) throws Exception {
//        System.out.println("login: " + request.getStudentId() + request.getPwd());
        return new ResponseEntity<UserLoginResponseDto>(userService.login(request), HttpStatus.OK);
    }

    @GetMapping(value = "/logout", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Boolean logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return true;
    }

    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(CreateAccessTokenRequest request) throws Exception {
        System.out.println("/refresh - access: " + request.getAccessToken());
        System.out.println("/refresh - refresh: " + request.getRefreshToken());

//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new CreateAccessTokenResponse(newAccessToken));
        return new ResponseEntity<CreateAccessTokenResponse>(tokenService.createNewAccessToken(request), HttpStatus.OK);
    }

//    public ResponseEntity<Boolean> join(@RequestBody UserJoinRequestDto request) throws Exception {
//        return new ResponseEntity<>(userService.join(request), HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/login")
//    public ResponseEntity<UserResponse> login(@RequestBody UserJoinRequestDto request) throws Exception {
//        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
//    }
//
//    @GetMapping("/refresh")
//    public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto token) throws Exception {
//        return new ResponseEntity<>(userService.refreshAccessToken(token), HttpStatus.OK);
//    }
}
