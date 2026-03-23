package com.wtk.takehome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 酒店评论API项目启动类
 * 核心注解@SpringBootApplication：自动配置+组件扫描+启用Spring Boot
 */
@SpringBootApplication
public class TakehomeApplication {

    // 项目入口方法，固定写法
    public static void main(String[] args) {
        // 启动Spring Boot应用，参数1：启动类class，参数2：命令行参数
        SpringApplication.run(TakehomeApplication.class, args);
    }

}