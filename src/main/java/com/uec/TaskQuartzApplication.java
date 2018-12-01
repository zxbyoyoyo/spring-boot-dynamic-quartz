package com.uec;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.uec.quartz.mapper")
public class TaskQuartzApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskQuartzApplication.class, args);
	}
}
