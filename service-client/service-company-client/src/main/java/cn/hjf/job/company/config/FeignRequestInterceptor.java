package cn.hjf.job.company.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 从当前线程的 RequestContext 中获取原始请求的 Authorization 头
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String authHeader = attributes.getRequest().getHeader("Authorization");

            if (authHeader != null) {
                // 将原请求中的 Authorization 头部添加到 Feign 请求的头部
                template.header("Authorization", authHeader);
            }
        }
    }
}

