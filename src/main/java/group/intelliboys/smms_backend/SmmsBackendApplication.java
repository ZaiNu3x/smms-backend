package group.intelliboys.smms_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SmmsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmmsBackendApplication.class, args);
	}

}
