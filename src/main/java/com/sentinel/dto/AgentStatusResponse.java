package com.sentinel.dto;

public class AgentStatusResponse {
    private boolean openAiConfigured;
    private String activeBackend; // "OPENAI" or "LOCAL_AGENT"
    private String configuredModel;
    private String baseUrl;
    private String statusMessage;

    public AgentStatusResponse() {
    }

    public AgentStatusResponse(boolean openAiConfigured, String activeBackend, String configuredModel, String baseUrl, String statusMessage) {
        this.openAiConfigured = openAiConfigured;
        this.activeBackend = activeBackend;
        this.configuredModel = configuredModel;
        this.baseUrl = baseUrl;
        this.statusMessage = statusMessage;
    }

    public boolean isOpenAiConfigured() {
        return openAiConfigured;
    }

    public void setOpenAiConfigured(boolean openAiConfigured) {
        this.openAiConfigured = openAiConfigured;
    }

    public String getActiveBackend() {
        return activeBackend;
    }

    public void setActiveBackend(String activeBackend) {
        this.activeBackend = activeBackend;
    }

    public String getConfiguredModel() {
        return configuredModel;
    }

    public void setConfiguredModel(String configuredModel) {
        this.configuredModel = configuredModel;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}

