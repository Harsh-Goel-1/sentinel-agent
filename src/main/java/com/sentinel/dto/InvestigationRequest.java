package com.sentinel.dto;

public class InvestigationRequest {
    private String sampleId;
    private String customFilename;
    private String fileType;
    private String notes;

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getCustomFilename() {
        return customFilename;
    }

    public void setCustomFilename(String customFilename) {
        this.customFilename = customFilename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

