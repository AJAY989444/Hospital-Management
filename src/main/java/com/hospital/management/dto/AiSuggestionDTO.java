package com.hospital.management.dto;

import com.hospital.management.entity.UrgencyLevel;
import java.time.LocalDateTime;

public class AiSuggestionDTO {
    private Long id;
    private String patientName;
    private String symptomsText;
    private String suggestedSpecialty;
    private UrgencyLevel urgencyLevel;
    private String reasoning;
    private LocalDateTime createdAt;

    public AiSuggestionDTO() {}

    public AiSuggestionDTO(Long id, String patientName, String symptomsText, String suggestedSpecialty,
                           UrgencyLevel urgencyLevel, String reasoning, LocalDateTime createdAt) {
        this.id = id; this.patientName = patientName; this.symptomsText = symptomsText;
        this.suggestedSpecialty = suggestedSpecialty; this.urgencyLevel = urgencyLevel;
        this.reasoning = reasoning; this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getPatientName() { return patientName; }
    public String getSymptomsText() { return symptomsText; }
    public String getSuggestedSpecialty() { return suggestedSpecialty; }
    public UrgencyLevel getUrgencyLevel() { return urgencyLevel; }
    public String getReasoning() { return reasoning; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setSymptomsText(String symptomsText) { this.symptomsText = symptomsText; }
    public void setSuggestedSpecialty(String suggestedSpecialty) { this.suggestedSpecialty = suggestedSpecialty; }
    public void setUrgencyLevel(UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String patientName; private String symptomsText;
        private String suggestedSpecialty; private UrgencyLevel urgencyLevel;
        private String reasoning; private LocalDateTime createdAt;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder patientName(String patientName) { this.patientName = patientName; return this; }
        public Builder symptomsText(String symptomsText) { this.symptomsText = symptomsText; return this; }
        public Builder suggestedSpecialty(String suggestedSpecialty) { this.suggestedSpecialty = suggestedSpecialty; return this; }
        public Builder urgencyLevel(UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; return this; }
        public Builder reasoning(String reasoning) { this.reasoning = reasoning; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AiSuggestionDTO build() {
            return new AiSuggestionDTO(id, patientName, symptomsText, suggestedSpecialty, urgencyLevel, reasoning, createdAt);
        }
    }
}
