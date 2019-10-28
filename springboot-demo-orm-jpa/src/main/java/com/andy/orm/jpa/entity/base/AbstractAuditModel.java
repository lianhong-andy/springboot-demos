package com.andy.orm.jpa.entity.base;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lianhong
 * @description 实体通用父类
 * @date 2019/9/22 0022下午 12:25
 */

/**
 * @MappedSuperclass的作用
 * 在Jpa里, 当我们在定义多个实体类时, 可能会遇到这几个实体类都有几个共同的属性, 这时就会出现很多重复代码.
 * 这时我们可以选择编写一个父类,将这些共同属性放到这个父类中, 并且在父类上加上@MappedSuperclass注解.
 * 注意:标注为@MappedSuperclass的类将不是一个完整的实体类，他将不会映射到数据库表，但是他的属性都将映射到其子类的数据库字段中。
 *  标注为@MappedSuperclass的类不能再标注@Entity或@Table注解，也无需实现序列化接口.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class AbstractAuditModel/* implements Serializable*/ {
    /**主键*/
    @Id
    /**
     * JPA提供的四种标准用法为TABLE,SEQUENCE,IDENTITY,AUTO. 
     * TABLE：使用一个特定的数据库表格来保存主键。 
     * SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。 
     * IDENTITY：主键由数据库自动生成（主要是自动增长型） 
     * AUTO：主键由程序控制。 
     * */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**创建时间*/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time",nullable = false,updatable = false)
    @CreatedDate
    private Date createTime;

    /**上次更新时间*/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update_time", nullable=false)
    @LastModifiedDate
    private Date lastUpdateTime;

}

/**
 *@Temporal标签的作用很简单：
 * 1、如果在某类中有Date类型的属性，数据库中存储可能是'yyyy-MM-dd hh:MM:ss'要在查询时获得年月日，在该属性上标注@Temporal(TemporalType.DATE) 会得到形如'yyyy-MM-dd' 格式的日期。
 * DATE ：等于java.sql.Date
 * 日期：
 * @Temporal(TemporalType.DATE)
 *   @Column(name = "applyDate", nullable = false, length = 12)
 *   public Date getApplyDate() {
 *    return applyDate;
 *  }
 * 在页面端取值：2016--09--28
 *
 * 2、如果在某类中有Date类型的属性，数据库中存储可能是'yyyy-MM-dd hh:MM:ss'要获得时分秒，在该属性上标注 @Temporal(TemporalType.TIME) 会得到形如'HH:MM:SS' 格式的日期。
 * TIME ：等于java.sql.Time 
 * 时间：
 * @Temporal(TemporalType.TIME)
 * 在页面端取值：15:50:30
 *
 * 3、如果在某类中有Date类型的属性，数据库中存储可能是'yyyy-MM-dd hh:MM:ss'要获得'是'yyyy-MM-dd hh:MM:ss'，在该属性上标注 @Temporal(TemporalType.TIMESTAMP) 会得到形如'HH:MM:SS' 格式的日期
 * TIMESTAMP ：等于java.sql.Timestamp 
 * 日期和时间(默认)：
 * @Temporal(TemporalType.TIMESTAMP)
 * 在页面端取值：2016-09-28 15:52:32:000
 *
 * 在jsp里控制不显示毫秒：
 * <td align="center">&nbsp;<fmt:formatDate value="${list[0].createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
 * */


/**
 * JPA Audit
 * 在spring jpa中，支持在字段或者方法上进行注解@CreatedDate、@CreatedBy、@LastModifiedDate、@LastModifiedBy，从字面意思可以很清楚的了解，这几个注解的用处。
 * @CreatedDate
 * 表示该字段为创建时间时间字段，在这个实体被insert的时候，会设置值
 * @CreatedBy
 * 表示该字段为创建人，在这个实体被insert的时候，会设置值
 * @LastModifiedDate、@LastModifiedBy同理。
 *
 * 如何使用？
 * 首先申明实体类，需要在类上加上注解@EntityListeners(AuditingEntityListener.class)，其次在application启动类中加上注解EnableJpaAuditing，
 * 同时在需要的字段上加上@CreatedDate、@CreatedBy、@LastModifiedDate、@LastModifiedBy等注解。
 * 这个时候，在jpa.save方法被调用的时候，时间字段会自动设置并插入数据库，但是CreatedBy和LastModifiedBy并没有赋值，
 * 因为需要实现AuditorAware接口来返回你需要插入的值。
 */

/**
 * 原文中提到的大致有以下几点：
 * 1. 此注解会生成equals(Object other) 和 hashCode()方法。
 * 2. 它默认使用非静态，非瞬态的属性
 * 3. 可通过参数exclude排除一些属性
 * 4. 可通过参数of指定仅使用哪些属性
 * 5. 它默认仅使用该类中定义的属性且不调用父类的方法
 * 6. 可通过callSuper=true解决上一点问题。让其生成的方法中调用父类的方法。
 *
 * 另：@Data相当于@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode这5个注解的合集。
 *
 * 通过官方文档，可以得知，当使用@Data注解时，则有了@EqualsAndHashCode注解，那么就会在此类中存在equals(Object other)
 * 和 hashCode()方法，且不会使用父类的属性，这就导致了可能的问题。
 * 比如，有多个类有相同的部分属性，把它们定义到父类中，恰好id（数据库主键）也在父类中，那么就会存在部分对象在比较时，
 * 它们并不相等，却因为lombok自动生成的equals(Object other) 和 hashCode()方法判定为相等，从而导致出错。
 *
 * 修复此问题的方法很简单：
 * 1. 使用@Getter @Setter @ToString代替@Data并且自定义equals(Object other) 和 hashCode()方法，比如有些类只需要判断主键id是否相等即足矣。
 * 2. 或者使用在使用@Data时同时加上@EqualsAndHashCode(callSuper=true)注解。
 */
