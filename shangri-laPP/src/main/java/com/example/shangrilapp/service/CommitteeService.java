package com.example.shangrilapp.service;

import com.example.shangrilapp.exception.CustomException;
import com.example.shangrilapp.model.Petition;
import com.example.shangrilapp.repository.PetitionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Correct import

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommitteeService {
    private final PetitionRepository petitionRepository;

    @Value("${petition.signature.threshold:1000}")
    private int signatureThreshold;

    public CommitteeService(PetitionRepository petitionRepository) {
        this.petitionRepository = petitionRepository;
    }

    @Transactional
    public void setSignatureThreshold(int threshold) {
        if (threshold < 1) {
            throw new CustomException("Threshold must be greater than 0");
        }
        this.signatureThreshold = threshold;
    }

    @Transactional
    public void respondToPetition(Long petitionId, String response) {
        Petition petition = petitionRepository.findById(petitionId)
                .orElseThrow(() -> new CustomException("Petition not found"));

        if (petition.getSignatureCount() < signatureThreshold) {
            throw new CustomException("Petition has not reached the required signature threshold");
        }

        if (!"open".equals(petition.getStatus())) {
            throw new CustomException("Petition is already closed");
        }

        petition.setResponse(response);
        petition.setStatus("closed");
        petitionRepository.save(petition);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getPetitionStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        statistics.put("totalPetitions", petitionRepository.count());
        statistics.put("openPetitions", petitionRepository.countByStatus("open"));
        statistics.put("closedPetitions", petitionRepository.countByStatus("closed"));
        statistics.put("thresholdReached",
                petitionRepository.findPetitionsReachingThreshold(signatureThreshold).size());
        statistics.put("currentThreshold", signatureThreshold);

        return statistics;
    }

    @Transactional(readOnly = true)
    public List<Petition> getPendingPetitions() {
        return petitionRepository.findPetitionsReachingThreshold(signatureThreshold);
    }

    public int getCurrentThreshold() {
        return signatureThreshold;
    }
}