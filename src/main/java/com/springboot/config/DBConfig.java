package com.springboot.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    // DataSource 설정 (JPA용)
    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3307/bookmarketdb");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("1234");

        // 필요하면 커넥션 풀 옵션 추가
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setPoolName("bookmarketHikariCP");

        return new HikariDataSource(hikariConfig);
    }
}
