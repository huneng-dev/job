package cn.hjf.job.candidate.service;

import cn.hjf.job.model.entity.candidate.CandidateInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class TestCandidateInfoServiceImpl {

    @Resource
    private CandidateInfoService candidateInfoService;

    @Test
    public void testGetCandidateInfo() {
        CandidateInfo candidateInfo = candidateInfoService.getCandidateInfo(1);
        System.out.println(candidateInfo);
    }

}
