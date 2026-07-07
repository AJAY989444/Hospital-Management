package com.hospital.management.controller;

import com.hospital.management.config.CustomUserDetails;
import com.hospital.management.dto.SuggestionRequest;
import com.hospital.management.dto.SuggestionResponse;
import com.hospital.management.entity.AiSuggestion;
import com.hospital.management.entity.Appointment;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.UrgencyLevel;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.AiSuggestionRepository;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.service.AppointmentService;
import com.hospital.management.service.GeminiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI Operations (Gemini)", description = "Endpoints powered by Google Gemini AI for symptoms assessment and post-consult summaries")
public class AiController {

    private final GeminiService geminiService;
    private final PatientRepository patientRepository;
    private final AiSuggestionRepository aiSuggestionRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentService appointmentService;

    public AiController(
            GeminiService geminiService,
            PatientRepository patientRepository,
            AiSuggestionRepository aiSuggestionRepository,
            AppointmentRepository appointmentRepository,
            AppointmentService appointmentService) {
        this.geminiService = geminiService;
        this.patientRepository = patientRepository;
        this.aiSuggestionRepository = aiSuggestionRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/suggest-doctor")
    @Operation(summary = "Get doctor specialty suggestion", description = "Analyzes text symptoms, suggests doctor specialty and urgency level, and logs query in audit logs.")
    @ApiResponse(responseCode = "200", description = "Suggestion successfully generated")
    @ApiResponse(responseCode = "404", description = "Patient profile not found")
    public ResponseEntity<SuggestionResponse> suggestDoctor(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody SuggestionRequest request) {
        
        Long userId = userDetails.getUser().getId();
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found for user ID: " + userId));

        // Call Gemini Service
        SuggestionResponse suggestion = geminiService.getSpecialtyRecommendation(request.getSymptomsText());

        // Parse UrgencyLevel
        UrgencyLevel urgency;
        try {
            urgency = UrgencyLevel.valueOf(suggestion.getUrgencyLevel().toUpperCase());
        } catch (Exception e) {
            urgency = UrgencyLevel.MEDIUM;
        }

        // Save AI Suggestion log to Database (Audit Trail)
        AiSuggestion log = AiSuggestion.builder()
                .patient(patient)
                .symptomsText(request.getSymptomsText())
                .suggestedSpecialty(suggestion.getSuggestedSpecialty())
                .urgencyLevel(urgency)
                .reasoning(suggestion.getReasoning())
                .build();
        aiSuggestionRepository.save(log);

        return ResponseEntity.ok(suggestion);
    }

    @PostMapping("/summarize-appointment/{appointmentId}")
    @Operation(summary = "Generate consultation summary", description = "Invoked by doctor completions. Formulates notes into an easy-to-understand paragraph for the patient dashboard.")
    @ApiResponse(responseCode = "200", description = "Summary generated and saved successfully")
    @ApiResponse(responseCode = "404", description = "Appointment not found")
    public ResponseEntity<Map<String, String>> summarizeAppointment(@PathVariable Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentId));

        if (appointment.getNotes() == null || appointment.getNotes().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cannot summarize appointment without clinical notes"));
        }

        String doctorName = appointment.getDoctor().getUser().getName();
        String specialty = appointment.getDoctor().getSpecialty();
        String date = appointment.getAppointmentDate().toString();
        String notes = appointment.getNotes();

        // Call Gemini Service for summary compilation
        String summary = geminiService.generateAppointmentSummary(doctorName, specialty, date, notes);

        // Save summary to database
        appointmentService.updateAiSummary(appointmentId, summary);

        return ResponseEntity.ok(Map.of("summary", summary));
    }
}
