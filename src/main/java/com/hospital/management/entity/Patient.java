package com.hospital.management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "patients")
@SQLDelete(sql = "UPDATE patients SET is_active = false WHERE id = ?")
@SQLRestriction("is_active = true")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String gender;

    @Column(name = "blood_group", nullable = false)
    private String bloodGroup;

    @Column(nullable = false)
    private String phone;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    public Patient() {}

    public Patient(Long id, User user, int age, String gender, String bloodGroup, String phone, boolean isActive) {
        this.id = id;
        this.user = user;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.isActive = isActive;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public User getUser() { return user; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getBloodGroup() { return bloodGroup; }
    public String getPhone() { return phone; }
    public boolean isActive() { return isActive; }

    // --- Setters ---
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setActive(boolean active) { isActive = active; }

    // --- Builder ---
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User user;
        private int age;
        private String gender;
        private String bloodGroup;
        private String phone;
        private boolean isActive = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder age(int age) { this.age = age; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder bloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder isActive(boolean isActive) { this.isActive = isActive; return this; }

        public Patient build() {
            return new Patient(id, user, age, gender, bloodGroup, phone, isActive);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return id != null && id.equals(patient.getId());
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
