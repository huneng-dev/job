package cn.hjf.job.auth.config;

import cn.hjf.job.auth.filter.*;
import cn.hjf.job.auth.handler.CustomAuthenticationFailureHandler;
import cn.hjf.job.auth.handler.CustomAuthenticationSuccessHandler;
import cn.hjf.job.auth.provider.EmailCodeAuthenticationProvider;
import cn.hjf.job.auth.provider.EmailPasswordAuthenticationProvider;
import cn.hjf.job.auth.provider.PhoneCodeAuthenticationProvider;
import cn.hjf.job.auth.provider.PhonePasswordAuthenticationProvider;
import cn.hjf.job.common.fillter.JwtAuthenticationFilter;
import cn.hjf.job.common.handler.CustomAccessDeniedHandler;
import jakarta.annotation.Resource;
import cn.hjf.job.common.util.ValidationUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Resource
    private CustomAuthenticationSuccessHandler successHandler;

    @Resource
    private CustomAuthenticationFailureHandler failureHandler;

    @Resource
    private AuthenticationConfiguration authenticationConfiguration;

    @Resource
    private ValidationUtil validationUtil;

    @Resource
    private EmailCodeAuthenticationProvider emailCodeAuthenticationProvider;

    @Resource
    private PhoneCodeAuthenticationProvider phoneCodeAuthenticationProvider;

    @Resource
    private PhonePasswordAuthenticationProvider phonePasswordAuthenticationProvider;

    @Resource
    private EmailPasswordAuthenticationProvider emailPasswordAuthenticationProvider;

    @Resource
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * --------------------------------------------------------------
     * 执行流程注释图（ASCII图）：
     * --------------------------------------------------------------
     * <p>
     * 1. 客户端发起认证请求（如：/recruiter/email/code）
     * |
     * V
     * 2. 请求到达 SecurityFilterChain 配置类，过滤器链开始处理
     * |
     * V
     * 3. HttpSecurity 会根据请求路径找到相应的认证过滤器
     * |
     * V
     * |--> 邮箱验证码登录过滤器
     * |    |
     * |    V
     * |  RecruiterEmailCodeAuthenticationFilter
     * |    |
     * |    V
     * |  调用 authenticationManager() 来处理认证
     * |    |
     * |    V
     * |  authenticationManager 会遍历所有 AuthenticationProvider
     * |    |
     * |    V
     * |  找到对应的 EmailCodeAuthenticationProvider
     * |    |
     * |    V
     * |  调用 EmailCodeAuthenticationProvider 的 authenticate() 方法
     * |    |
     * |    V
     * |  EmailCodeAuthenticationProvider 内部调用 EmailCodeUserDetailsService
     * |    |
     * |    V
     * |  EmailCodeUserDetailsService 调用 loadUserByUsernameAndType(email, type)
     * |    |
     * |    V
     * |  验证验证码
     * |    |
     * |    V
     * |  验证通过后返回认证成功，调用 successHandler
     * |    |
     * |    V
     * |  返回认证成功响应
     * |
     * V
     * 4. 认证成功，继续访问受保护资源（如：职位详情、公司信息等）
     * <p>
     * --------------------------------------------------------------
     * 其他认证方式（手机验证码、手机密码、邮箱密码）类似处理流程：
     * - 找到相应的过滤器
     * - 认证提供者处理验证
     * - 成功后调用 successHandler 处理结果
     * --------------------------------------------------------------
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //邮箱验证码登录过滤器
        RecruiterEmailCodeAuthenticationFilter recruiterEmailCodeAuthenticationFilter =
                new RecruiterEmailCodeAuthenticationFilter(
                        authenticationManager(authenticationConfiguration),
                        successHandler,
                        failureHandler,
                        validationUtil
                );
        CandidateEmailCodeAuthenticationFilter candidateEmailCodeAuthenticationFilter =
                new CandidateEmailCodeAuthenticationFilter(
                        authenticationManager(authenticationConfiguration),
                        successHandler,
                        failureHandler,
                        validationUtil
                );

        // 手机验证码登录过滤器
        RecruiterPhoneCodeAuthenticationFilter recruiterPhoneCodeAuthenticationFilter =
                new RecruiterPhoneCodeAuthenticationFilter(
                        authenticationManager(authenticationConfiguration),
                        successHandler,
                        failureHandler,
                        validationUtil
                );
        CandidatePhoneCodeAuthenticationFilter candidatePhoneCodeAuthenticationFilter =
                new CandidatePhoneCodeAuthenticationFilter(
                        authenticationManager(authenticationConfiguration),
                        successHandler,
                        failureHandler,
                        validationUtil
                );

        // 手机密码登录过滤器
        RecruiterPhonePasswordAuthenticationFilter recruiterPhonePasswordAuthenticationFilter =
                new RecruiterPhonePasswordAuthenticationFilter(
                        authenticationManager(authenticationConfiguration),
                        successHandler,
                        failureHandler,
                        validationUtil
                );
        CandidatePhonePasswordAuthenticationFilter candidatePhonePasswordAuthenticationFilter =
                new CandidatePhonePasswordAuthenticationFilter(
                        authenticationManager(authenticationConfiguration),
                        successHandler,
                        failureHandler,
                        validationUtil
                );

        // 邮箱密码登录过滤器
        RecruiterEmailPasswordAuthenticationFilter recruiterEmailPasswordAuthenticationFilter =
                new RecruiterEmailPasswordAuthenticationFilter(
                        authenticationManager(authenticationConfiguration),
                        successHandler,
                        failureHandler,
                        validationUtil
                );
        CandidateEmailPasswordAuthenticationFilter candidateEmailPasswordAuthenticationFilter =
                new CandidateEmailPasswordAuthenticationFilter(
                        authenticationManager(authenticationConfiguration),
                        successHandler,
                        failureHandler,
                        validationUtil
                );

        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(recruiterEmailCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(candidateEmailCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(candidatePhoneCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(recruiterPhoneCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(recruiterPhonePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(candidatePhonePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(recruiterEmailPasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(candidateEmailPasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(emailCodeAuthenticationProvider);
        providers.add(phoneCodeAuthenticationProvider);
        providers.add(phonePasswordAuthenticationProvider);
        providers.add(emailPasswordAuthenticationProvider);
        return new ProviderManager(providers);
    }
}