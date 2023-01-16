package ru.company.settings.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class SettingsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SettingsApiApplication.class, args);
	}
}
