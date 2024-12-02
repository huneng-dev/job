package cn.hjf.job.auth.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class EmailPasswordAuthenticationToken extends AbstractAuthenticationToken {

    private final String email;
    // 验证码
    private final String password;

    // 用户类型
    private final Integer type;

    public EmailPasswordAuthenticationToken(String email, String password, Integer type) {
        super(null);
        this.email = email;
        this.password = password;
        this.type = type;
        this.setAuthenticated(false);
    }

    public EmailPasswordAuthenticationToken(String id, String password, Integer type, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.email = id;
        this.password = password;
        this.type = type;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
