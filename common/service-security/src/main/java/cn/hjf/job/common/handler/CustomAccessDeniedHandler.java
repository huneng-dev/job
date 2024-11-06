package cn.hjf.job.common.handler;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.common.result.ResultCodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 设置响应的内容类型为 JSON
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value()); // 响应 200 状态码
        Result<String> result = Result.build(accessDeniedException.getMessage(), ResultCodeEnum.PERMISSION);
        String jsonResponse = new ObjectMapper().writeValueAsString(result);
                // 将 JSON 数据写入响应
        response.getWriter().write(jsonResponse);
    }
}
