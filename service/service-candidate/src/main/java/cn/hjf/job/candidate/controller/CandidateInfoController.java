package cn.hjf.job.candidate.controller;

import cn.hjf.job.candidate.service.CandidateInfoService;
import cn.hjf.job.model.entity.candidate.CandidateInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hjf
 * @since 2024-10-24
 */
@RestController
@RequestMapping("/candidateInfo")
public class CandidateInfoController {

    @Resource
    private CandidateInfoService candidateInfoService;

    @GetMapping("/{id}")
    public CandidateInfo getCandidateInfo(@PathVariable(name = "id") Integer id) {
        return candidateInfoService.getCandidateInfo(id);
    }

}
