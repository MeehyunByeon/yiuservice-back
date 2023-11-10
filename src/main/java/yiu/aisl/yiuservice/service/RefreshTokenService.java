package yiu.aisl.yiuservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final UserRepository userRepository;

    public User findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
