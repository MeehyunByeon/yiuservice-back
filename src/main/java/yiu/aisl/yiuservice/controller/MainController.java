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
import yiu.aisl.yiuservice.security.TokenProvider;
import yiu.aisl.yiuservice.service.TokenService;
import yiu.aisl.yiuservice.service.MainService;

import java.io.UnsupportedEncodingException;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;

    @GetMapping("/test")
    public void test() throws Exception{
        System.out.println("push test API");
        mainService.pushTest();
    }

    @GetMapping("/main")
    public ResponseEntity<Map<String, List<?>>> getList() throws Exception {
        System.out.println("API - /main");
        return new ResponseEntity<Map<String, List<?>>>(mainService.getList(), HttpStatus.OK);
    }

    @PostMapping(value = "/join", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> join(UserJoinRequestDto request) throws Exception {
        System.out.println("API - /join");
        return new ResponseEntity<Boolean>(mainService.join(request), HttpStatus.OK);
    }

    @PostMapping(value = "/mail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> sendMail(SendEmailRequestDTO request) throws MessagingException, UnsupportedEncodingException {
        System.out.println("API - /mail");
        return new ResponseEntity<String>(mainService.sendEmail(request.getEmail()), HttpStatus.OK);
    }

    @PostMapping(value = "/changepwd/mail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> sendMailWhenPwdChanges(SendEmailRequestDTO request) throws MessagingException, UnsupportedEncodingException {
        System.out.println("API - /changepwd/mail");
        return new ResponseEntity<String>(mainService.sendEmailWhenPwdChanges(request.getEmail()), HttpStatus.OK);
    }

    @PostMapping(value = "/nickcheck", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> checkNickname (CheckNicknameRequestDTO request) throws Exception {
        System.out.println("API - /nickcheck");
        return new ResponseEntity<Boolean>(mainService.checkNickname(request), HttpStatus.OK);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<UserLoginResponseDto> login(UserLoginRequestDto request) throws Exception {
        System.out.println("API - /login");
        return new ResponseEntity<UserLoginResponseDto>(mainService.login(request), HttpStatus.OK);
    }

    @GetMapping(value = "/logout", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Boolean logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
        System.out.println("API - /logout");
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return true;
    }

    @PostMapping(value = "/changepwd", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> changePwd(ChangePwdRequestDTO request) throws Exception {
        System.out.println("API - /changepwd");
        return new ResponseEntity<Boolean>(mainService.changePwd(request), HttpStatus.OK);
    }

    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<TokenDto> createNewAccessToken(TokenDto token) throws Exception {
        System.out.println("API - /refresh");
        return new ResponseEntity<>(mainService.refreshAccessToken(token), HttpStatus.OK);
    }

    @GetMapping("/api/jwt/info/{token}")
    public TokenProvider.TokenInfo jwtInfo(@PathVariable String token) {
        TokenProvider.TokenInfo tokenInfo = tokenProvider.getTokenInfo(token);
        return tokenInfo;
    }
}
