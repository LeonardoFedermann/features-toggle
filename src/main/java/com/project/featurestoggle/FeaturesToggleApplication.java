package com.project.featurestoggle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class FeaturesToggleApplication {
//    @Bean
//    public static FlywayMigrationStrategy cleanMigrateStrategy() {
//        return flyway -> {
//            flyway.repair();
//            flyway.migrate();
//        };
//    }

    public static void main(String[] args) {
//        cleanMigrateStrategy();
        SpringApplication.run(FeaturesToggleApplication.class, args);
    }
}
