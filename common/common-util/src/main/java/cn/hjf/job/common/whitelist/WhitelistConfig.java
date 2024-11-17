package cn.hjf.job.common.whitelist;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hjf
 * @version 1.0
 * @description 白名单加载类
 */
@Getter
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "whitelist")
public class WhitelistConfig {

    // 白名单
    private Set<String> path = new HashSet<>();

    // 返回 String 数组
    public String[] getPathArray() {
        return path.toArray(new String[0]);  // 将 Set 转换为 String 数组
    }

    // 根据多个前缀返回数组
    public String[] getPathArrayByPrefixes(String... prefixes) {
        ArrayList<String> matchPaths = new ArrayList<>();
        for (String p : path) {
            for (String prefix : prefixes) {
                if (p.startsWith(prefix)) {
                    matchPaths.add(p);
                    break; // 匹配到一个前缀后跳出内层循环
                }
            }
        }
        return matchPaths.toArray(new String[0]);
    }
}
