package cn.hjf.job.auth.filter;

import cn.hjf.job.common.constant.UserTypeConstant;
import cn.hjf.job.common.execption.ValidationException;
import cn.hjf.job.auth.handler.CustomAuthenticationFailureHandler;
import cn.hjf.job.auth.handler.CustomAuthenticationSuccessHandler;
import cn.hjf.job.auth.token.EmailCodeAuthenticationToken;
import cn.hjf.job.model.form.auth.EmailCodeLoginForm;
import cn.hjf.job.common.util.ValidationUtil;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class RecruiterEmailCodeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ValidationUtil validationUtil;

    public RecruiterEmailCodeAuthenticationFilter(
            AuthenticationManager authenticationManager,
            CustomAuthenticationSuccessHandler successHandler,
            CustomAuthenticationFailureHandler failureHandler,
            ValidationUtil validationUtil
    ) {
        this.validationUtil = validationUtil;
        super.setAuthenticationManager(authenticationManager);
        super.setPostOnly(true);
        super.setFilterProcessesUrl("/auth/recruiter/email/code");
        super.setAuthenticationSuccessHandler(successHandler);
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 获取参数
        EmailCodeLoginForm emailCodeLoginForm = JSON.parseObject(request.getInputStream(), EmailCodeLoginForm.class);
        try {
            // 校验参数
            validationUtil.validate(emailCodeLoginForm);
        } catch (ValidationException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        return this.getAuthenticationManager().authenticate(new EmailCodeAuthenticationToken(emailCodeLoginForm.getEmail(), emailCodeLoginForm.getValidateCode(), UserTypeConstant.RECRUITER));
    }

}
