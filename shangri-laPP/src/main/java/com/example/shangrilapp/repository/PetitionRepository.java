package com.example.shangrilapp.repository;

import com.example.shangrilapp.model.Petition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetitionRepository extends JpaRepository<Petition, Long> {
    // Find all petitions by status
    List<Petition> findByStatus(String status);

    // Find petitions by creator's email
    List<Petition> findByPetitioner_Email(String email);

    // Find petitions that have reached the signature threshold
    @Query("SELECT p FROM Petition p WHERE p.signatureCount >= :threshold AND p.status = 'open'")
    List<Petition> findPetitionsReachingThreshold(@Param("threshold") int threshold);

    // Find trending petitions (most signatures in last 7 days)
    @Query(value = "SELECT p.* FROM petitions p " +
            "JOIN signatures s ON p.id = s.petition_id " +
            "WHERE s.signed_at >= DATE_SUB(NOW(), INTERVAL 7 DAY) " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(s.id) DESC LIMIT 10",
            nativeQuery = true)
    List<Petition> findTrendingPetitions();

    // Count petitions by status
    Long countByStatus(String status);

    // Find petition with signatures count
    @Query("SELECT p FROM Petition p LEFT JOIN FETCH p.signatures WHERE p.id = :id")
    Optional<Petition> findByIdWithSignatures(@Param("id") Long id);

    List<Petition> findByPetitionerEmail(String email);
    long countByPetitionerEmail(String email);
    long countByPetitionerEmailAndStatus(String email, String status);
    @Query("SELECT COALESCE(SUM(p.signatureCount), 0) FROM Petition p WHERE p.petitioner.email = :email")
    long countTotalSignaturesByPetitionerEmail(@Param("email") String email);
}

