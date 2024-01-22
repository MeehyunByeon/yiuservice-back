package yiu.aisl.yiuservice.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import yiu.aisl.yiuservice.domain.User;

import java.util.Collection;
import java.util.Collections;

// UserDetails를 사용하면 아래의 메소드명을 변경하면 안됨
public class CustomUserDetails implements UserDetails {
    private final User user;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    public CustomUserDetails(User user) {
        this.user = user;
    }
    public final User getUser() {
        return user;
    }

    public Long getStudentId() {
        return user.getStudentId();
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }

    @Override
    public String getUsername() {
        return user.getNickname();
    }

    //이하 4개의 메소드는 jwt를 사용하기에 true로 설정
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}