package com.hospital.management.dto;

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String name;
    private String role;
    private Long userId;

    public AuthResponse() {}

    public AuthResponse(String accessToken, String refreshToken, String email, String name, String role, Long userId) {
        this.accessToken = accessToken; this.refreshToken = refreshToken; this.email = email;
        this.name = name; this.role = role; this.userId = userId;
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public Long getUserId() { return userId; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setUserId(Long userId) { this.userId = userId; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String accessToken; private String refreshToken; private String email;
        private String name; private String role; private Long userId;
        public Builder accessToken(String t) { this.accessToken = t; return this; }
        public Builder refreshToken(String t) { this.refreshToken = t; return this; }
        public Builder email(String e) { this.email = e; return this; }
        public Builder name(String n) { this.name = n; return this; }
        public Builder role(String r) { this.role = r; return this; }
        public Builder userId(Long id) { this.userId = id; return this; }
        public AuthResponse build() { return new AuthResponse(accessToken, refreshToken, email, name, role, userId); }
    }
}
