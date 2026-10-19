// src/main/java/com/example/shangrilapp/repository/PetitionerRepository.java
package com.example.shangrilapp.repository;

import com.example.shangrilapp.model.Petitioner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetitionerRepository extends JpaRepository<Petitioner, Long> {
    boolean existsByEmail(String email);
    boolean existsByBioId(String bioId);
    Optional<Petitioner> findByEmail(String email);

}