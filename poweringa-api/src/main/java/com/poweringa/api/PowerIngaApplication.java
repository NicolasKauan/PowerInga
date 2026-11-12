package com.poweringa.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PowerIngaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PowerIngaApplication.class, args);
	}

}
