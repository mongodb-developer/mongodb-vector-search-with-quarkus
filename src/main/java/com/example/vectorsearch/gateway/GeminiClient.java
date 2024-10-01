package com.example.vectorsearch.gateway;

import com.example.vectorsearch.response.GeminiResponse;
import jakarta.ws.rs.POST;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface GeminiClient {

    @POST
    GeminiResponse embedding(String request);
}
