package com.example.vectorsearch.config;

import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class GeminiConfig {

    @ConfigProperty(name = "gemini.base.url")
    String geminiAIUrl;

//    @ConfigProperty(name = "gemini.bearer.token")
//    String token;

    @ConfigProperty(name = "gemini.bearer.model")
    String model;

    @ConfigProperty(name = "gemini.api.key")
    String apiKey;

    public String getGeminiAIUrl() {
        return geminiAIUrl;
    }


    public String getModel() {
        return model;
    }

    public String getApiKey() {
        return apiKey;
    }
}
