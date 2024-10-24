package cn.hjf.job;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("cn.hjf.job.candidate.mapper")
public class RunCandidate {
    public static void main(String[] args) {
        SpringApplication.run(RunCandidate.class,args);
    }
}
