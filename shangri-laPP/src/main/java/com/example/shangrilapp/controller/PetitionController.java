package com.example.shangrilapp.controller;

import com.example.shangrilapp.dto.PetitionDTO;
import com.example.shangrilapp.model.Petition;
import com.example.shangrilapp.service.PetitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/petitions")
@RequiredArgsConstructor
public class PetitionController {
    private final PetitionService petitionService;

//    @PostMapping
//    public ResponseEntity<Petition> createPetition(@Valid @RequestBody PetitionDTO petitionDTO, Authentication authentication) {
//        return ResponseEntity.ok(petitionService.createPetition(petitionDTO, authentication));
//    }

//    @PostMapping
//    public ResponseEntity<?> createPetition(
//            @Valid @RequestBody PetitionDTO petitionDTO,
//            Authentication authentication) {
//        try {
//            System.out.println("Received petition: " + petitionDTO); // Debug log
//            Petition petition = petitionService.createPetition(petitionDTO, authentication);
//            return ResponseEntity.ok(petition);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                    .body(Map.of("message", e.getMessage()));
//        }
//    }

    @PostMapping
    public ResponseEntity<?> createPetition(
            @Valid @RequestBody PetitionDTO petitionDTO,
            Authentication authentication) {
        try {
            // Debug log
            System.out.println("Received petition request: " + petitionDTO);
            System.out.println("Authentication: " + authentication);

            if (petitionDTO.getTitle() == null || petitionDTO.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Title is required"));
            }

            if (petitionDTO.getContent() == null || petitionDTO.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Content is required"));
            }

            Petition petition = petitionService.createPetition(petitionDTO, authentication);
            return ResponseEntity.ok(petition);

        } catch (Exception e) {
            // Debug log
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }


    @GetMapping
    public ResponseEntity<List<Petition>> getAllPetitions(
            @RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok(petitionService.getPetitionsByStatus(status));
        }
        return ResponseEntity.ok(petitionService.getAllPetitions());
    }

    @PostMapping("/{petitionId}/sign")
    public ResponseEntity<?> signPetition(@PathVariable Long petitionId) {
        petitionService.signPetition(petitionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{petitionId}")
    public ResponseEntity<Petition> getPetition(@PathVariable Long petitionId) {
        return ResponseEntity.ok(petitionService.getPetition(petitionId));
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getPetitionStatistics(Authentication authentication) {
        return ResponseEntity.ok(petitionService.getPetitionStatistics(authentication));
    }
}
