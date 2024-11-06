package cn.hjf.job.user.service.impl;

import cn.hjf.job.common.constant.VerifyInfoConstant;
import cn.hjf.job.model.entity.user.UserInfo;
import cn.hjf.job.model.form.user.EmailPasswordVerifyForm;
import cn.hjf.job.model.form.user.PhonePasswordVerifyForm;
import cn.hjf.job.model.query.user.UserVerifyQuery;
import cn.hjf.job.user.mapper.UserInfoMapper;
import cn.hjf.job.user.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserVerifyQuery phonePasswordVerify(PhonePasswordVerifyForm phonePasswordVerifyForm) {
        // 获取用户信息
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.select(UserInfo::getId, UserInfo::getPassword, UserInfo::getType, UserInfo::getStatus);
        userInfoLambdaQueryWrapper.eq(UserInfo::getPhone, phonePasswordVerifyForm.getPhone());
        UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);

        return VerifyUserInfo(phonePasswordVerifyForm.getPassword(), userInfo);
    }

    @Override
    public UserVerifyQuery emailPasswordVerify(EmailPasswordVerifyForm emailPasswordVerifyForm) {
        // 获取用户信息
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.select(UserInfo::getId, UserInfo::getPassword, UserInfo::getType, UserInfo::getStatus);
        userInfoLambdaQueryWrapper.eq(UserInfo::getPhone, emailPasswordVerifyForm.getEmail());
        UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);

        return VerifyUserInfo(emailPasswordVerifyForm.getPassword(), userInfo);
    }


    /**
     * @param password 用户输入的密码
     * @param userInfo 数据库查询的用户信息
     * @return UserVerifyQuery
     */
    private UserVerifyQuery VerifyUserInfo(String password, UserInfo userInfo) {
        // 判断用户是否存在
        if (userInfo == null)
            return new UserVerifyQuery(null, null, VerifyInfoConstant.USER_NOT_EXIST);

        // 判断用户密码是否正确
        if (!passwordEncoder.matches(password, userInfo.getPassword()))
            return new UserVerifyQuery(null, null, VerifyInfoConstant.PASSWORD_ERROR);

        // 判断用户是否禁用
        if (userInfo.getStatus() == 2)
            return new UserVerifyQuery(null, null, VerifyInfoConstant.USER_DISABLE);

        return new UserVerifyQuery(userInfo.getId(), userInfo.getType(), VerifyInfoConstant.VERIFY_SUCCESS);
    }
}
