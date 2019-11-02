package com.andy.orm.beetsql.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @author lianhong
 * @description
 * @date 2019/11/1 0001下午 7:49
 */
@Configuration
public class BeetConfig {
    /**
     * Beetl需要显示的配置数据源，方可启动项目，大坑，切记！
     * @param env
     * @return
     */
    @Bean(name = "datasource")
    public DataSource getDataSource(Environment env) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;

    }
}
