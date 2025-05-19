package com.asusoftware.DentaTrack_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DentaTrackBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DentaTrackBackendApplication.class, args);
	}

}
