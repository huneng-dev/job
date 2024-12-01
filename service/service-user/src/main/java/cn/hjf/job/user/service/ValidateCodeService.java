package cn.hjf.job.user.service;

import cn.hjf.job.common.result.Result;

/**
 * 验证码服务类
 *
 * @author hjf
 * @version 1.0
 * @description
 */

public interface ValidateCodeService {


    /**
     * 发送邮件注册验证码
     *
     * @param to 目标邮箱
     * @return Result<String>
     */
    Result<String> sendEmailRegisterValidateCode(String to);

    /**
     * 发送邮件找回密码验证码
     *
     * @param to 目标邮箱
     * @return Result<String>
     */
    Result<String> sendEmailRecoveryValidateCode(String to);

    /**
     * 发送邮件登录验证码
     *
     * @param to 目标邮箱
     * @return Result<String>
     */
    Result<String> sendEmailLoginValidateCode(String to);


    /**
     * 发送手机号注册验证码
     *
     * @param phone 目标手机号
     * @return Result<String>
     */
    Result<String> sendPhoneRegisterValidateCode(String phone);

    /**
     * 发送手机号找回密码验证码
     *
     * @param phone 目标手机号
     * @return Result<String>
     */
    Result<String> sendPhoneRecoveryValidateCode(String phone);

    /**
     * 发送手机号登录验证码
     *
     * @param phone 目标手机号
     * @return Result<String>
     */
    Result<String> sendPhoneLoginValidateCode(String phone);


}
