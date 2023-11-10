package yiu.aisl.yiuservice.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import yiu.aisl.yiuservice.domain.User;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    @Value("${jwt.secret.key}")
    private String salt;

    private Key secretKey;

    // 만료시간 => 1h => 1000L * 60 * 60
    private final long exp = 1000L * 60; // 1분


    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 토큰 생성 메서드
    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    public String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 type : JWT
                .setIssuer(jwtProperties.getIssuer()) // 내용 iss : yiu.aisl(application.properties)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getNickname())
                .claim("studentId", user.getStudentId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    // JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀겂으로 복호화
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) { // 복호화 과정에서 에러가 나면 유효하지 않은 토큰
            return false;
        }

//        try {
//            // Bearer 검증
//            if(!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
//                return false;
//            }
//            else {
//                token = token.split(" ")[1].trim();
//            }
//
//            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
//
//            // 만료되었을 시 => false
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
    }

     // 토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token, authorities);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getAccount(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰 기반으로 학번을 가져오는 메서드
    public Long getStudentId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
//        // 만료된 토큰에 대해 parseClaimsJwt를 수행하면 io.jsonwebtoken.ExpiredJwrException이 발생한다.
//        try {
//            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
//        } catch (ExpiredJwtException e) {
//            e.printStackTrace();
//            return e.getClaims().getSubject();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parser() // 클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    // Authorization Header를 통해 인증을 한다.
//    public String resolveToken(HttpServletRequest request) {
//        return request.getHeader("Authorization");
//    }
//
//    // 토큰 검증
//    public boolean validateToken(String token) {
//        try {
//            // Bearer 검증
//            if(!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
//                return false;
//            }
//            else {
//                token = token.split(" ")[1].trim();
//            }
//
//            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
//
//            // 만료되었을 시 => false
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
}
