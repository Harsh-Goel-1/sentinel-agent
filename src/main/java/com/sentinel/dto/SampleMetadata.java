package com.sentinel.dto;

public class SampleMetadata {
    private String id;
    private String title;
    private String filename;
    private String previewImage;
    private String expectedVerdict;
    private String description;

    public SampleMetadata() {
    }

    public SampleMetadata(String id, String title, String filename, String previewImage, String expectedVerdict, String description) {
        this.id = id;
        this.title = title;
        this.filename = filename;
        this.previewImage = previewImage;
        this.expectedVerdict = expectedVerdict;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }

    public String getExpectedVerdict() {
        return expectedVerdict;
    }

    public void setExpectedVerdict(String expectedVerdict) {
        this.expectedVerdict = expectedVerdict;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

