package pham.com.doconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"pham.com.patients.repository",
										"pham.com.doctors.repository",
										"pham.com.appointments.repository"})
@SpringBootApplication(scanBasePackages = {"pham.com"})
@EntityScan(basePackages = {"pham.com.patients.model",
							"pham.com.doctors.model",
							"pham.com.appointments",
							"pham.com.doconnect"})
public class DoconnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoconnectApplication.class, args);
	}

}
