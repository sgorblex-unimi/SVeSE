package org.miniblex.svese;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SVeSE {
	public static String name = "SVeSE";
	public static String description = "Sistema di Voto e Scrutinio Elettronico";

	public static void main(String[] args) {
		SpringApplication.run(SVeSE.class, args);
	}

}
