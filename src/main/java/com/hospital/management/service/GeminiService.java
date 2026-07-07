package com.hospital.management.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.management.dto.SuggestionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class GeminiService {

    private final String apiKey;
    private final String apiUrl;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public GeminiService(
            @Value("${gemini.api.key:}") String apiKey,
            @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}") String apiUrl,
            ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public SuggestionResponse getSpecialtyRecommendation(String symptomsText) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return SuggestionResponse.builder()
                    .suggestedSpecialty("General Physician")
                    .urgencyLevel("MEDIUM")
                    .reasoning("Gemini API key is not configured. Falling back to General Physician for safety.")
                    .build();
        }

        try {
            // Build the prompt requesting JSON output
            String prompt = "Analyze the following patient symptoms. Suggest the single best doctor specialty (from: Cardiologist, Dermatologist, Neurologist, Orthopedic, Pediatrician, General Physician), assess urgency level (HIGH, MEDIUM, LOW), and explain why in 2 sentences. Patient symptoms: " + symptomsText;

            // Request body JSON matching Gemini REST specs with schema validation
            String requestBody = "{\n" +
                    "  \"contents\": [{\n" +
                    "    \"parts\": [{\n" +
                    "      \"text\": " + objectMapper.writeValueAsString(prompt) + "\n" +
                    "    }]\n" +
                    "  }],\n" +
                    "  \"generationConfig\": {\n" +
                    "    \"responseMimeType\": \"application/json\",\n" +
                    "    \"responseSchema\": {\n" +
                    "      \"type\": \"object\",\n" +
                    "      \"properties\": {\n" +
                    "        \"suggestedSpecialty\": {\n" +
                    "          \"type\": \"string\",\n" +
                    "          \"enum\": [\"Cardiologist\", \"Dermatologist\", \"Neurologist\", \"Orthopedic\", \"Pediatrician\", \"General Physician\"]\n" +
                    "        },\n" +
                    "        \"urgencyLevel\": {\n" +
                    "          \"type\": \"string\",\n" +
                    "          \"enum\": [\"HIGH\", \"MEDIUM\", \"LOW\"]\n" +
                    "        },\n" +
                    "        \"reasoning\": {\n" +
                    "          \"type\": \"string\"\n" +
                    "        }\n" +
                    "      },\n" +
                    "      \"required\": [\"suggestedSpecialty\", \"urgencyLevel\", \"reasoning\"]\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";

            String requestUrl = apiUrl + "?key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .timeout(Duration.ofSeconds(15))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                String innerJsonText = root.path("candidates")
                        .path(0)
                        .path("content")
                        .path("parts")
                        .path(0)
                        .path("text")
                        .asText();
                
                return objectMapper.readValue(innerJsonText, SuggestionResponse.class);
            } else {
                throw new RuntimeException("Gemini API call failed with status: " + response.statusCode() + ", body: " + response.body());
            }
        } catch (Exception e) {
            // Log warning and return fallback
            System.err.println("Gemini recommendation error: " + e.getMessage());
            return SuggestionResponse.builder()
                    .suggestedSpecialty("General Physician")
                    .urgencyLevel("MEDIUM")
                    .reasoning("Clinical analysis failed to contact AI. Please consult General Physician as standard practice.")
                    .build();
        }
    }

    public String generateAppointmentSummary(String doctorName, String specialty, String date, String notes) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return "Appointment completed with " + doctorName + " on " + date + ". Prescribed notes: " + notes;
        }

        try {
            String prompt = String.format(
                    "Summarize the following clinical consultation notes into a plain English, easy-to-understand paragraph for the patient. Keep it empathetic and brief (3 sentences max). Doctor Name: %s, Specialty: %s, Date: %s, Notes: %s",
                    doctorName, specialty, date, notes
            );

            String requestBody = "{\n" +
                    "  \"contents\": [{\n" +
                    "    \"parts\": [{\n" +
                    "      \"text\": " + objectMapper.writeValueAsString(prompt) + "\n" +
                    "    }]\n" +
                    "  }]\n" +
                    "}";

            String requestUrl = apiUrl + "?key=" + apiKey;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .timeout(Duration.ofSeconds(15))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                return root.path("candidates")
                        .path(0)
                        .path("content")
                        .path("parts")
                        .path(0)
                        .path("text")
                        .asText()
                        .trim();
            } else {
                throw new RuntimeException("Gemini API call failed with status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Gemini summary error: " + e.getMessage());
            return "Summary unavailable. Consult notes: " + notes;
        }
    }
}
