package com.example.shangrilapp.controller;

import com.example.shangrilapp.service.CommitteeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/committee")
@RequiredArgsConstructor
@PreAuthorize("hasRole('COMMITTEE')")

public class CommitteeController {
    private final CommitteeService committeeService;

    @PostMapping("/threshold")
    public ResponseEntity<?> setSignatureThreshold(@RequestParam int threshold) {
        committeeService.setSignatureThreshold(threshold);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/petitions/{petitionId}/response")
    public ResponseEntity<?> respondToPetition(
            @PathVariable Long petitionId,
            @RequestBody String response) {
        committeeService.respondToPetition(petitionId, response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getPetitionStatistics() {
        return ResponseEntity.ok(committeeService.getPetitionStatistics());
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingPetitions() {
        return ResponseEntity.ok(committeeService.getPendingPetitions());
    }
}
