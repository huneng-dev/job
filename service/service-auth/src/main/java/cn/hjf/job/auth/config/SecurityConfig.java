package cn.hjf.job.auth.config;

import cn.hjf.job.common.jwt.JwtUtil;
import cn.hjf.job.common.whitelist.WhitelistConfig;
import cn.hjf.job.user.client.UserInfoFeignClient;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Resource
    private AuthenticationConfiguration authenticationConfiguration;

    @Resource
    private UserInfoFeignClient userInfoFeignClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private WhitelistConfig whitelistConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers(whitelistConfig.getPathArrayByPrefixes("/auth")).permitAll()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(exception -> exception.accessDeniedHandler(customAccessDeniedHandler))
                // 关闭csrf
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationManager(authenticationManager(authenticationConfiguration))
                .authenticationProvider(new CustomAuthenticationProvider())
                .addFilterAt(new CustomUsernamePasswordAuthenticationFilter(
                                authenticationManager(authenticationConfiguration),
                                redisTemplate,
                                11111111L,
                                userInfoFeignClient,
                                jwtUtil
                        )
                        , UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = passwordEncoder();
        System.out.println(passwordEncoder.encode("job@020902"));
//        System.out.println(passwordEncoder.matches("job@020902","$2a$04$O7B1jE9bGKDO6BYFbtIu4OoDhYF.EOIhBglOvAusUw6ZBTsCNQlLa"));
    }
}