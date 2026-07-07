package com.hospital.management.dto;

public class SuggestionResponse {
    private String suggestedSpecialty;
    private String urgencyLevel;
    private String reasoning;

    public SuggestionResponse() {}
    public SuggestionResponse(String suggestedSpecialty, String urgencyLevel, String reasoning) {
        this.suggestedSpecialty = suggestedSpecialty; this.urgencyLevel = urgencyLevel; this.reasoning = reasoning;
    }

    public String getSuggestedSpecialty() { return suggestedSpecialty; }
    public String getUrgencyLevel() { return urgencyLevel; }
    public String getReasoning() { return reasoning; }

    public void setSuggestedSpecialty(String suggestedSpecialty) { this.suggestedSpecialty = suggestedSpecialty; }
    public void setUrgencyLevel(String urgencyLevel) { this.urgencyLevel = urgencyLevel; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String suggestedSpecialty; private String urgencyLevel; private String reasoning;
        public Builder suggestedSpecialty(String s) { this.suggestedSpecialty = s; return this; }
        public Builder urgencyLevel(String u) { this.urgencyLevel = u; return this; }
        public Builder reasoning(String r) { this.reasoning = r; return this; }
        public SuggestionResponse build() { return new SuggestionResponse(suggestedSpecialty, urgencyLevel, reasoning); }
    }
}
