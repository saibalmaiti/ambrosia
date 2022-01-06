package com.ambrosia.main;

import com.ambrosia.main.firebase.config.FirebaseServiceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@Slf4j
public class MainApplication {

	public static void main(String[] args) {
		try {
			FirebaseServiceConfig.configureFirebaseService();
		}
		catch (IOException e) {
			log.error("Cannot open firebaseconfiguration json" + e.getMessage());
		}

		SpringApplication.run(MainApplication.class, args);
	}


}
