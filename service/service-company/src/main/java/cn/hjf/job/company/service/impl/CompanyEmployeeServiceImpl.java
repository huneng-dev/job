package cn.hjf.job.company.service.impl;

import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.constant.ValidateCodeConstant;
import cn.hjf.job.company.mapper.CompanyEmployeeMapper;
import cn.hjf.job.company.service.CompanyEmployeeService;
import cn.hjf.job.model.entity.company.CompanyEmployee;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
public class CompanyEmployeeServiceImpl extends ServiceImpl<CompanyEmployeeMapper, CompanyEmployee> implements CompanyEmployeeService {

    @Resource
    private CompanyEmployeeMapper companyEmployeeMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

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
