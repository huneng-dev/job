package cn.hjf.job.auth.token;


import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class PhoneCodeAuthenticationToken extends AbstractAuthenticationToken {


    private final String phone;
    // 验证码
    private final String code;

    // 用户类型
    private final Integer type;

    public PhoneCodeAuthenticationToken(String phone, String code, Integer type) {
        super(null); //认证前不设置权限
        this.phone = phone;
        this.code = code;
        this.type = type;
        super.setAuthenticated(false);
    }

    public PhoneCodeAuthenticationToken(String id, String code, Integer type, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.phone = id;
        this.code = code;
        this.type = type;
        setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getPrincipal() {
        return phone;
    }
}
