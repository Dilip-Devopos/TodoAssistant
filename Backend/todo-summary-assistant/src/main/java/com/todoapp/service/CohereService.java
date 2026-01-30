package com.todoapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class CohereService {

    @Value("${cohere.api.key}")
    private String cohereApiKey;

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public CohereService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Summarizes the given text using Cohere API.
     * Safely handles trial key errors and returns a fallback message if API fails.
     */
    public String summarizeText(String text) {
        if (text == null || text.isEmpty()) {
            return "No text provided to summarize.";
        }

        String url = "https://api.cohere.ai/v1/summarize"; // Cohere Summarization endpoint

        try {
            // Build JSON request
            ObjectNode requestBodyJson = objectMapper.createObjectNode();
            requestBodyJson.put("text", text);
            requestBodyJson.put("length", "long");      // Options: "short", "medium", "long"
            requestBodyJson.put("format", "paragraph"); // Options: "paragraph", "bullets"
            requestBodyJson.put("model", "summarize-xlarge"); // safer for trial keys

            RequestBody body = RequestBody.create(
                    requestBodyJson.toString(),
                    MediaType.parse("application/json; charset=utf-8")
            );

            // Build HTTP request
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + cohereApiKey)
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build();

            // Execute request
            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    // Log Cohere error for debugging
                    System.err.println("Cohere API error. Code: " + response.code() + " Body: " + responseBody);
                    return "Cohere API error: " + response.code() + " - Trial key may be limited.";
                }

                JsonNode jsonNode = objectMapper.readTree(responseBody);
                return jsonNode.has("summary") ? jsonNode.get("summary").asText() : 
                        "No summary found in Cohere response.";
            }

        } catch (IOException e) {
            System.err.println("Cohere API call failed: " + e.getMessage());
            return "Failed to summarize todos (Cohere trial key may have limits).";
        }
    }
}