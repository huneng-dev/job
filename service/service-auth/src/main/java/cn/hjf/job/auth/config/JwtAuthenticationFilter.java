package cn.hjf.job.auth.config;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);
        if (token != null) {
            Authentication authentication = getAuthenticationFromToken(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private Authentication getAuthenticationFromToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String username = jwt.getClaim("sub");  // 假设JWT中存储了username
            List<GrantedAuthority> authorities = extractAuthoritiesFromJwt(jwt); // 提取角色信息
            // TODO 从redis中查询权限
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        }  catch (JwtValidationException ex) {
            // 处理 JWT 校验失败（如过期或无效）
            return null; // 或者可以抛出一个特定的认证异常
        } catch (JwtException ex) {
            // 其他 JWT 相关异常
            return null; // 或者抛出一个认证异常
        } catch (Exception ex) {
            // 捕获其他类型的异常
            return null;
        }
    }

    private List<GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
        // 假设角色存储在JWT的"roles"字段中
        List<String> roles = jwt.getClaim("roles");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
