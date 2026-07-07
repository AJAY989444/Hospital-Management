package com.hospital.management.dto;

import com.hospital.management.entity.UserRole;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private boolean isActive;

    public UserDTO() {}

    public UserDTO(Long id, String name, String email, UserRole role, boolean isActive) {
        this.id = id; this.name = name; this.email = email; this.role = role; this.isActive = isActive;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public UserRole getRole() { return role; }
    public boolean isActive() { return isActive; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(UserRole role) { this.role = role; }
    public void setActive(boolean active) { isActive = active; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String name; private String email;
        private UserRole role; private boolean isActive;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder role(UserRole role) { this.role = role; return this; }
        public Builder isActive(boolean isActive) { this.isActive = isActive; return this; }
        public UserDTO build() { return new UserDTO(id, name, email, role, isActive); }
    }
}
