package cn.hjf.job.user.service;

import cn.hjf.job.model.entity.user.UserInfo;
import cn.hjf.job.model.form.user.EmailPasswordVerifyForm;
import cn.hjf.job.model.form.user.PhonePasswordVerifyForm;
import cn.hjf.job.model.query.user.UserVerifyQuery;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjf
 * @since 2024-10-31
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     *  校验手机号登录方式的密码
     * @param phonePasswordVerifyForm  phonePasswordVerifyForm
     * @return UserVerifyQuery
     */
    UserVerifyQuery phonePasswordVerify(PhonePasswordVerifyForm phonePasswordVerifyForm);

    /**
     *  校验邮箱登录方式的密码
     * @param emailPasswordVerifyForm  emailPasswordVerifyForm
     * @return UserVerifyQuery
     */
    UserVerifyQuery emailPasswordVerify(EmailPasswordVerifyForm emailPasswordVerifyForm);

}
