package com.gerry.pang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableZuulProxy
@SpringBootApplication
public class ZuulServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulServerApplication.class, args);
		log.info("===== zuul server start success =====");
	}

}
