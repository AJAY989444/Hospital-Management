package com.hospital.management.dto;

import com.hospital.management.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "Role is required")
    private UserRole role;

    // Patient fields
    private Integer age;
    private String gender;
    private String bloodGroup;
    private String phone;

    // Doctor fields
    private String specialty;
    private String qualification;
    private Integer experienceYears;

    public RegisterRequest() {}

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }
    public Integer getAge() { return age; }
    public String getGender() { return gender; }
    public String getBloodGroup() { return bloodGroup; }
    public String getPhone() { return phone; }
    public String getSpecialty() { return specialty; }
    public String getQualification() { return qualification; }
    public Integer getExperienceYears() { return experienceYears; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(UserRole role) { this.role = role; }
    public void setAge(Integer age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
}
