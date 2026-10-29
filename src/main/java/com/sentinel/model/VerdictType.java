package com.sentinel.model;

public enum VerdictType {
    VERIFIED("Verified", "Cryptographically signed and untampered provenance chain"),
    MODIFIED("Modified", "Evidence of content tampering or provenance signature mismatch"),
    UNKNOWN("Unknown", "Missing provenance or stripped metadata — absence of evidence is not evidence of fabrication");

    private final String label;
    private final String description;

    VerdictType(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}

