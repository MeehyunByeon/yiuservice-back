package yiu.aisl.yiuservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.yiuservice.dto.UserJoinRequestDto;
import yiu.aisl.yiuservice.dto.UserResponse;
import yiu.aisl.yiuservice.repository.UserRepository;
import yiu.aisl.yiuservice.service.UserService;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping(value = "/join")
    public ResponseEntity<Boolean> join(@RequestBody UserJoinRequestDto request) throws Exception {
        return new ResponseEntity<>(userService.join(request), HttpStatus.OK);
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
