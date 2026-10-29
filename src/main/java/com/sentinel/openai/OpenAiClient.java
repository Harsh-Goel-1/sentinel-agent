package com.sentinel.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sentinel.config.OpenAiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiClient {
    private static final Logger log = LoggerFactory.getLogger(OpenAiClient.class);

    private final OpenAiConfig config;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public OpenAiClient(OpenAiConfig config, ObjectMapper objectMapper) {
        this.config = config;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    public record OpenAiMessage(String role, String content) {}

    public record OpenAiResponse(
            boolean success,
            String content,
            String model,
            int promptTokens,
            int completionTokens,
            String errorMessage
    ) {}

    public boolean isConfigured(String apiKeyOverride) {
        if (apiKeyOverride != null && !apiKeyOverride.isBlank()) {
            return true;
        }
        return config.hasApiKey();
    }

    public OpenAiResponse chatCompletion(List<OpenAiMessage> messages, String apiKeyOverride) {
        String effectiveKey = (apiKeyOverride != null && !apiKeyOverride.isBlank())
                ? apiKeyOverride.trim()
                : config.getApiKey();

        if (effectiveKey == null || effectiveKey.isBlank()) {
            return new OpenAiResponse(false, null, null, 0, 0, "No OpenAI API key provided");
        }

        try {
            ObjectNode root = objectMapper.createObjectNode();
            root.put("model", config.getModel());
            root.put("temperature", config.getTemperature());
            root.put("max_tokens", config.getMaxTokens());

            ArrayNode messagesArray = root.putArray("messages");
            for (OpenAiMessage msg : messages) {
                ObjectNode m = messagesArray.addObject();
                m.put("role", msg.role());
                m.put("content", msg.content());
            }

            String requestBody = objectMapper.writeValueAsString(root);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getBaseUrl() + "/chat/completions"))
                    .timeout(Duration.ofSeconds(60))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + effectiveKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JsonNode resJson = objectMapper.readTree(response.body());
                String content = resJson.path("choices").path(0).path("message").path("content").asText();
                String model = resJson.path("model").asText(config.getModel());
                int promptTokens = resJson.path("usage").path("prompt_tokens").asInt(0);
                int compTokens = resJson.path("usage").path("completion_tokens").asInt(0);

                return new OpenAiResponse(true, content, model, promptTokens, compTokens, null);
            } else {
                log.warn("OpenAI API error {}: {}", response.statusCode(), response.body());
                return new OpenAiResponse(false, null, null, 0, 0,
                        "OpenAI HTTP " + response.statusCode() + ": " + response.body());
            }
        } catch (Exception e) {
            log.error("Failed to call OpenAI API", e);
            return new OpenAiResponse(false, null, null, 0, 0, "OpenAI connection failed: " + e.getMessage());
        }
    }
}

