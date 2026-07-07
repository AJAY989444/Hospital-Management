package com.hospital.management.dto;

import com.hospital.management.entity.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppointmentDTO {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private String specialty;
    private LocalDate appointmentDate;
    private String timeSlot;
    private AppointmentStatus status;
    private String notes;
    private String aiSummary;
    private LocalDateTime createdAt;

    public AppointmentDTO() {}

    public AppointmentDTO(Long id, Long patientId, String patientName, Long doctorId, String doctorName,
                          String specialty, LocalDate appointmentDate, String timeSlot,
                          AppointmentStatus status, String notes, String aiSummary, LocalDateTime createdAt) {
        this.id = id; this.patientId = patientId; this.patientName = patientName;
        this.doctorId = doctorId; this.doctorName = doctorName; this.specialty = specialty;
        this.appointmentDate = appointmentDate; this.timeSlot = timeSlot; this.status = status;
        this.notes = notes; this.aiSummary = aiSummary; this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public Long getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getSpecialty() { return specialty; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public String getTimeSlot() { return timeSlot; }
    public AppointmentStatus getStatus() { return status; }
    public String getNotes() { return notes; }
    public String getAiSummary() { return aiSummary; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private Long patientId; private String patientName;
        private Long doctorId; private String doctorName; private String specialty;
        private LocalDate appointmentDate; private String timeSlot;
        private AppointmentStatus status; private String notes; private String aiSummary; private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder patientId(Long patientId) { this.patientId = patientId; return this; }
        public Builder patientName(String patientName) { this.patientName = patientName; return this; }
        public Builder doctorId(Long doctorId) { this.doctorId = doctorId; return this; }
        public Builder doctorName(String doctorName) { this.doctorName = doctorName; return this; }
        public Builder specialty(String specialty) { this.specialty = specialty; return this; }
        public Builder appointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; return this; }
        public Builder timeSlot(String timeSlot) { this.timeSlot = timeSlot; return this; }
        public Builder status(AppointmentStatus status) { this.status = status; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder aiSummary(String aiSummary) { this.aiSummary = aiSummary; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AppointmentDTO build() {
            return new AppointmentDTO(id, patientId, patientName, doctorId, doctorName, specialty,
                    appointmentDate, timeSlot, status, notes, aiSummary, createdAt);
        }
    }
}
