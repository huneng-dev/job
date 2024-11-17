package cn.hjf.job.position.service;

import cn.hjf.job.model.vo.position.PositionTypeVo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class PositionTypeServiceTest {

    @Resource
    private PositionTypeService positionTypeService;

    @Test
    public void queryPositionTypesByIndustryId() {
//        Map<Long, List<PositionTypeVo>> longListMap = positionTypeService.queryPositionTypeByIndustryId(Arrays.asList(1L, 2L, 3L));
        positionTypeService.queryPositionTypeByIndustryId(Arrays.asList(21L, 22L,24L, 23L));
    }
}
