package nl.fontys.s3.rentride_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RentRideBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentRideBeApplication.class, args);
    }

}
