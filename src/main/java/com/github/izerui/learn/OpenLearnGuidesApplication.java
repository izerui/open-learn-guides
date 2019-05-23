package com.github.izerui.learn;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OpenLearnGuidesApplication {

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(OpenLearnGuidesApplication.class, args);
    }

}
