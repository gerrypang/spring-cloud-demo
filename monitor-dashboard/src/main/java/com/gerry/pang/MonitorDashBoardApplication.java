package com.gerry.pang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableHystrixDashboard
@SpringBootApplication
public class MonitorDashBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorDashBoardApplication.class, args);
		log.info("===== monitor dashboard start success =====");
	}

}
