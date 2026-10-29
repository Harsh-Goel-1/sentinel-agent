package com.sentinel.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvestigationReport {
    private String id;
    private String sampleId;
    private String filename;
    private String mediaType;
    private String fileSize;
    private String sha256;
    private VerdictType verdict;
    private double confidenceScore; // e.g. 97.3
    private double uncertaintyScore; // e.g. 0.027
    private String executiveSummary;
    private String narrative;
    private List<EvidenceStep> steps = new ArrayList<>();
    private Map<String, Object> evidenceChain = new HashMap<>();
    private String agentBackend; // "OPENAI" or "LOCAL_AGENT"
    private String modelUsed;
    private long executionTimeMs;
    private Instant timestamp = Instant.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public VerdictType getVerdict() {
        return verdict;
    }

    public void setVerdict(VerdictType verdict) {
        this.verdict = verdict;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public double getUncertaintyScore() {
        return uncertaintyScore;
    }

    public void setUncertaintyScore(double uncertaintyScore) {
        this.uncertaintyScore = uncertaintyScore;
    }

    public String getExecutiveSummary() {
        return executiveSummary;
    }

    public void setExecutiveSummary(String executiveSummary) {
        this.executiveSummary = executiveSummary;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public List<EvidenceStep> getSteps() {
        return steps;
    }

    public void setSteps(List<EvidenceStep> steps) {
        this.steps = steps;
    }

    public Map<String, Object> getEvidenceChain() {
        return evidenceChain;
    }

    public void setEvidenceChain(Map<String, Object> evidenceChain) {
        this.evidenceChain = evidenceChain;
    }

    public String getAgentBackend() {
        return agentBackend;
    }

    public void setAgentBackend(String agentBackend) {
        this.agentBackend = agentBackend;
    }

    public String getModelUsed() {
        return modelUsed;
    }

    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}

