package tr.com.milia.resurgence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@EnableAsync
@SpringBootApplication
public class ResurgenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResurgenceApplication.class, args);
	}
}
