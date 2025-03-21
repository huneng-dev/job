package cn.hjf.job.user.service;

import cn.hjf.job.model.entity.user.UserInfo;
import cn.hjf.job.model.form.user.*;
import cn.hjf.job.model.dto.user.UserInfoPasswordStatus;
import cn.hjf.job.model.dto.user.UserInfoStatus;
import cn.hjf.job.model.request.user.EmailAndUserTypeRequest;
import cn.hjf.job.model.request.user.PhoneAndUserTypeRequest;
import cn.hjf.job.model.vo.user.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

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
     * 应聘端邮箱注册
     *
     * @param emailRegisterInfoForm 注册信息表单
     * @return 是否注册成功
     */
    boolean candidateRegisterByEmail(EmailRegisterInfoForm emailRegisterInfoForm);


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

    /**
     * 设置用户的身份证信息
     *
     * @param userIdCardInfoForm 用户身份证信息
     * @param userId             用户 id
     * @return 是否成功
     */
    boolean setUserIdCardInfo(UserIdCardInfoForm userIdCardInfoForm, Long userId);

    /**
     * 获取员工信息
     *
     * @param ids 用户 ids
     * @return List<EmployeeInfoVo>
     */
    List<EmployeeInfoVo> findCompanyEmployeeByUserIds(List<Long> ids);

    /**
     * 获取招聘端用户信息
     *
     * @param id 用户id
     * @return RecruiterUserInfoVo
     */
    RecruiterUserInfoVo getRecruiterUserInfo(Long id);

    /**
     * 保存用户头像
     *
     * @param id        用户id
     * @param avatarUrl 用户头像 url
     * @return 是否成功
     */
    boolean saveUserAvatar(Long id, String avatarUrl);

    /**
     * 绑定邮箱
     *
     * @param bindEmailForm 邮箱表单
     * @param id            用户id
     * @return 是否成功
     */
    boolean bindEmail(BindEmailForm bindEmailForm, Long id);

    /**
     * 绑定手机号
     *
     * @param bindPhoneForm 手机号表单
     * @param id            用户 d
     * @return 是否成功
     */
    boolean bindPhone(BindPhoneForm bindPhoneForm, Long id);

    /**
     * 全部基础信息 (已掩盖)
     *
     * @param userId 用户 id
     * @return 用户全部可见的基础信息
     */
    UserInfoAllVo getUserInfoAllVo(Long userId);


    Map<Long, UserInfoAllVo> getUserInfoAllVos(List<Long> userIds);

    PublicUserInfoVo getPublicUserInfoVo(Long userId);
}
