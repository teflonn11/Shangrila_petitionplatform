package com.example.shangrilapp.service;

import com.example.shangrilapp.dto.PetitionDTO;
import com.example.shangrilapp.exception.CustomException;
import com.example.shangrilapp.model.Petition;
import com.example.shangrilapp.model.Petitioner;
import com.example.shangrilapp.model.Signature;
import com.example.shangrilapp.repository.PetitionRepository;
import com.example.shangrilapp.repository.PetitionerRepository;
import com.example.shangrilapp.repository.SignatureRepository;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
//@RequiredArgsConstructor

public class PetitionService {
    private final PetitionRepository petitionRepository;
    private final SignatureRepository signatureRepository;
    private final PetitionerRepository petitionerRepository;

    public PetitionService(PetitionRepository petitionRepository, SignatureRepository signatureRepository,
                           PetitionerRepository petitionerRepository) {
        this.petitionRepository = petitionRepository;
        this.signatureRepository = signatureRepository;
        this.petitionerRepository = petitionerRepository;
    }

    @Transactional
    public Petition createPetition(PetitionDTO petitionDTO, Authentication authentication) {
        Petitioner petitioner = petitionerRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Petition petition = new Petition();
        petition.setTitle(petitionDTO.getTitle());
        petition.setContent(petitionDTO.getContent());
        petition.setPetitioner(petitioner);
        petition.setStatus("open");
        petition.setCreatedAt(LocalDateTime.now());
        petition.setSignatureCount(0);

        return petitionRepository.save(petition);
    }

//    @Transactional(readOnly = true)
    public List<Petition> getAllPetitions() {
        return petitionRepository.findAll();
    }

    public List<Petition> getUserPetitions(Authentication authentication) {
        return petitionRepository.findByPetitionerEmail(authentication.getName());
    }

    @Transactional(readOnly = true)
    public List<Petition> getPetitionsByStatus(String status) {
        return petitionRepository.findByStatus(status);
    }

    @Transactional
    public void signPetition(Long petitionId) {
        Petitioner currentPetitioner = (Petitioner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Petition petition = petitionRepository.findById(petitionId)
                .orElseThrow(() -> new CustomException("Petition not found"));

        if (!"open".equals(petition.getStatus())) {
            throw new CustomException("This petition is closed");
        }

        if (signatureRepository.existsByPetition_IdAndPetitioner_Email(
                petitionId, currentPetitioner.getEmail())) {
            throw new CustomException("You have already signed this petition");
        }

        Signature signature = new Signature(petition, currentPetitioner);
        signatureRepository.save(signature);

        petition.setSignatureCount(petition.getSignatureCount() + 1);
        petitionRepository.save(petition);
    }

    @Transactional(readOnly = true)
    public Petition getPetition(Long petitionId) {
        return petitionRepository.findByIdWithSignatures(petitionId)
                .orElseThrow(() -> new CustomException("Petition not found"));
    }

    public Map<String, Object> getPetitionStatistics(Authentication authentication) {
        String email = authentication.getName();
        long totalPetitions = petitionRepository.countByPetitionerEmail(email);
        long activePetitions = petitionRepository.countByPetitionerEmailAndStatus(email, "open");

        return Map.of(
                "totalPetitions", totalPetitions,
                "activePetitions", activePetitions
        );
    }
}
