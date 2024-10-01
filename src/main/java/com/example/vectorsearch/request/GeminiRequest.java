package com.example.vectorsearch.request;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
public record GeminiRequest(

        String model, Content content) {

    public static record Content(
            List<Part> parts) {}

    public static record Part(
            String text) {}
}
