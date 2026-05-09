package com.pilipala.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.pilipala"})
@MapperScan(basePackages = {"com.pilipala.mappers"})
@EnableTransactionManagement
@EnableScheduling
public class PilipalaAdminRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(PilipalaAdminRunApplication.class, args);
    }
}
