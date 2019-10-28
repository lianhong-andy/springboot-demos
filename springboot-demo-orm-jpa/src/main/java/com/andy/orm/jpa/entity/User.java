package com.andy.orm.jpa.entity;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.andy.orm.jpa.constant.Const;
import com.andy.orm.jpa.entity.base.AbstractAuditModel;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author lianhong
 * @description 用户实体, 继承了父类，父类中有@MappedSupperclass注解，
 *   所以父类中定义的字段为其子类通用，子类中无需再重复定义这些字段
 * @date 2019/9/22 0022下午 12:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orm_user")
@ToString(callSuper = true)
public class User extends AbstractAuditModel {
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
     * 状态，-1：逻辑删除，0：禁用，1：启用
     */
    private Integer status;

    /**
     * 上次登录时间
     */
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    /**
     * @Builder标注生产络合剂的API可以使代码更加简洁优雅。
     * @Builder 允许您使用以下代码自动生成使您的类可实例化所需的代码：
     * 文章链接：https://www.jianshu.com/p/5e42ecede166
     * @param args
     */
    public static void main(String[] args) {
        //可以使用类似于链式编程的操作:
        User andy = User.builder().name("andy").email("123@qq.com").status(1).build();
        System.out.println("andy = " + andy);
    }

    public static void getEncryptedUser(User user) {
        String salt = IdUtil.simpleUUID();
        String pwd = SecureUtil.md5(user.getPassword() + Const.SALT_PREFIX + salt);
        user.setPassword(pwd);
        user.setSalt(salt);
    }
}


