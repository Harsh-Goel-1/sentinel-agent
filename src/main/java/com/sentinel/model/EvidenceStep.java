package com.sentinel.model;

import java.util.HashMap;
import java.util.Map;

public class EvidenceStep {
    private int stepNumber;
    private String name;
    private String status; // "DONE", "WARN", "NEUTRAL", "ALERT"
    private String summary;
    private String detail;
    private Map<String, Object> data = new HashMap<>();

    public EvidenceStep() {
    }

    public EvidenceStep(int stepNumber, String name, String status, String summary, String detail) {
        this.stepNumber = stepNumber;
        this.name = name;
        this.status = status;
        this.summary = summary;
        this.detail = detail;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public EvidenceStep addData(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
        return this;
    }
}

