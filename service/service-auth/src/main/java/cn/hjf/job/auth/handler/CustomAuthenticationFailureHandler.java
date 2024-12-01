package cn.hjf.job.auth.handler;

import cn.hjf.job.common.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Result<String> build = Result.build("", 403, exception.getMessage());
        // 设置响应类型为 JSON
        response.setContentType("application/json;charset=UTF-8");
        // 将对象转换为 JSON 字符串
        String jsonResponse = new ObjectMapper().writeValueAsString(build);
        response.getWriter().write(jsonResponse);
    }
}
