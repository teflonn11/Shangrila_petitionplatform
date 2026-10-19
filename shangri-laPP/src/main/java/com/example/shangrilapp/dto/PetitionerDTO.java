// src/main/java/com/example/shangrilapp/dto/PetitionerDTO.java
package com.example.shangrilapp.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class PetitionerDTO {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "BioID is required")
    private String bioId;

    // Getters
    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public String getBioId() {
        return bioId;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBioId(String bioId) {
        this.bioId = bioId;
    }

    // Constructors
    public PetitionerDTO() {
    }

    public PetitionerDTO(String email, String fullName, LocalDate dateOfBirth, String password, String bioId) {
        this.email = email;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.bioId = bioId;
    }
}