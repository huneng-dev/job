package cn.hjf.job.position.controller;

import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.vo.position.PositionTypeVo;
import cn.hjf.job.position.service.PositionTypeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 职位类型管理Controller
 *
 * @author hjf
 * @since 2024-10-25
 */
@RestController
@RequestMapping("/position-type")
public class PositionTypeController {

    @Resource
    private PositionTypeService positionTypeService;

    /**
     * 获取职位类型
     *
     * @param industriesIds 行业id
     * @return 职位类型
     */
    @GetMapping("/find-position-type")
    public Result<Map<Long, List<PositionTypeVo>>> findPositionTypesByIndustriesId(@RequestParam List<Long> industriesIds) {
        Map<Long, List<PositionTypeVo>> longListMap = positionTypeService.queryPositionTypeByIndustryId(industriesIds);
        return Result.ok(longListMap);
    }

    /**
     * 根据职位类型 id 获取描述
     *
     * @param id 职位类型 id
     * @return Result<String>
     */
    @GetMapping("/{id}")
    public Result<String> getPositionTypeById(@PathVariable(name = "id") Long id) {
        String positionTypeDescByPositionId = positionTypeService.getPositionTypeDescByPositionId(id);
        return Result.ok(positionTypeDescByPositionId);
    }
}
