package cn.hjf.job.auth.filter;

import cn.hjf.job.auth.handler.CustomAuthenticationFailureHandler;
import cn.hjf.job.auth.handler.CustomAuthenticationSuccessHandler;
import cn.hjf.job.auth.token.PhoneCodeAuthenticationToken;
import cn.hjf.job.common.constant.UserTypeConstant;
import cn.hjf.job.common.execption.ValidationException;
import cn.hjf.job.common.util.ValidationUtil;
import cn.hjf.job.model.form.auth.PhoneCodeLoginForm;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class RecruiterPhoneCodeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ValidationUtil validationUtil;

    public RecruiterPhoneCodeAuthenticationFilter(
            AuthenticationManager authenticationManager,
            CustomAuthenticationSuccessHandler successHandler,
            CustomAuthenticationFailureHandler failureHandler,
            ValidationUtil validationUtil
    ) {
        this.validationUtil = validationUtil;
        super.setAuthenticationManager(authenticationManager);
        super.setPostOnly(true);
        super.setFilterProcessesUrl("/auth/recruiter/phone/code");
        super.setAuthenticationSuccessHandler(successHandler);
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 从请求中获取表单
        PhoneCodeLoginForm phoneCodeLoginForm = JSON.parseObject(request.getInputStream(), PhoneCodeLoginForm.class);
        try {
            // 校验参数
            validationUtil.validate(phoneCodeLoginForm);
        } catch (ValidationException e) {
            throw new BadCredentialsException("Invalid input: " + e.getMessage());
        }
        return this.getAuthenticationManager().authenticate(new PhoneCodeAuthenticationToken(phoneCodeLoginForm.getPhone(), phoneCodeLoginForm.getValidateCode(), UserTypeConstant.RECRUITER));
    }
}
