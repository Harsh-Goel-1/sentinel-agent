package com.sentinel.model;

import java.util.HashMap;
import java.util.Map;

public class MediaInspectionResult {
    private String filename;
    private String mimeType;
    private String format;
    private long fileSizeBytes;
    private String fileSizeFormatted;
    private int width;
    private int height;
    private String sha256;
    private Map<String, Object> containerAttributes = new HashMap<>();

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public String getFileSizeFormatted() {
        return fileSizeFormatted;
    }

    public void setFileSizeFormatted(String fileSizeFormatted) {
        this.fileSizeFormatted = fileSizeFormatted;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public Map<String, Object> getContainerAttributes() {
        return containerAttributes;
    }

    public void setContainerAttributes(Map<String, Object> containerAttributes) {
        this.containerAttributes = containerAttributes;
    }
}

