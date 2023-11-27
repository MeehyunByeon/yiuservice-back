package yiu.aisl.yiuservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.yiuservice.config.CustomUserDetails;
import yiu.aisl.yiuservice.dto.DeliveryRequest;
import yiu.aisl.yiuservice.repository.UserRepository;
import yiu.aisl.yiuservice.service.DeliveryService;
import yiu.aisl.yiuservice.service.TokenService;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @PostMapping(value = "/delivery/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Boolean> create(@AuthenticationPrincipal CustomUserDetails user, DeliveryRequest.CreateDTO request) throws Exception {
        System.out.println("controller: " + user.getStudentId());
        return new ResponseEntity<Boolean>(deliveryService.create(user.getStudentId(), request), HttpStatus.OK);
    }
}
