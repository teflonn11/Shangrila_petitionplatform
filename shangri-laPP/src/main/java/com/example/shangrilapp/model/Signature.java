package com.example.shangrilapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "signatures",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"petition_id", "petitioner_id"})
        })
public class Signature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petition_id", nullable = false)
    private Petition petition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petitioner_id", nullable = false)
    private Petitioner petitioner;

    @Column(nullable = false)
    private LocalDateTime signedAt;

    // Getters
    public Long getId() {
        return id;
    }

    public Petition getPetition() {
        return petition;
    }

    public Petitioner getPetitioner() {
        return petitioner;
    }

    public LocalDateTime getSignedAt() {
        return signedAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setPetition(Petition petition) {
        this.petition = petition;
    }

    public void setPetitioner(Petitioner petitioner) {
        this.petitioner = petitioner;
    }

    public void setSignedAt(LocalDateTime signedAt) {
        this.signedAt = signedAt;
    }

    // Constructors
    public Signature() {
        this.signedAt = LocalDateTime.now();
    }

    public Signature(Petition petition, Petitioner petitioner) {
        this.petition = petition;
        this.petitioner = petitioner;
        this.signedAt = LocalDateTime.now();
    }
}