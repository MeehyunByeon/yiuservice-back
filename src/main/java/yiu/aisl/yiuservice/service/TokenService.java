package yiu.aisl.yiuservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.config.jwt.TokenProvider;
import yiu.aisl.yiuservice.domain.User;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long studentId = refreshTokenService.findByRefreshToken(refreshToken).getStudentId();
        User user = userService.findByStudentId(studentId);

        return tokenProvider.generateToken(user, Duration.ofHours(1));
    }
}
