package cn.hjf.job.recruiter.controller;

import cn.hjf.job.model.entity.recruiter.RecruiterInfo;
import cn.hjf.job.recruiter.service.RecruiterInfoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hjf
 * @since 2024-10-25
 */
@RestController
@RequestMapping("/recruiter")
public class RecruiterInfoController {

    @Resource
    private RecruiterInfoService recruiterInfoService;

    @GetMapping("/{id}")
    public RecruiterInfo getRecruiterInfoById(@PathVariable(name = "id") Integer id){
        return recruiterInfoService.getRecruiterInfoById(id);
    }
}
