package com.example.shangrilapp.repository;

import com.example.shangrilapp.model.Signature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface SignatureRepository extends JpaRepository<Signature, Long> {

    boolean existsByPetition_IdAndPetitioner_Email(Long petitionId, String petitionerEmail);


    Long countByPetition_Id(Long petitionId);


    List<Signature> findByPetition_Id(Long petitionId);


    List<Signature> findByPetition_IdAndSignedAtAfter(Long petitionId, LocalDateTime date);


    @Query(value = "SELECT DATE(signed_at) as date, COUNT(*) as count " +
            "FROM signatures " +
            "WHERE petition_id = :petitionId " +
            "AND signed_at >= :startDate " +
            "GROUP BY DATE(signed_at) " +
            "ORDER BY date DESC",
            nativeQuery = true)
    List<Object[]> findSignatureTrends(@Param("petitionId") Long petitionId,
                                       @Param("startDate") LocalDateTime startDate);


    void deleteByPetition_Id(Long petitionId);


    List<Signature> findByPetitioner_Email(String email);
}
