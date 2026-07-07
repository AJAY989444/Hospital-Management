package com.hospital.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SuggestionRequest {
    @NotBlank(message = "Symptoms text is required")
    @Size(min = 10, message = "Please describe symptoms in at least 10 characters")
    private String symptomsText;

    public SuggestionRequest() {}
    public SuggestionRequest(String symptomsText) { this.symptomsText = symptomsText; }

    public String getSymptomsText() { return symptomsText; }
    public void setSymptomsText(String symptomsText) { this.symptomsText = symptomsText; }
}
