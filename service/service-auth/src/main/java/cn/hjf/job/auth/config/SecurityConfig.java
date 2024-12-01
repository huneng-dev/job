package cn.hjf.job.auth.config;

import cn.hjf.job.auth.filter.CandidateEmailCodeAuthenticationFilter;
import cn.hjf.job.auth.filter.CandidatePhoneCodeAuthenticationFilter;
import cn.hjf.job.auth.filter.RecruiterEmailCodeAuthenticationFilter;
import cn.hjf.job.auth.filter.RecruiterPhoneCodeAuthenticationFilter;
import cn.hjf.job.auth.handler.CustomAuthenticationFailureHandler;
import cn.hjf.job.auth.handler.CustomAuthenticationSuccessHandler;
import cn.hjf.job.auth.provider.EmailCodeAuthenticationProvider;
import cn.hjf.job.auth.provider.PhoneCodeAuthenticationProvider;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authorize) -> authorize
////                        .requestMatchers(whitelistConfig.getPathArrayByPrefixes("/auth")).permitAll()
//                        .anyRequest().permitAll()
//                )
//                .exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler))
//                // 关闭csrf
//                .csrf(AbstractHttpConfigurer::disable)
//                .authenticationManager(authenticationManager(authenticationConfiguration))
//                .authenticationProvider(new CustomAuthenticationProvider())
//                .addFilterAt(new CustomUsernamePasswordAuthenticationFilter(
//                                authenticationManager(authenticationConfiguration),
//                                redisTemplate,
//                                11111111L,
//                                userInfoFeignClient,
//                                jwtUtil
//                        )
//                        , UsernamePasswordAuthenticationFilter.class
//                )
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//        ;
//        return http.build();
//    }

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

        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(recruiterEmailCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(candidateEmailCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(candidatePhoneCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(recruiterPhoneCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(emailCodeAuthenticationProvider);
        providers.add(phoneCodeAuthenticationProvider);

        return new ProviderManager(providers);
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
}