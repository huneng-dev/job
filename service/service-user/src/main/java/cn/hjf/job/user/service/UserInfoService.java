package cn.hjf.job.user.service;

import cn.hjf.job.model.entity.user.UserInfo;
import cn.hjf.job.model.form.user.*;
import cn.hjf.job.model.query.user.UserInfoPasswordStatus;
import cn.hjf.job.model.query.user.UserInfoStatus;
import cn.hjf.job.model.request.user.EmailAndUserTypeRequest;
import cn.hjf.job.model.request.user.PhoneAndUserTypeRequest;
import cn.hjf.job.model.vo.user.UserInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 获取用户信息 (去敏)
     *
     * @param id 用户id
     * @return UserInfoQuery
     */
    UserInfoVo getUserInfo(Long id);


    /**
     * 招聘端通过邮箱注册
     *
     * @param emailRegisterInfoForm 注册信息表单
     * @return 是否注册成功
     */
    boolean recruiterRegisterByEmail(EmailRegisterInfoForm emailRegisterInfoForm);

    /**
     * 招聘端通过手机号注册
     *
     * @param phoneRegisterInfoForm 注册信息表单
     * @return 是否注册成功
     */
    boolean recruiterRegisterByPhone(PhoneRegisterInfoForm phoneRegisterInfoForm);

    /**
     * 应聘端手机号注册
     *
     * @param phoneRegisterInfoForm 注册信息表单
     * @return 是否注册成功
     */
    boolean candidateRegisterByPhone(PhoneRegisterInfoForm phoneRegisterInfoForm);


    /**
     * 邮件验证码方式获取用户信息
     *
     * @param emailAndUserTypeRequest EmailAndUserTypeRequest
     * @return UserInfoStatus
     */
    UserInfoStatus getUserInfoStatusByEmailCode(EmailAndUserTypeRequest emailAndUserTypeRequest);

    /**
     * 手机验证码方式获取用户信息
     *
     * @param phoneAndUserTypeRequest PhoneAndUserTypeRequest
     * @return UserInfoStatus
     */
    UserInfoStatus getUserInfoStatusByPhoneCode(PhoneAndUserTypeRequest phoneAndUserTypeRequest);

    /**
     * 邮箱密码方式获取用户信息
     *
     * @param emailAndUserTypeRequest EmailAndUserTypeRequest
     * @return UserInfoPasswordStatus
     */
    UserInfoPasswordStatus getUserInfoPasswordStatusByEmailPassword(EmailAndUserTypeRequest emailAndUserTypeRequest);

    /**
     * 手机号密码获取用户信息
     *
     * @param phoneAndUserTypeRequest PhoneAndUserTypeRequest
     * @return UserInfoPasswordStatus
     */
    UserInfoPasswordStatus getUserInfoPasswordStatusByPhonePassword(PhoneAndUserTypeRequest phoneAndUserTypeRequest);

}
