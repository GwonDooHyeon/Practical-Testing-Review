package sample.cafekioskreview.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CafekioskReviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafekioskReviewApplication.class, args);
    }

}
