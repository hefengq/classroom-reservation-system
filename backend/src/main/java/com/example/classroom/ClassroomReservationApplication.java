package com.example.classroom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@MapperScan("com.example.classroom.mapper")
public class ClassroomReservationApplication {
  public static void main(String[] args) {
    SpringApplication.run(ClassroomReservationApplication.class, args);
  }
}
