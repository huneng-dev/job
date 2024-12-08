package cn.hjf.job.user.service.impl;

import cn.hjf.job.auth.client.UserRoleFeignClient;
import cn.hjf.job.common.constant.RedisConstant;
import cn.hjf.job.common.constant.UserDefaultInfoConstant;
import cn.hjf.job.common.constant.UserTypeConstant;
import cn.hjf.job.common.result.Result;
import cn.hjf.job.model.entity.user.UserInfo;
import cn.hjf.job.model.form.user.*;
import cn.hjf.job.model.dto.user.UserInfoPasswordStatus;
import cn.hjf.job.model.dto.user.UserInfoStatus;
import cn.hjf.job.model.request.auth.DefaultUserRoleRequest;
import cn.hjf.job.model.request.user.EmailAndUserTypeRequest;
import cn.hjf.job.model.request.user.PhoneAndUserTypeRequest;
import cn.hjf.job.model.vo.user.UserInfoVo;
import cn.hjf.job.user.config.KeyProperties;
import cn.hjf.job.user.exception.EmailAlreadyRegisteredException;
import cn.hjf.job.user.exception.PhoneAlreadyRegisterException;
import cn.hjf.job.user.exception.VerificationCodeException;
import cn.hjf.job.user.mapper.UserInfoMapper;
import cn.hjf.job.user.service.UserInfoService;
import cn.hjf.job.user.utils.UsernameGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private UserRoleFeignClient userRoleFeignClient;

    @Resource
    private KeyProperties keyProperties;

    @Override
    public UserInfoVo getUserInfo(Long id) {
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.select(
                UserInfo::getNickname,
                UserInfo::getAvatar,
                UserInfo::getPhone,
                UserInfo::getEmail
        ).eq(UserInfo::getId, id);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);
        maskSensitiveUserInfo(userInfoVo);
        return userInfoVo;
    }

    @Override
    @GlobalTransactional(name = "user-recruiter-register-email", rollbackFor = Exception.class)
    public boolean recruiterRegisterByEmail(EmailRegisterInfoForm emailRegisterInfoForm) {

        String email = emailRegisterInfoForm.getEmail();
        String lockKey = RedisConstant.EMAIL_REGISTER_LOCK_PREFIX + email;  // 以邮箱为 key 获取锁
        RLock lock = redissonClient.getLock(lockKey);  // 创建锁

        try {
            boolean lockAcquired = lock.tryLock(
                    RedisConstant.USER_INFO_OPERATE_LOCK_WAIT_TIME,
                    RedisConstant.USER_INFO_OPERATE_LOCK_LEASE_TIME,
                    TimeUnit.SECONDS
            );

            if (!lockAcquired) {
                return false;  // 若未能获取到锁，则直接返回
            }

            // 检查验证码
            String rawCode = redisTemplate.opsForValue().get(RedisConstant.EMAIL_REGISTER_CODE + email);
            if (rawCode == null || rawCode.isEmpty()) {
                throw new VerificationCodeException("验证码过期！");
            }

            if (!rawCode.equals(emailRegisterInfoForm.getValidateCode())) {
                throw new VerificationCodeException("验证码错误！");
            }

            // 验证成功后立即删除验证码
            redisTemplate.delete(RedisConstant.EMAIL_REGISTER_CODE + email);

            // 检查邮箱是否已注册
            LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(UserInfo::getEmail)
                    .eq(UserInfo::getEmail, email)
                    .eq(UserInfo::getType, 2);

            UserInfo existingUser = userInfoMapper.selectOne(queryWrapper);
            if (existingUser != null) {
                throw new EmailAlreadyRegisteredException("该邮箱已注册！");
            }

            // 保存用户信息
            UserInfo userInfo = new UserInfo();
            userInfo.setAvatar(UserDefaultInfoConstant.RECRUITER_DEFAULT_AVATAR);
            userInfo.setNickname(UsernameGenerator.generateDefaultUsername());
            userInfo.setEmail(email);
            userInfo.setPassword(passwordEncoder.encode(emailRegisterInfoForm.getPassword()));  // 密码加密
            userInfo.setType(UserTypeConstant.RECRUITER);  // 设置为招聘用户

            userInfoMapper.insert(userInfo);

            // 设置用户角色
            Result<String> result = userRoleFeignClient.setDefaultUserRole(
                    new DefaultUserRoleRequest(userInfo.getId(),
                            UserDefaultInfoConstant.RECRUITER_DEFAULT_ROLES,
                            keyProperties.getKey())
            );

            if (!result.getCode().equals(200)) {
                throw new RuntimeException("角色分配失败！");
            }

            return true;
        } catch (VerificationCodeException | EmailAlreadyRegisteredException e) {
            // 特定异常直接抛出
            throw e;
        } catch (InterruptedException e) {
            // 捕获中断异常
            Thread.currentThread().interrupt();  // 保持中断状态
            throw new RuntimeException("操作被中断", e);
        } catch (Exception e) {
            // 捕获其他异常并记录
            throw new RuntimeException("系统错误，请稍后再试", e);
        } finally {
            // 确保锁被释放
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }


    @Override
    @GlobalTransactional(name = "user-recruiter-register-phone", rollbackFor = Exception.class)
    public boolean recruiterRegisterByPhone(PhoneRegisterInfoForm phoneRegisterInfoForm) {

        String phone = phoneRegisterInfoForm.getPhone();
        String lockKey = RedisConstant.PHONE_REGISTER_LOCK_PREFIX + phone;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean flag = lock.tryLock(
                    RedisConstant.USER_INFO_OPERATE_LOCK_WAIT_TIME,
                    RedisConstant.USER_INFO_OPERATE_LOCK_LEASE_TIME,
                    TimeUnit.SECONDS
            );
            if (flag) {
                // 判断验证码是否过期
                String rawCode = redisTemplate.opsForValue().get(RedisConstant.PHONE_REGISTER_CODE + phone);
                if (rawCode == null || rawCode.isEmpty()) {
                    throw new VerificationCodeException("验证码已过期");
                }
                // 判断验证码是否正确
                if (!rawCode.equals(phoneRegisterInfoForm.getValidateCode())) {
                    throw new VerificationCodeException("验证码不正确");
                }
                // 验证码验证成功后删除
                redisTemplate.delete(RedisConstant.PHONE_REGISTER_CODE + phone);

                // 判断手机号是否被注册
                LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userInfoLambdaQueryWrapper
                        .select(UserInfo::getPhone)
                        .eq(UserInfo::getPhone, phone)
                        .eq(UserInfo::getType, UserTypeConstant.RECRUITER);
                UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);
                if (userInfo != null) {
                    throw new PhoneAlreadyRegisterException();
                }

                // 将信息插入数据库
                UserInfo user = new UserInfo();
                user.setPhone(phone);
                user.setAvatar(UserDefaultInfoConstant.RECRUITER_DEFAULT_AVATAR);
                user.setNickname(UsernameGenerator.generateDefaultUsername());
                String encode = passwordEncoder.encode(phoneRegisterInfoForm.getPassword());
                user.setPassword(encode);
                user.setType(UserTypeConstant.RECRUITER);
                userInfoMapper.insert(user);

                // 设置用户角色
                Result<String> result = userRoleFeignClient.setDefaultUserRole(
                        new DefaultUserRoleRequest(user.getId(),
                                UserDefaultInfoConstant.RECRUITER_DEFAULT_ROLES,
                                keyProperties.getKey()
                        )
                );

                if (!result.getCode().equals(200)) {
                    throw new RuntimeException("角色分配失败！");
                }

                return true;
            }
        } catch (VerificationCodeException | PhoneAlreadyRegisterException e) {
            // 特定异常直接抛出
            throw e;
        } catch (InterruptedException e) {
            // 捕获中断异常
            Thread.currentThread().interrupt();  // 保持中断状态
            throw new RuntimeException("操作被中断", e);
        } catch (Exception e) {
            // 捕获其他异常并记录
            log.error("用户注册失败: {}", e.getMessage(), e);
            throw new RuntimeException("系统错误，请稍后再试", e);
        } finally {
            // 确保锁被释放
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
        return false;
    }

    @Override
    @GlobalTransactional(name = "user-candidate-register-phone", rollbackFor = Exception.class)
    public boolean candidateRegisterByPhone(PhoneRegisterInfoForm phoneRegisterInfoForm) {
        String phone = phoneRegisterInfoForm.getPhone();
        String lockKey = RedisConstant.PHONE_REGISTER_LOCK_PREFIX + phone;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean flag = lock.tryLock(
                    RedisConstant.USER_INFO_OPERATE_LOCK_WAIT_TIME,
                    RedisConstant.USER_INFO_OPERATE_LOCK_LEASE_TIME,
                    TimeUnit.SECONDS
            );
            if (flag) {
                // 判断验证码是否过期
                String rawCode = redisTemplate.opsForValue().get(RedisConstant.PHONE_REGISTER_CODE + phone);
                if (rawCode == null || rawCode.isEmpty()) {
                    throw new VerificationCodeException("验证码已过期");
                }
                // 判断验证码是否正确
                if (!rawCode.equals(phoneRegisterInfoForm.getValidateCode())) {
                    throw new VerificationCodeException("验证码不正确");
                }
                // 验证码验证成功后删除
                redisTemplate.delete(RedisConstant.PHONE_REGISTER_CODE + phone);

                // 判断手机号是否被注册
                LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userInfoLambdaQueryWrapper
                        .select(UserInfo::getPhone)
                        .eq(UserInfo::getPhone, phone)
                        .eq(UserInfo::getType, UserTypeConstant.CANDIDATE);
                UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);
                if (userInfo != null) {
                    throw new PhoneAlreadyRegisterException("已注册");
                }

                // 将信息插入数据库
                UserInfo user = new UserInfo();
                user.setPhone(phone);
                user.setAvatar(UserDefaultInfoConstant.CANDIDATE_DEFAULT_AVATAR);
                user.setNickname(UsernameGenerator.generateDefaultUsername());
                // 加密密码
                String encode = passwordEncoder.encode(phoneRegisterInfoForm.getPassword());
                user.setPassword(encode);
                user.setType(UserTypeConstant.CANDIDATE);
                userInfoMapper.insert(user);

                // 设置用户角色
                Result<String> result = userRoleFeignClient.setDefaultUserRole(
                        new DefaultUserRoleRequest(user.getId(),
                                UserDefaultInfoConstant.CANDIDATE_DEFAULT_ROLES,
                                keyProperties.getKey())
                );

                if (!result.getCode().equals(200)) {
                    throw new RuntimeException("角色分配失败！");
                }

                return true;
            }
        } catch (VerificationCodeException | PhoneAlreadyRegisterException e) {
            // 特定异常直接抛出
            throw e;
        } catch (InterruptedException e) {
            // 捕获中断异常
            Thread.currentThread().interrupt();  // 保持中断状态
            throw new RuntimeException("操作被中断", e);
        } catch (Exception e) {
            // 捕获其他异常并记录
            log.error("用户注册失败: {}", e.getMessage(), e);
            throw new RuntimeException("系统错误，请稍后再试", e);
        } finally {
            // 确保锁被释放
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
        return false;
    }

    @Override
    @GlobalTransactional(name = "user-candidate-register-email", rollbackFor = Exception.class)
    public boolean candidateRegisterByEmail(EmailRegisterInfoForm emailRegisterInfoForm) {
        String email = emailRegisterInfoForm.getEmail();
        String lockKey = RedisConstant.EMAIL_REGISTER_LOCK_PREFIX + email;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean flag = lock.tryLock(
                    RedisConstant.USER_INFO_OPERATE_LOCK_WAIT_TIME,
                    RedisConstant.USER_INFO_OPERATE_LOCK_LEASE_TIME,
                    TimeUnit.SECONDS
            );
            if (flag) {
                // 判断验证码是否过期
                String rawCode = redisTemplate.opsForValue().get(RedisConstant.EMAIL_REGISTER_CODE + email);
                if (rawCode == null || rawCode.isEmpty()) {
                    throw new VerificationCodeException("验证码已过期");
                }
                // 判断验证码是否正确
                if (!rawCode.equals(emailRegisterInfoForm.getValidateCode())) {
                    throw new VerificationCodeException("验证码不正确");
                }
                // 验证码验证成功后删除
                redisTemplate.delete(RedisConstant.EMAIL_REGISTER_CODE + email);

                // 判断手机号是否被注册
                LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userInfoLambdaQueryWrapper
                        .select(UserInfo::getEmail)
                        .eq(UserInfo::getEmail, email)
                        .eq(UserInfo::getType, UserTypeConstant.CANDIDATE);
                UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);
                if (userInfo != null) {
                    throw new EmailAlreadyRegisteredException("已注册");
                }

                // 将信息插入数据库
                UserInfo user = new UserInfo();
                user.setEmail(email);
                user.setAvatar(UserDefaultInfoConstant.CANDIDATE_DEFAULT_AVATAR);
                user.setNickname(UsernameGenerator.generateDefaultUsername());
                // 加密密码
                String encode = passwordEncoder.encode(emailRegisterInfoForm.getPassword());
                user.setPassword(encode);
                user.setType(UserTypeConstant.CANDIDATE);
                userInfoMapper.insert(user);

                // 设置用户角色
                Result<String> result = userRoleFeignClient.setDefaultUserRole(
                        new DefaultUserRoleRequest(user.getId(),
                                UserDefaultInfoConstant.CANDIDATE_DEFAULT_ROLES,
                                keyProperties.getKey())
                );

                if (!result.getCode().equals(200)) {
                    throw new RuntimeException("角色分配失败！");
                }

                return true;
            }
        } catch (VerificationCodeException | EmailAlreadyRegisteredException e) {
            // 特定异常直接抛出
            throw e;
        } catch (InterruptedException e) {
            // 捕获中断异常
            Thread.currentThread().interrupt();  // 保持中断状态
            throw new RuntimeException("操作被中断", e);
        } catch (Exception e) {
            // 捕获其他异常并记录
            log.error("用户注册失败: {}", e.getMessage(), e);
            throw new RuntimeException("系统错误，请稍后再试", e);
        } finally {
            // 确保锁被释放
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
        return false;
    }

    @Override
    public UserInfoStatus getUserInfoStatusByEmailCode(EmailAndUserTypeRequest emailAndUserTypeRequest) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserInfo::getId, UserInfo::getStatus);
        queryWrapper.eq(UserInfo::getEmail, emailAndUserTypeRequest.getEmail()).eq(UserInfo::getType, emailAndUserTypeRequest.getUserType());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (userInfo == null) {
            return null;
        }
        return new UserInfoStatus(userInfo.getId(), userInfo.getStatus());
    }

    @Override
    public UserInfoStatus getUserInfoStatusByPhoneCode(PhoneAndUserTypeRequest phoneAndUserTypeRequest) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserInfo::getId, UserInfo::getStatus);
        queryWrapper.eq(UserInfo::getPhone, phoneAndUserTypeRequest.getPhone()).eq(UserInfo::getType, phoneAndUserTypeRequest.getUserType());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (userInfo == null) {
            return null;
        }
        return new UserInfoStatus(userInfo.getId(), userInfo.getStatus());
    }

    @Override
    public UserInfoPasswordStatus getUserInfoPasswordStatusByEmailPassword(EmailAndUserTypeRequest emailAndUserTypeRequest) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserInfo::getId, UserInfo::getPassword, UserInfo::getStatus);
        queryWrapper.eq(UserInfo::getEmail, emailAndUserTypeRequest.getEmail()).eq(UserInfo::getType, emailAndUserTypeRequest.getUserType());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (userInfo == null) {
            return null;
        }
        return new UserInfoPasswordStatus(userInfo.getId(), userInfo.getPassword(), userInfo.getStatus());
    }

    @Override
    public UserInfoPasswordStatus getUserInfoPasswordStatusByPhonePassword(PhoneAndUserTypeRequest phoneAndUserTypeRequest) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserInfo::getId, UserInfo::getPassword, UserInfo::getStatus);
        queryWrapper.eq(UserInfo::getPhone, phoneAndUserTypeRequest.getPhone()).eq(UserInfo::getType, phoneAndUserTypeRequest.getUserType());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (userInfo == null) {
            return null;
        }
        return new UserInfoPasswordStatus(userInfo.getId(), userInfo.getPassword(), userInfo.getStatus());
    }

    /**
     * 对用户信息进行遮蔽或脱敏操作，防止敏感数据泄露。
     *
     * @param userInfoVo 用户信息
     */
    private void maskSensitiveUserInfo(UserInfoVo userInfoVo) {
        if (userInfoVo.getEmail() != null) {
            userInfoVo.setEmail(maskEmail(userInfoVo.getEmail()));
        }

        if (userInfoVo.getPhone() != null) {
            userInfoVo.setPhone(maskPhone(userInfoVo.getPhone()));
        }
        // TODO 后续自行扩展
    }

    //模糊邮箱
    private String maskEmail(String email) {

        String[] parts = email.split("@");
        if (parts.length == 2) {
            String localPart = parts[0];
            if (localPart.length() > 3) {
                return localPart.substring(0, 3) + "****@" + parts[1];
            } else {
                return localPart.charAt(0) + "****" + parts[1];
            }
        }
        return email;
    }

    // 模糊手机号
    private String maskPhone(String phone) {
        // 将手机号脱敏，保留前3后4位，中间的部分用星号代替
        if (phone != null && phone.length() == 11) {
            return phone.substring(0, 3) + "****" + phone.substring(7);
        }
        return phone;
    }

}
