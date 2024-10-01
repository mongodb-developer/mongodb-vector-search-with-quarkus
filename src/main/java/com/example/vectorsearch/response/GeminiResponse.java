package com.example.vectorsearch.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GeminiResponse {

    @JsonProperty("embedding")
    private Embedding embedding;

    public Embedding getEmbedding() {
        return embedding;
    }


    public static class Embedding {
        @JsonProperty("values")
        private List<Double> values;

        public List<Double> getValues() {
            return values;
        }

    }
}
