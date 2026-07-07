package com.hospital.management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "doctors")
@SQLDelete(sql = "UPDATE doctors SET is_active = false WHERE id = ?")
@SQLRestriction("is_active = true")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String specialty;

    @Column(nullable = false)
    private String qualification;

    @Column(name = "experience_years", nullable = false)
    private int experienceYears;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    public Doctor() {}

    public Doctor(Long id, User user, String specialty, String qualification, int experienceYears, boolean isActive) {
        this.id = id;
        this.user = user;
        this.specialty = specialty;
        this.qualification = qualification;
        this.experienceYears = experienceYears;
        this.isActive = isActive;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getSpecialty() { return specialty; }
    public String getQualification() { return qualification; }
    public int getExperienceYears() { return experienceYears; }
    public boolean isActive() { return isActive; }

    // --- Setters ---
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    public void setActive(boolean active) { isActive = active; }

    // --- Builder ---
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private String specialty;
        private String qualification;
        private int experienceYears;
        private boolean isActive = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder specialty(String specialty) { this.specialty = specialty; return this; }
        public Builder qualification(String qualification) { this.qualification = qualification; return this; }
        public Builder experienceYears(int experienceYears) { this.experienceYears = experienceYears; return this; }
        public Builder isActive(boolean isActive) { this.isActive = isActive; return this; }

        public Doctor build() {
            return new Doctor(id, user, specialty, qualification, experienceYears, isActive);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        Doctor doctor = (Doctor) o;
        return id != null && id.equals(doctor.getId());
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
