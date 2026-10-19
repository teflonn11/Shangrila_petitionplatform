package com.example.shangrilapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "petitions")
public class Petition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petitioner_id", nullable = false)
    private Petitioner petitioner;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String status = "open"; // open or closed

    private String response;

    @Column(nullable = false)
    private Integer signatureCount = 0;

    @OneToMany(mappedBy = "petition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Signature> signatures = new HashSet<>();

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Petitioner getPetitioner() {
        return petitioner;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStatus() {
        return status;
    }

    public String getResponse() {
        return response;
    }

    public Integer getSignatureCount() {
        return signatureCount;
    }

    public Set<Signature> getSignatures() {
        return signatures;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPetitioner(Petitioner petitioner) {
        this.petitioner = petitioner;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setSignatureCount(Integer signatureCount) {
        this.signatureCount = signatureCount;
    }

    public void setSignatures(Set<Signature> signatures) {
        this.signatures = signatures;
    }

    public void addSignature(Signature signature) {
        signatures.add(signature);
        signature.setPetition(this);
        this.signatureCount++;
    }

    // Constructor
    public Petition() {
    }

    public Petition(String title, String content, Petitioner petitioner) {
        this.title = title;
        this.content = content;
        this.petitioner = petitioner;
        this.createdAt = LocalDateTime.now();
        this.status = "open";
        this.signatureCount = 0;
    }
}