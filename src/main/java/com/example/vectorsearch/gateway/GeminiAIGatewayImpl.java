package com.example.vectorsearch.gateway;

import com.example.vectorsearch.config.GeminiConfig;
import com.example.vectorsearch.request.GeminiRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

@ApplicationScoped
public class GeminiAIGatewayImpl implements GeminiAIGateway {

    @Inject
    GeminiConfig geminiAIConfig;

    private static final Logger LOGGER = LoggerFactory.getLogger(GeminiAIGatewayImpl.class);

    @Override
    public List<Double> getEmbedding(String input) {
        ObjectMapper objectMapper = new ObjectMapper();

        var geminiAIGateway = QuarkusRestClientBuilder.newBuilder()
                .baseUri(URI.create(geminiAIConfig.getGeminiAIUrl() + "?key=" + geminiAIConfig.getApiKey()))
                .build(GeminiClient.class);

        LOGGER.info("Requesting embeddings for input: {}", input);

        GeminiRequest request = new GeminiRequest(
                geminiAIConfig.getModel(),
                new GeminiRequest.Content(List.of(new GeminiRequest.Part(input)))
        );
        try {
            String jsonRequest = objectMapper.writeValueAsString(request);
            var embedding = geminiAIGateway.embedding(jsonRequest);

            return embedding.getEmbedding().getValues();
        } catch (WebApplicationException e) {
            LOGGER.error("Error from Gemini AI: {}", e.getResponse().readEntity(String.class));
            throw e;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
