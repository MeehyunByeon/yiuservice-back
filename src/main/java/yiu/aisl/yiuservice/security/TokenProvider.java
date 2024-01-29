package yiu.aisl.yiuservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import yiu.aisl.yiuservice.config.CustomUserDetails;
import yiu.aisl.yiuservice.domain.User;
import yiu.aisl.yiuservice.security.JwtProperties;
import yiu.aisl.yiuservice.service.JpaUserDetailsService;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class TokenProvider {

    private final JwtProperties jwtProperties;

    @Value("${jwt.secret.key}")
    private String salt;

    private Key secretKey;

    @Value("${admin.studentId1}")
    private Long admin1;
    @Value("${admin.studentId2}")
    private Long admin2;
    @Value("${admin.studentId3}")
    private Long admin3;


    // 만료시간 : 30분
//    private final long exp = 500L * 60 * 60;

    private long accessTokenValidTime = Duration.ofMinutes(30).toMillis(); // 만료시간 30분
    private long refreshTokenValidTime = Duration.ofDays(14).toMillis(); // 만료시간 2주

    private final JpaUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성
    public String createToken(User user) {
        Date now = new Date();

        JwtBuilder jwtBuilder = Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidTime))
                .setSubject(user.getNickname())
                .claim("studentId", user.getStudentId())
                .claim("nickname", user.getNickname())
                .signWith(secretKey, SignatureAlgorithm.HS256);

        if(user.getStudentId().equals(admin1)
                || user.getStudentId().equals(admin2)
                || user.getStudentId().equals(admin3))
            jwtBuilder.claim("role", "ADMIN");
        else jwtBuilder.claim("role", "USER");

        return jwtBuilder.compact();
    }

    // 권한정보 획득
    // Spring Security 인증과정에서 권한확인을 위한 기능
    public Authentication getAuthentication(String token) {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByStudentId(this.getStudentId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    // 토큰에 담겨있는 유저 account 획득
    public Long getStudentId(String token) {
        Claims claims = getClaims(token);
        return claims.get("studentId", Long.class);
    }

    // Authorization Header를 통해 인증을 한다.
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            // Bearer 검증
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                    .parseClaimsJws(token);
            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        System.out.println("validToken 시작");
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                    .parseClaimsJws(token);
            System.out.println("validToken claims: "+ claims.getBody());
            // 만료되었을 시 false
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) { // 복호화 과정에서 에러가 나면 유효하지 않은 토큰
            System.out.println("복호화 에러: "+ e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser() // 클레임 조회
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 정보 리턴
    public TokenInfo getTokenInfo(String token) {
        Claims body = getClaims(token);
        Set<String> keySet = body.keySet();
        for (String s : keySet) {
            System.out.println("s = " + s);
        }

        Long studentId = body.get("studentId", Long.class);
        String nickname = body.get("nickname", String.class);
        Date issuedAt = body.getIssuedAt();
        Date expiration = body.getExpiration();
        return new TokenInfo(studentId, nickname, issuedAt, expiration);
    }


    @Getter
    public class TokenInfo {
        private Long studentId;
        private String nickname;
        private Date issuedAt;
        private Date expire;

        public TokenInfo(Long studentId, String nickname, Date issuedAt, Date expire) {
            this.studentId = studentId;
            this.nickname = nickname;
            this.issuedAt = issuedAt;
            this.expire = expire;
        }
    }

}
