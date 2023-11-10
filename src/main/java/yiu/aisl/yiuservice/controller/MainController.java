package yiu.aisl.yiuservice.controller;

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

@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final UserRepository userRepository;
    private TokenService tokenService;

    @PostMapping(value = "/join", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<Boolean> join(@RequestBody UserJoinRequestDto request) throws Exception {
        return new ResponseEntity<>(userService.join(request), HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto request) throws Exception {
        return new ResponseEntity<UserLoginResponseDto>(userService.login(request), HttpStatus.OK);
    }

    @GetMapping(value = "/logout")
    public Boolean logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return true;
    }

    @PostMapping(value = "/refresh")
    @ResponseBody
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
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
