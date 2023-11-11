package yiu.aisl.yiuservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.config.jwt.TokenProvider;
import yiu.aisl.yiuservice.domain.Token;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.dto.CreateAccessTokenRequest;
import yiu.aisl.yiuservice.dto.CreateAccessTokenResponse;
import yiu.aisl.yiuservice.dto.TokenDto;
import yiu.aisl.yiuservice.dto.UserLoginResponseDto;
import yiu.aisl.yiuservice.repository.TokenRepository;
import yiu.aisl.yiuservice.repository.UserRepository;

import java.time.Duration;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    public CreateAccessTokenResponse createNewAccessToken(CreateAccessTokenRequest request) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(request.getRefreshToken())) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long studentId = findByRefreshToken(request.getRefreshToken()).getStudentId();
        User user = userService.findByStudentId(studentId);
        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));

        System.out.println("새로 생성된 액세스 토큰: " + accessToken);

        return CreateAccessTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public Token validRefreshToken(User user, String refreshToken) throws Exception {
        Token token = tokenRepository.findById(user.getStudentId()).orElseThrow(() -> new Exception("만료된 계정입니다. 로그인을 다시 시도하세요"));
        // 해당 유저 Refresh 토큰 만료 => Redis에 해당 유저의 토큰이 존재하지 않음
        if(token.getRefreshToken() == null) {
            return null;
        }
        else {
            // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장
            if(token.getExpiration() < 10) {
                token.setExpiration(1000);
                tokenRepository.save(token);
            }

            // 토큰이 같은지 비교
            if(!token.getRefreshToken().equals(refreshToken)) {
                return null;
            }
            else {
                return token;
            }
        }
    }

//    public TokenDto refreshAccessToken(TokenDto token) throws Exception {
////        String account = token.getAccount(token.getAccess_token());
////        User user = userRepository.findByAccount(account).orElseThrow(() ->
////                new BadCredentialsException("잘못된 계정정보입니다."));
////        Token refreshToken = validRefreshToken(user, token.getRefresh_token());
////
////        if (refreshToken != null) {
////            return TokenDto.builder()
////                    .access_token(jwtProvider.createToken(account, user.getRoles()))
////                    .refresh_token(refreshToken.getRefresh_token())
////                    .build();
////        }
////        else {
////            throw new Exception("로그인을 해주세요");
////        }
//    }

    public User findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
