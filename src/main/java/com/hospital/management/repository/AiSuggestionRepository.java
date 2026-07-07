package com.hospital.management.repository;

import com.hospital.management.entity.AiSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiSuggestionRepository extends JpaRepository<AiSuggestion, Long> {
    List<AiSuggestion> findAllByOrderByCreatedAtDesc();
    List<AiSuggestion> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
