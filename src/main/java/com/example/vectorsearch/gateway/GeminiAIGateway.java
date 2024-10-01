package com.example.vectorsearch.gateway;

import java.util.List;

public interface GeminiAIGateway {
    List<Double> getEmbedding(String input);
}
