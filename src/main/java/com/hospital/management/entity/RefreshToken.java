package com.hospital.management.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    public RefreshToken() {}

    public RefreshToken(Long id, String token, User user, Instant expiryDate) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getToken() { return token; }
    public User getUser() { return user; }
    public Instant getExpiryDate() { return expiryDate; }

    // --- Setters ---
    public void setId(Long id) { this.id = id; }
    public void setToken(String token) { this.token = token; }
    public void setUser(User user) { this.user = user; }
    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }

    // --- Builder ---
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String token;
        private User user;
        private Instant expiryDate;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder token(String token) { this.token = token; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder expiryDate(Instant expiryDate) { this.expiryDate = expiryDate; return this; }

        public RefreshToken build() {
            return new RefreshToken(id, token, user, expiryDate);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshToken)) return false;
        RefreshToken that = (RefreshToken) o;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
