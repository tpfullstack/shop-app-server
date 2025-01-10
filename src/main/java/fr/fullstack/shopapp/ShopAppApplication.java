package fr.fullstack.shopapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableElasticsearchRepositories(basePackages = "fr.fullstack.shopapp.repository.elastic")
@EnableJpaRepositories(basePackages = "fr.fullstack.shopapp.repository.jpa")
@SpringBootApplication
public class ShopAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopAppApplication.class, args);
    }
}
