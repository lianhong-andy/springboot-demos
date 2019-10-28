package com.andy.orm.mybatis.entity;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.andy.orm.mybatis.constant.Const;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lianhong
 * @description
 * @date 2019/10/28 0028下午 7:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = -1840831686851699943L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 加密后的密码
     */
    private String password;

    /**
     * 加密使用的盐
     */
    private String salt;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 状态，-1：逻辑删除，0：禁用，1：启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 上次登录时间
     */
    private Long lastLoginTime;

    /**
     * 上次更新时间
     */
    private Long lastUpdateTime;

    public static void getEncryptedUser(User user) {
        String salt = IdUtil.simpleUUID();
        String pwd = SecureUtil.md5(user.getPassword() + Const.SALT_PREFIX + salt);
        user.setPassword(pwd);
        user.setSalt(salt);
    }
}
