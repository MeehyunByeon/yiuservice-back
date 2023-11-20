package yiu.aisl.yiuservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter{
    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더의 Authorization 키의 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        // 가져온 값에서 접두사 제거
        String token = getAccessToken(authorizationHeader);
        // 가져온 토큰이 유효한지 확인하고, 유효한 때는 인증 정보 설정
        // => 시큐리티 컨텍스트에 인증 정보를 설정
        if(token != null && tokenProvider.validToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        // 요청 헤더 'Authorization' 필드 값 가져와서 접두사 Bearer 제외하고 반환
        if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }


//    public TokenAuthenticationFilter(TokenProvider jwtProvider) {
//        this.tokenProvider = jwtPr;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response , FilterChain filterChain)
//            throws ServletException, IOException {
//        String token = jwtProvider.resolveToken(request);
//
//        if(token != null && jwtProvider.validateToken(token)) {
//            // access token 확인
//            // Beare access_token => split 하면 [0] = Bearer [1] = access_token
//            token = token.split(" ")[1].trim();
////            Authentication auth = jwtProvider.getAuthentication(token);
////            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//
//        filterChain.doFilter(request, response);
//    }
}
