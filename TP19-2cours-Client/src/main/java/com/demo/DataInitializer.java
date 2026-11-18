package com.demo;


import com.demo.entite.Client;
import com.demo.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initialiserBaseH2(
            ClientRepository clientRepository) {

        return args -> {
            clientRepository.save(
                    new Client(null, "Rabab Benani", 23f));

            clientRepository.save(
                    new Client(null, "Adil RAMI", 22f));

            clientRepository.save(
                    new Client(null, "Mehdi SAFI", 22f));
        };
    }
}