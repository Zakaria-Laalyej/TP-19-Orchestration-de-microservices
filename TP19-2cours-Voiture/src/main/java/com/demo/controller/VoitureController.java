
package com.demo.controller;

import com.demo.entities.Voiture;
import com.demo.entities.Client;
import com.demo.service.ClientService;
import com.demo.repository.VoitureRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.List;

@RestController
public class VoitureController {

    @Autowired
    private VoitureRepository voitureRepository;

    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/voitures", produces = "application/json")
    public ResponseEntity<Object> findAll() {
        try {
            List<Voiture> voitures = voitureRepository.findAll();
            return ResponseEntity.ok(voitures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching voitures: " + e.getMessage());
        }
    }

    @GetMapping("/voitures/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        try {
            Voiture voiture = voitureRepository.findById(id)
                    .orElseThrow(() -> new Exception("Voiture Introuvable"));

            voiture.setClient(clientService.clientById(voiture.getId_client()));

            return ResponseEntity.ok(voiture);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Voiture not found with ID: " + id);
        }
    }

    @GetMapping("/voitures/client/{id}")
    public ResponseEntity<List<Voiture>> findByClient(@PathVariable Long id) {
        try {
            Client client = clientService.clientById(id);

            if (client != null) {
                List<Voiture> voitures =
                        voitureRepository.findVoituresByClient(id);

                return ResponseEntity.ok(voitures);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/voitures/{id_client}")
    public ResponseEntity<Object> save(
            @PathVariable Long id_client,
            @RequestBody Voiture voiture) {
        try {
            Client client = clientService.clientById(id_client);

            if (client != null) {
                voiture.setClient(client);
                voiture.setId_client(id_client);

                Voiture savedVoiture = voitureRepository.save(voiture);

                return ResponseEntity.ok(savedVoiture);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Client not found with ID: " + id_client);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving voiture: " + e.getMessage());
        }
    }

    @PutMapping("/voitures/{id}")
    public ResponseEntity<Object> update(
            @PathVariable Long id,
            @RequestBody Voiture updatedVoiture) {

        try {
            Voiture existingVoiture = voitureRepository.findById(id)
                    .orElseThrow(() -> new Exception("Voiture not found"));

            if (updatedVoiture.getMatricule() != null
                    && !updatedVoiture.getMatricule().isEmpty()) {
                existingVoiture.setMatricule(updatedVoiture.getMatricule());
            }

            if (updatedVoiture.getMarque() != null
                    && !updatedVoiture.getMarque().isEmpty()) {
                existingVoiture.setMarque(updatedVoiture.getMarque());
            }

            if (updatedVoiture.getModel() != null
                    && !updatedVoiture.getModel().isEmpty()) {
                existingVoiture.setModel(updatedVoiture.getModel());
            }

            Voiture savedVoiture = voitureRepository.save(existingVoiture);

            return ResponseEntity.ok(savedVoiture);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating voiture: " + e.getMessage());
        }
    }
}
