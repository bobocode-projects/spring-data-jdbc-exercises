package com.bobocode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The exercise is powered by this Spring Boot application. It gets all the auto configuration for JDBC, Spring Data
 * JDBC and in-memory database H2. On startup Spring will also create a required database table using Flyway
 * migration script. You can check it under "resources/db/migration" folder. In order to complete the task you need to
 * address all the todo comments.
 */
@SpringBootApplication
public class SpringDataJdbcExercisesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJdbcExercisesApplication.class, args);
    }

}
