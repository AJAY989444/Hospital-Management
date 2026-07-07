package com.hospital.management.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DoctorDTO {
    private Long id;
    private UserDTO user;
    private String specialty;
    private String qualification;
    private int experienceYears;
    private boolean isActive;

    public DoctorDTO() {}

    public DoctorDTO(Long id, UserDTO user, String specialty, String qualification, int experienceYears, boolean isActive) {
        this.id = id; this.user = user; this.specialty = specialty;
        this.qualification = qualification; this.experienceYears = experienceYears; this.isActive = isActive;
    }

    public Long getId() { return id; }
    public UserDTO getUser() { return user; }
    public String getSpecialty() { return specialty; }
    public String getQualification() { return qualification; }
    public int getExperienceYears() { return experienceYears; }
    @JsonProperty("isActive")
    public boolean isActive() { return isActive; }
    public boolean getIsActive() { return isActive; }

    public void setId(Long id) { this.id = id; }
    public void setUser(UserDTO user) { this.user = user; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    public void setActive(boolean active) { isActive = active; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private UserDTO user; private String specialty;
        private String qualification; private int experienceYears; private boolean isActive;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(UserDTO user) { this.user = user; return this; }
        public Builder specialty(String specialty) { this.specialty = specialty; return this; }
        public Builder qualification(String qualification) { this.qualification = qualification; return this; }
        public Builder experienceYears(int experienceYears) { this.experienceYears = experienceYears; return this; }
        public Builder isActive(boolean isActive) { this.isActive = isActive; return this; }
        public DoctorDTO build() { return new DoctorDTO(id, user, specialty, qualification, experienceYears, isActive); }
    }
}
