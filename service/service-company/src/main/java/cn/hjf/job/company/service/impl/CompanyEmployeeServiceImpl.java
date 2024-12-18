package cn.hjf.job.company.service.impl;

import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.constant.ValidateCodeConstant;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.company.config.KeyProperties;
import cn.hjf.job.company.mapper.CompanyEmployeeMapper;
import cn.hjf.job.company.service.CompanyEmployeeService;
import cn.hjf.job.company.service.CompanyTitleService;
import cn.hjf.job.model.entity.company.CompanyEmployee;
import cn.hjf.job.model.vo.base.PageVo;
import cn.hjf.job.model.vo.company.CompanyEmployeeVo;
import cn.hjf.job.model.vo.company.CompanyIdAndIsAdmin;
import cn.hjf.job.model.vo.user.EmployeeInfoVo;
import cn.hjf.job.user.client.UserInfoFeignClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
public class CompanyEmployeeServiceImpl extends ServiceImpl<CompanyEmployeeMapper, CompanyEmployee> implements CompanyEmployeeService {

    @Resource
    private CompanyEmployeeMapper companyEmployeeMapper;

    @Resource(name = "companyTitleServiceImpl")
    private CompanyTitleService companyTitleService;

    @Resource
    private UserInfoFeignClient userInfoFeignClient;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private KeyProperties keyProperties;

    @Override
    public boolean setCompanyAdminEmployee(Long companyId, Long userId, Long titleId) {
        CompanyEmployee companyEmployee = new CompanyEmployee(userId, companyId, titleId, 1);
        int insert = companyEmployeeMapper.insert(companyEmployee);
        return insert == 1;
    }

    @Override
    public Long findCompanyIdByUserId(Long userId) {
        LambdaQueryWrapper<CompanyEmployee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CompanyEmployee::getCompanyId).eq(CompanyEmployee::getUserId, userId);
        CompanyEmployee companyEmployee = companyEmployeeMapper.selectOne(queryWrapper);
        if (companyEmployee == null) {
            return null;
        }
        return companyEmployee.getCompanyId();
    }


    @Override
    public String getVerificationCode(Long userId) {
        // 查询管理员所在的公司
        LambdaQueryWrapper<CompanyEmployee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CompanyEmployee::getCompanyId).eq(CompanyEmployee::getUserId, userId);
        CompanyEmployee companyEmployee = companyEmployeeMapper.selectOne(queryWrapper);

        if (companyEmployee == null) return null;
        Long companyId = companyEmployee.getCompanyId();

        // 获取到验证码
        String verificationCode = generateVerificationCode();

        // 存储到 redis
        redisTemplate.opsForValue().set(
                RedisConstant.COMPANY_ADD_CODE + companyId,
                verificationCode,
                ValidateCodeConstant.COMPANY_ADD_CODE_TIME,
                TimeUnit.SECONDS
        );

        return verificationCode;
    }

    @Override
    public boolean addEmployeeToCompany(Long userId, Long companyId, String code) {
        // 检查用户是否已在公司
        if (findCompanyIdByUserId(userId) != null) {
            return false;
        }

        // 获取当前用户的剩余时间及尝试次数
        String attemptsStr = redisTemplate.opsForValue().get(RedisConstant.COMPANY_ADD_USER + userId);
        int userNum = 0;
        if (attemptsStr != null) {
            userNum = Integer.parseInt(attemptsStr);
            Long expire = redisTemplate.getExpire(RedisConstant.COMPANY_ADD_USER + userId);
            if (expire != null && expire > 0 && userNum >= 5) {
                long minutesLeft = expire / 60;
                throw new RuntimeException("错误次数过多, 请 " + minutesLeft + " 分钟后重试");
            }
        }

        // 获取验证码并校验
        String storedCode = redisTemplate.opsForValue().get(RedisConstant.COMPANY_ADD_CODE + companyId);
        if (storedCode == null || !storedCode.equals(code)) {
            // 记录错误次数并设置过期时间
            incrementUserAttempts(userId, userNum);
            return false;
        }

        // 删除 Redis 中的验证码和错误尝试记录
        redisTemplate.delete(RedisConstant.COMPANY_ADD_CODE + companyId);
        redisTemplate.delete(RedisConstant.COMPANY_ADD_USER + userId);

        // 将员工加入公司
        CompanyEmployee companyEmployee = new CompanyEmployee();
        companyEmployee.setCompanyId(companyId);
        companyEmployee.setUserId(userId);
        int isSuccess = companyEmployeeMapper.insert(companyEmployee);
        return isSuccess == 1;
    }

    @Override
    public PageVo<CompanyEmployeeVo> findCompanyEmployeePage(Page<CompanyEmployee> companyEmployeePage, Long userId) {
        Long companyId = findCompanyIdByUserId(userId);

        LambdaQueryWrapper<CompanyEmployee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询参数 {userId,titleId} , 排除当前公司的管理员
        lambdaQueryWrapper.select(CompanyEmployee::getUserId, CompanyEmployee::getTitleId)
                .eq(CompanyEmployee::getCompanyId, companyId)
                .eq(CompanyEmployee::getIsAdmin, 0);

        Page<CompanyEmployee> page = companyEmployeeMapper.selectPage(companyEmployeePage, lambdaQueryWrapper);

        List<CompanyEmployee> records = page.getRecords();

        List<CompanyEmployeeVo> companyEmployeeVos = new ArrayList<>();

        // 使用 HashMap 缓存标题名称
        HashMap<Long, String> titleNameCache = new HashMap<>();

        for (CompanyEmployee recode : records) {
            CompanyEmployeeVo companyEmployeeVo = new CompanyEmployeeVo();
            companyEmployeeVo.setId(recode.getUserId());

            String title;

            if (recode.getTitleId() == null) {
                // 如果没有 TitleId，设置为默认值
                title = "专员";
            } else {
                // 查询缓存中是否存在标题
                title = titleNameCache.get(recode.getTitleId());

                if (title == null) {
                    // 如果缓存中没有，查询数据库并缓存结果
                    title = companyTitleService.findTitleNameById(recode.getTitleId());
                    titleNameCache.put(recode.getTitleId(), title);
                }
            }

            // 设置标题
            companyEmployeeVo.setTitle(title);

            // 添加到最终的列表
            companyEmployeeVos.add(companyEmployeeVo);
        }

        // 找到全部的用户id
        List<Long> userIds = companyEmployeeVos.stream().map(
                CompanyEmployeeVo::getId
        ).toList();

        Result<List<EmployeeInfoVo>> companyEmployeeResult = userInfoFeignClient.findCompanyEmployeeByUserIds(userIds, keyProperties.getKey());

        if (!Objects.equals(companyEmployeeResult.getCode(), 200)) {
            return null;
        }

        List<EmployeeInfoVo> data = companyEmployeeResult.getData();

        // 直接按顺序映射，确保 companyEmployeeVos 和 data 顺序一致
        for (int i = 0; i < companyEmployeeVos.size(); i++) {
            if (i < data.size()) {
                EmployeeInfoVo employeeInfoVo = data.get(i);
                companyEmployeeVos.get(i).setAvatar(employeeInfoVo.getAvatar());
                companyEmployeeVos.get(i).setName(employeeInfoVo.getName());
                companyEmployeeVos.get(i).setUserName(employeeInfoVo.getUserName());
            }
        }

        PageVo<CompanyEmployeeVo> companyEmployeeVoPage = new PageVo<>();
        companyEmployeeVoPage.setRecords(companyEmployeeVos);
        companyEmployeeVoPage.setTotal(page.getTotal());
        companyEmployeeVoPage.setPage(page.getCurrent());
        companyEmployeeVoPage.setPages(page.getPages());
        companyEmployeeVoPage.setLimit(page.getSize());
        return companyEmployeeVoPage;
    }

    @Override
    public CompanyIdAndIsAdmin findCompanyIdAndIsAdminByUserId(Long userId) {
        LambdaQueryWrapper<CompanyEmployee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CompanyEmployee::getCompanyId, CompanyEmployee::getIsAdmin)
                .eq(CompanyEmployee::getUserId, userId);
        CompanyEmployee companyEmployee = companyEmployeeMapper.selectOne(queryWrapper);
        return new CompanyIdAndIsAdmin(
                companyEmployee.getCompanyId(),
                companyEmployee.getIsAdmin()
        );
    }

    @Override
    public CompanyEmployeeVo findCompanyEmployeeById(Long targetId, Long userId) {
        Long companyId = findCompanyIdByUserId(userId);
        LambdaQueryWrapper<CompanyEmployee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询参数 {userId,titleId} , 排除当前公司的管理员
        lambdaQueryWrapper.select(CompanyEmployee::getUserId, CompanyEmployee::getTitleId)
                .eq(CompanyEmployee::getCompanyId, companyId)
                .eq(CompanyEmployee::getUserId, targetId);

        CompanyEmployee companyEmployee = companyEmployeeMapper.selectOne(lambdaQueryWrapper);

        CompanyEmployeeVo companyEmployeeVo = new CompanyEmployeeVo();
        companyEmployeeVo.setId(companyEmployee.getUserId());

        if (companyEmployee.getTitleId() == null) {
            companyEmployeeVo.setTitle("专员");
        } else {
            String title = companyTitleService.findTitleNameById(companyEmployee.getTitleId());
            companyEmployeeVo.setTitle(title);
        }
        List<Long> userIds = new ArrayList<>();
        userIds.add(targetId);
        Result<List<EmployeeInfoVo>> companyEmployeeResult = userInfoFeignClient.findCompanyEmployeeByUserIds(userIds, keyProperties.getKey());
        EmployeeInfoVo employeeInfoVo = companyEmployeeResult.getData().get(0);

        companyEmployeeVo.setUserName(employeeInfoVo.getUserName());
        companyEmployeeVo.setName(employeeInfoVo.getName());
        companyEmployeeVo.setAvatar(employeeInfoVo.getAvatar());

        return companyEmployeeVo;
    }

    // 提取出增加用户尝试次数的方法
    private void incrementUserAttempts(Long userId, int userNum) {
        redisTemplate.opsForValue().set(
                RedisConstant.COMPANY_ADD_USER + userId,
                String.valueOf(userNum + 1),
                ValidateCodeConstant.COMPANY_ADD_CODE_EXPIRATION_TIME,
                TimeUnit.SECONDS
        );
    }

    private String generateVerificationCode() {
        // 生成一个 6 位数字验证码
        int verificationCode = 100000 + ThreadLocalRandom.current().nextInt(900000); // 100000 到 999999
        return String.valueOf(verificationCode);  // 转为字符串
    }

}
