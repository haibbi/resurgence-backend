package tr.com.milia.resurgence;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class ResurgenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResurgenceApplication.class, args);
    }

}
