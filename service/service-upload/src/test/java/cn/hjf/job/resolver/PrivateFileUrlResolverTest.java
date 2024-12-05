package cn.hjf.job.resolver;

import cn.hjf.job.common.minio.resolver.PrivateFileUrlResolver;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class PrivateFileUrlResolverTest {

    @Resource
    private PrivateFileUrlResolver privateFileUrlResolver;

    @Test
    public void resolveSingleUrl() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String path = privateFileUrlResolver.resolveSingleUrl(
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                120
        );
        System.err.println(path);
    }

    @Test
    public void resolveMultipleUrls() {
        List<String> list = Arrays.asList(
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png",
                "/121job/user-avatar/20241206/b1a9f2e3d8ef4ac18a71eb4bce090e2f.png"
        );

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 计算 resolveMultipleUrls 执行的耗时
        List<String> result = privateFileUrlResolver.resolveMultipleUrls(list, 60);

        // 记录结束时间
        long endTime = System.currentTimeMillis();

        // 计算耗时并打印
        long duration = endTime - startTime;
        System.out.println("Execution time: " + duration + " ms");

    }
}
