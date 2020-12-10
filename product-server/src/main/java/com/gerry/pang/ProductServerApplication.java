package com.gerry.pang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableDiscoveryClient
@EnableFeignClients // feign支持
@EnableCircuitBreaker // 断路器支持
@SpringBootApplication
public class ProductServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServerApplication.class, args);
		log.info("******** product server start success ********");
	}

}
