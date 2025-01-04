package cn.hjf.job.company.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.service.CompanyAlbumService;
import cn.hjf.job.company.service.CompanyEmployeeService;
import cn.hjf.job.model.vo.company.PhotoVo;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * 公司相册控制器
 *
 * @author hjf
 * @version 1.0
 * @description
 */
@RestController
@RequestMapping("/album")
public class CompanyAlbumController {

    @Resource(name = "companyEmployeeServiceImpl")
    private CompanyEmployeeService companyEmployeeService;

    @Resource(name = "companyAlbumServiceImpl")
    private CompanyAlbumService companyAlbumService;

    /**
     * 招聘端获取照片
     *
     * @param principal 用户信息
     * @return Result<List < String>>
     */
    @GetMapping("/photos/recruiter")
    @PreAuthorize("hasRole('ROLE_ADMIN_RECRUITER')")
    public Result<List<PhotoVo>> findRecruiterPhotos(Principal principal) {
        try {
            Long companyId = companyEmployeeService.findCompanyIdByUserId(Long.parseLong(principal.getName()));
            if (companyId == null) return Result.fail();
            List<PhotoVo> recruiterPhotos = companyAlbumService.findRecruiterPhotos(companyId);
            return recruiterPhotos != null ? Result.ok(recruiterPhotos) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 保存公司照片
     *
     * @param path      路径
     * @param principal 用户
     * @return Result<String>
     */
    @PostMapping("/photo/recruiter")
    @PreAuthorize("hasRole('ROLE_ADMIN_RECRUITER')")
    public Result<Long> savePhoto(@RequestParam String path, Principal principal) {
        try {
            Long companyId = companyEmployeeService.findCompanyIdByUserId(Long.parseLong(principal.getName()));
            if (companyId == null) return Result.fail();
            Long id = companyAlbumService.savePhoto(companyId, path);
            return id != null ? Result.ok(id) : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 删除公司照片
     *
     * @param photoId   照片id
     * @param principal 用户
     * @return Result<String>
     */
    @DeleteMapping("/photo/recruiter/{photoId}")
    @PreAuthorize("hasRole('ROLE_ADMIN_RECRUITER')")
    public Result<String> deletePhoto(@PathVariable(name = "photoId") Long photoId, Principal principal) {
        try {
            Long companyId = companyEmployeeService.findCompanyIdByUserId(Long.parseLong(principal.getName()));
            if (companyId == null) return Result.fail();
            boolean isSuccess = companyAlbumService.deletePhoto(companyId, photoId);
            return isSuccess ? Result.ok() : Result.fail();
        } catch (Exception e) {
            return Result.fail();
        }
    }

    /**
     * 获取公司照片
     *
     * @param companyId 公司 id
     * @return Result<List < String>>
     */
    @GetMapping("/photos/candidate")
    public Result<List<String>> findCandidatePhotos(@RequestParam Long companyId) {
        List<String> candidatePhotos = companyAlbumService.findCandidatePhotos(companyId);
        return candidatePhotos != null ? Result.ok(candidatePhotos) : Result.fail();
    }
}
