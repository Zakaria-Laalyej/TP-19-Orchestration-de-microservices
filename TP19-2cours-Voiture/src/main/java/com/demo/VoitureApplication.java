
package com.demo;

import com.demo.entities.Client;
import com.demo.entities.Voiture;
import com.demo.repository.VoitureRepository;
import com.demo.service.ClientService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class VoitureApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoitureApplication.class, args);
    }

    @Bean
    CommandLineRunner initialiserBaseH2(
            VoitureRepository voitureRepository,
            ClientService clientService) {

        return args -> {
            try {
                Client c1 = clientService.clientById(2L);
                Client c2 = clientService.clientById(1L);

                if (c1 == null || c2 == null) {
                    System.err.println("Client introuvable !");
                    return;
                }

                System.out.println("Client 1 : " + c2.getNom());
                System.out.println("Client 2 : " + c1.getNom());

                voitureRepository.save(
                        new Voiture(null, "AlfaRomeo", "A 25 333", "Corolla", 1L, c2)
                );

                voitureRepository.save(
                        new Voiture(null, "Renault", "B 6 3456", "Megane", 1L, c2)
                );

                voitureRepository.save(
                        new Voiture(null, "Peugeot", "A 55 4444", "301", 2L, c1)
                );

                System.out.println("Nombre de voitures enregistrées : "
                        + voitureRepository.count());

            } catch (Exception e) {
                System.err.println("Erreur lors de l'initialisation : "
                        + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
