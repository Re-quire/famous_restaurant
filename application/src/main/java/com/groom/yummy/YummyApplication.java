package com.groom.yummy;

import com.groom.yummy.exception.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

@SpringBootApplication
 public class YummyApplication {
    public static void main(String[] args){ SpringApplication.run(YummyApplication.class, args); }
}
