package com.example.shangrilapp.service;

import com.example.shangrilapp.dto.PetitionerDTO;
import com.example.shangrilapp.exception.CustomException;
import com.example.shangrilapp.model.Petition;
import com.example.shangrilapp.model.Petitioner;
import com.example.shangrilapp.repository.PetitionRepository;
import com.example.shangrilapp.repository.PetitionerRepository;
import com.example.shangrilapp.util.BioIdValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PetitionerService {
    private final PetitionerRepository petitionerRepository;
    private final PasswordEncoder passwordEncoder;
    private final BioIdValidator bioIdValidator;
    private final PetitionRepository petitionRepository;



    public PetitionerService(PetitionerRepository petitionerRepository, PasswordEncoder passwordEncoder, BioIdValidator bioIdValidator, PetitionRepository petitionRepository) {
        this.petitionerRepository = petitionerRepository;
        this.passwordEncoder = passwordEncoder;
        this.bioIdValidator = bioIdValidator;
        this.petitionRepository = petitionRepository;
    }

    @Transactional
    public void register(PetitionerDTO dto) {
        // Validate email uniqueness
        if (petitionerRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException("Email already registered");
        }

        // Validate BioID uniqueness
        if (petitionerRepository.existsByBioId(dto.getBioId())) {
            throw new CustomException("BioID already in use");
        }

        // Validate BioID format and existence
        if (!bioIdValidator.isValid(dto.getBioId())) {
            throw new CustomException("Invalid BioID");
        }

        // Create new petitioner
        Petitioner petitioner = new Petitioner();
        petitioner.setEmail(dto.getEmail());
        petitioner.setFullName(dto.getFullName());
        petitioner.setDateOfBirth(dto.getDateOfBirth());
        petitioner.setPassword(passwordEncoder.encode(dto.getPassword()));
        petitioner.setBioId(dto.getBioId());

        petitionerRepository.save(petitioner);
    }

    @Transactional(readOnly = true)
    public Petitioner findByEmail(String email) {
        return petitionerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("Petitioner not found"));
    }

    public Map<String, Object> getStatistics(String email) {
        Map<String, Object> statistics = new HashMap<>();

        // Get counts of petitions
        long totalPetitions = petitionRepository.countByPetitionerEmail(email);
        long activePetitions = petitionRepository.countByPetitionerEmailAndStatus(email, "open");
        long totalSignatures = petitionRepository.countTotalSignaturesByPetitionerEmail(email);

        statistics.put("totalPetitions", totalPetitions);
        statistics.put("activePetitions", activePetitions);
        statistics.put("totalSignatures", totalSignatures);

        return statistics;
    }

    public List<Petition> getPetitions(String email) {
        return petitionRepository.findByPetitionerEmail(email);
    }
}