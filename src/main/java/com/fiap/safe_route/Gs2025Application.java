package com.fiap.safe_route;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Gs2025Application {

	public static void main(String[] args) {
		if (System.getenv("ENV") == null || "dev".equals(System.getenv("ENV"))) {
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		}
		SpringApplication.run(Gs2025Application.class, args);
	}

}
