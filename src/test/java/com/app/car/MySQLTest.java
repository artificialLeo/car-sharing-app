//package com.app.car;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//@SpringBootTest
//@Testcontainers
//public class MySQLTest {
//
//    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8");
//
//    @DynamicPropertySource
//    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", mysql::getJdbcUrl);
//        registry.add("spring.datasource.username", mysql::getUsername);
//        registry.add("spring.datasource.password", mysql::getPassword);
//    }
//
//
//    @Test
//    public void test() {
//        mysql.start();
//    }
//}
