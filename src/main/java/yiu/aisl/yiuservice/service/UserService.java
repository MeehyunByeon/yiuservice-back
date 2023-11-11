package yiu.aisl.yiuservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.Token;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.dto.TokenDto;
import yiu.aisl.yiuservice.dto.UserJoinRequestDto;
import yiu.aisl.yiuservice.dto.UserLoginRequestDto;
import yiu.aisl.yiuservice.dto.UserLoginResponseDto;
import yiu.aisl.yiuservice.repository.TokenRepository;
import yiu.aisl.yiuservice.repository.UserRepository;
import yiu.aisl.yiuservice.config.jwt.TokenProvider;

import java.time.Duration;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;
//    private final TokenService tokenService;

    private final long exp = 1000L * 60 * 60 * 24 * 14; // 14일

    @Transactional
    public Boolean join(UserJoinRequestDto request) throws Exception {
        if (userRepository.findByStudentId(request.getStudentId()).isPresent()) {
            throw new Exception("이미 존재하는 학번입니다.");
        }

        try {
            User user = User.builder()
                    .studentId(request.getStudentId())
                    .nickname(request.getNickname())
                    .pwd(passwordEncoder.encode(request.getPwd()))
                    .build();
            userRepository.save(user);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    // 학번으로 유저의 정보를 가져오는 메서드
    public User findByStudentId(Long studentId) {
        return userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public UserLoginResponseDto login(UserLoginRequestDto request) {
        User user = userRepository.findByStudentId(request.getStudentId()).orElseThrow(() -> new BadCredentialsException("존재하지 않는 계정"));

        if(!passwordEncoder.matches(request.getPwd(), user.getPwd())) {
            throw new BadCredentialsException("비밀번호 불일치");
        }

        user.setRefreshToken(createRefreshToken(user));
        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));

        return UserLoginResponseDto.builder()
                .studentId(user.getStudentId())
                .nickname(user.getNickname())
                .accessToken(accessToken)
                .build();
    }

    public String createRefreshToken(User user) {
        Token token = tokenRepository.save(
                Token.builder()
                        .studentId(user.getStudentId())
                        .refreshToken(UUID.randomUUID().toString())
                        .expiration(120) // 2분ㅋ
                        .build()
        );
//        System.out.println("token" + token.getRefreshToken());
        return token.getRefreshToken();
    }

    public User findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }

//    public UserResponse login(UserRequest request) throws Exception {
//        User user = userRepository.findByAccount(request.getAccount()).orElseThrow(() ->
//                new BadCredentialsException("잘못된 계정정보입니다."));
//        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new BadCredentialsException("잘못된 계정정보입니다.");
//        }
//
//        user.setRefreshToken(createRefreshToken(user));
//
//        return UserResponse.builder()
//                .id(user.getId())
//                .account(user.getAccount())
//                .email(user.getEmail())
//                .name(user.getName())
//                .roles(user.getRoles())
//                .token(TokenDto.builder()
//                        .access_token(jwtProvider.createToken(user.getAccount(), user.getRoles()))
//                        .refresh_token(user.getRefreshToken())
//                        .build())
//                .build();
//    }
//
//    public UserResponse getUser(String account) throws Exception {
//        User user = userRepository.findByAccount(account)
//                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
//        return new UserResponse(user);
//    }
//
//
}
