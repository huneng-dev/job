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
import org.springframework.beans.factory.annotation.Autowired;
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