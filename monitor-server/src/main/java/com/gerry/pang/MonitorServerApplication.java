package com.gerry.pang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableTurbine // 启用Turbine
@SpringBootApplication
public class MonitorServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorServerApplication.class, args);
		log.info("===== monitor server start success =====");
	}

}
