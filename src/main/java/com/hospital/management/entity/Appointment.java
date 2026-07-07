package com.hospital.management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "time_slot", nullable = false)
    private String timeSlot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "ai_summary", columnDefinition = "TEXT")
    private String aiSummary;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Appointment() {}

    public Appointment(Long id, Patient patient, Doctor doctor, LocalDate appointmentDate,
                       String timeSlot, AppointmentStatus status, String notes, String aiSummary,
                       LocalDateTime createdAt) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.timeSlot = timeSlot;
        this.status = status != null ? status : AppointmentStatus.PENDING;
        this.notes = notes;
        this.aiSummary = aiSummary;
        this.createdAt = createdAt;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public String getTimeSlot() { return timeSlot; }
    public AppointmentStatus getStatus() { return status; }
    public String getNotes() { return notes; }
    public String getAiSummary() { return aiSummary; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // --- Setters ---
    public void setId(Long id) { this.id = id; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // --- Builder ---
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Patient patient;
        private Doctor doctor;
        private LocalDate appointmentDate;
        private String timeSlot;
        private AppointmentStatus status = AppointmentStatus.PENDING;
        private String notes;
        private String aiSummary;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder patient(Patient patient) { this.patient = patient; return this; }
        public Builder doctor(Doctor doctor) { this.doctor = doctor; return this; }
        public Builder appointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; return this; }
        public Builder timeSlot(String timeSlot) { this.timeSlot = timeSlot; return this; }
        public Builder status(AppointmentStatus status) { this.status = status; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder aiSummary(String aiSummary) { this.aiSummary = aiSummary; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Appointment build() {
            return new Appointment(id, patient, doctor, appointmentDate, timeSlot, status, notes, aiSummary, createdAt);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        Appointment that = (Appointment) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
