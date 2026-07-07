package com.hospital.management.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PatientDTO {
    private Long id;
    private UserDTO user;
    private int age;
    private String gender;
    private String bloodGroup;
    private String phone;
    private boolean isActive;

    public PatientDTO() {}

    public PatientDTO(Long id, UserDTO user, int age, String gender, String bloodGroup, String phone, boolean isActive) {
        this.id = id; this.user = user; this.age = age; this.gender = gender;
        this.bloodGroup = bloodGroup; this.phone = phone; this.isActive = isActive;
    }

    public Long getId() { return id; }
    public UserDTO getUser() { return user; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getBloodGroup() { return bloodGroup; }
    public String getPhone() { return phone; }
    @JsonProperty("isActive")
    public boolean isActive() { return isActive; }
    public boolean getIsActive() { return isActive; }

    public void setId(Long id) { this.id = id; }
    public void setUser(UserDTO user) { this.user = user; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setActive(boolean active) { isActive = active; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private UserDTO user; private int age; private String gender;
        private String bloodGroup; private String phone; private boolean isActive;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(UserDTO user) { this.user = user; return this; }
        public Builder age(int age) { this.age = age; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder bloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder isActive(boolean isActive) { this.isActive = isActive; return this; }
        public PatientDTO build() { return new PatientDTO(id, user, age, gender, bloodGroup, phone, isActive); }
    }
}
