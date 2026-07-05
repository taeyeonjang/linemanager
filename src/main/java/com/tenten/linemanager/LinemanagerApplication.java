package com.tenten.linemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LinemanagerApplication {

	public static void main(String[] args) {
		//깃허브 수정 확인
		SpringApplication.run(LinemanagerApplication.class, args);
	}

}
