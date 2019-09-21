package com.andy.orm.jdbctemplate.entity;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.andy.orm.jdbctemplate.constant.Const;
import com.andy.orm.jdbctemplate.annotation.Column;
import com.andy.orm.jdbctemplate.annotation.PK;
import com.andy.orm.jdbctemplate.annotation.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lianhong
 * @description 用户实体类
 * @date 2019/9/19 0019下午 10:36
 */
@Data
@Table(name = "orm_user")
public class User implements Serializable {
    /**
     * 主键
     */
    @PK
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
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * 状态，-1:逻辑删除，0：禁用，1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 上次登录时间
     */
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    /**
     * 上次更新时间
     */
    @Column(name = "last_update_time")
    private Date lastUpdateTime;

    public static void getEncryptedUser(User user) {
        String salt = IdUtil.simpleUUID();
        String pwd = SecureUtil.md5(user.getPassword() + Const.SALT_PREFIX + salt);
        user.setPassword(pwd);
        user.setSalt(salt);
    }

}
