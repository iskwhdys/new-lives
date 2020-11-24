package com.iskwhdys.newlives;

import com.iskwhdys.newlives.infra.config.AppConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	public AppConfig appConfig() {
		return new AppConfig();
	}

}
