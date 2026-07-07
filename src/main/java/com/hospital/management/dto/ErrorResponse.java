package com.hospital.management.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<String> details;

    public ErrorResponse() {}
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, List<String> details) {
        this.timestamp = timestamp; this.status = status; this.error = error;
        this.message = message; this.details = details;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public List<String> getDetails() { return details; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setStatus(int status) { this.status = status; }
    public void setError(String error) { this.error = error; }
    public void setMessage(String message) { this.message = message; }
    public void setDetails(List<String> details) { this.details = details; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private LocalDateTime timestamp; private int status; private String error;
        private String message; private List<String> details;
        public Builder timestamp(LocalDateTime t) { this.timestamp = t; return this; }
        public Builder status(int s) { this.status = s; return this; }
        public Builder error(String e) { this.error = e; return this; }
        public Builder message(String m) { this.message = m; return this; }
        public Builder details(List<String> d) { this.details = d; return this; }
        public ErrorResponse build() { return new ErrorResponse(timestamp, status, error, message, details); }
    }
}
