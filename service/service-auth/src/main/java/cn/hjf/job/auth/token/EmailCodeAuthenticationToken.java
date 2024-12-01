package cn.hjf.job.auth.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class EmailCodeAuthenticationToken extends AbstractAuthenticationToken {
    // 手机号
    private final String email;

    // 验证码
    private final String code;

    // 用户类型
    private final Integer type;


    // 认证前的构造函数，credentials 为 null，表示尚未认证
    public EmailCodeAuthenticationToken(String email, String code, Integer type) {
        super(null); //认证前不设置权限
        this.email = email;
        this.code = code;
        this.type = type;
        setAuthenticated(false);
    }

    // 认证后的构造函数，包含权限信息
    public EmailCodeAuthenticationToken(String id, String code, Integer type, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.email = id;
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
        return email;
    }
}
