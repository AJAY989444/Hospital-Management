package com.hospital.management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_suggestions")
public class AiSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "symptoms_text", columnDefinition = "TEXT", nullable = false)
    private String symptomsText;

    @Column(name = "suggested_specialty", nullable = false)
    private String suggestedSpecialty;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_level", nullable = false)
    private UrgencyLevel urgencyLevel;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reasoning;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public AiSuggestion() {}

    public AiSuggestion(Long id, Patient patient, String symptomsText, String suggestedSpecialty,
                        UrgencyLevel urgencyLevel, String reasoning, LocalDateTime createdAt) {
        this.id = id;
        this.patient = patient;
        this.symptomsText = symptomsText;
        this.suggestedSpecialty = suggestedSpecialty;
        this.urgencyLevel = urgencyLevel;
        this.reasoning = reasoning;
        this.createdAt = createdAt;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public Patient getPatient() { return patient; }
    public String getSymptomsText() { return symptomsText; }
    public String getSuggestedSpecialty() { return suggestedSpecialty; }
    public UrgencyLevel getUrgencyLevel() { return urgencyLevel; }
    public String getReasoning() { return reasoning; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // --- Setters ---
    public void setId(Long id) { this.id = id; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public void setSymptomsText(String symptomsText) { this.symptomsText = symptomsText; }
    public void setSuggestedSpecialty(String suggestedSpecialty) { this.suggestedSpecialty = suggestedSpecialty; }
    public void setUrgencyLevel(UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // --- Builder ---
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Patient patient;
        private String symptomsText;
        private String suggestedSpecialty;
        private UrgencyLevel urgencyLevel;
        private String reasoning;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder patient(Patient patient) { this.patient = patient; return this; }
        public Builder symptomsText(String symptomsText) { this.symptomsText = symptomsText; return this; }
        public Builder suggestedSpecialty(String suggestedSpecialty) { this.suggestedSpecialty = suggestedSpecialty; return this; }
        public Builder urgencyLevel(UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; return this; }
        public Builder reasoning(String reasoning) { this.reasoning = reasoning; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public AiSuggestion build() {
            return new AiSuggestion(id, patient, symptomsText, suggestedSpecialty, urgencyLevel, reasoning, createdAt);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AiSuggestion)) return false;
        AiSuggestion that = (AiSuggestion) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
