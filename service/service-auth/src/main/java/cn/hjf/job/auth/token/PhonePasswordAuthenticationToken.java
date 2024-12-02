package cn.hjf.job.auth.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class PhonePasswordAuthenticationToken extends AbstractAuthenticationToken {

    private final String phone;
    // 验证码
    private final String password;

    // 用户类型
    private final Integer type;

    public PhonePasswordAuthenticationToken(String phone, String password, Integer type) {
        super(null);
        this.phone = phone;
        this.password = password;
        this.type = type;
        this.setAuthenticated(false);
    }

    public PhonePasswordAuthenticationToken(String id, String password, Integer type, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.phone = id;
        this.password = password;
        this.type = type;
        this.setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return phone;
    }
}
