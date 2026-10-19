package com.example.shangrilapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SignatureDTO {
    private Long id;
    private Long petitionId;
    private String petitionerEmail;
    private LocalDateTime signedAt;

    // Constructor for creating a new signature
    public SignatureDTO(Long petitionId, String petitionerEmail) {
        this.petitionId = petitionId;
        this.petitionerEmail = petitionerEmail;
        this.signedAt = LocalDateTime.now();
    }

    // Add validation method to check if petitioner has already signed
    public boolean isValid() {
        return petitionId != null && petitionerEmail != null && !petitionerEmail.trim().isEmpty();
    }
}