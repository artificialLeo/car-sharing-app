//package com.app.car;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
//import org.springframework.context.annotation.Bean;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.utility.DockerImageName;
//
//@TestConfiguration(proxyBeanMethods = false)
//public class TestCarApplication {
//
//	@Bean
//	@ServiceConnection
//	MySQLContainer<?> mysqlContainer() {
//		return new MySQLContainer<>(DockerImageName.parse("mysql:latest"));
//	}
//
//	public static void main(String[] args) {
//		SpringApplication.from(CarApplication::main).with(TestCarApplication.class).run(args);
//	}
//
//}
