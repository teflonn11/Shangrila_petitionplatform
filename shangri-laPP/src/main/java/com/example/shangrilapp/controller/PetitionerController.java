package com.example.shangrilapp.controller;

import com.example.shangrilapp.dto.PetitionerDTO;
import com.example.shangrilapp.service.PetitionerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/petitioners")
public class PetitionerController {
    @Autowired
    private PetitionerService petitionerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody PetitionerDTO dto) {
        try {
            petitionerService.register(dto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/statistics")
    public ResponseEntity<?> getPetitionerStatistics(Authentication authentication) {
        return ResponseEntity.ok(petitionerService.getStatistics(authentication.getName()));
    }

    @GetMapping("/petitions")
    public ResponseEntity<?> getPetitionerPetitions(Authentication authentication) {
        return ResponseEntity.ok(petitionerService.getPetitions(authentication.getName()));
    }
}

