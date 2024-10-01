package com.example.vectorsearch.service;

import com.example.vectorsearch.gateway.GeminiAIGatewayImpl;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;


import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class ListingService {

    @Inject
    MongoClient mongoClient;


    @Inject
    GeminiAIGatewayImpl geminiClient;

    public void generateAndStoreEmbeddings() {
        MongoCollection<Document> listingsCollection = mongoClient.getDatabase("sample_airbnb").getCollection("listingsAndReviews");

        try (MongoCursor<Document> cursor = listingsCollection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                String description = document.getString("description");
                List<Double> embeddings = geminiClient.getEmbedding(description);

                if (embeddings != null) {
                    document.put("embeddings", embeddings);
                    listingsCollection.replaceOne(Filters.eq("_id", document.getString("_id")), document);
                }
            }
        }
    }

    public void performVectorSearch(String query){
        MongoCollection<Document> collection = mongoClient.getDatabase("sample_airbnb").getCollection("listingsAndReviews");

        List<Double> queryEmbeddings = geminiClient.getEmbedding(query);
        String indexName = "vector_index";
        int numCandidates = 150;
        int limit = 10;

        List<Bson> pipeline = Arrays.asList(new Document("$vectorSearch",
                        new Document("index", indexName)
                                .append("path", "embeddings")
                                .append("queryVector", queryEmbeddings )
                                .append("numCandidates", numCandidates)
                                .append("limit", limit)),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("name", 1L)
                                .append("listing_url", 1L)
                                .append("description", 1L)
                                .append("price", 1L)),
                new Document("$limit", 5L));

        collection.aggregate(pipeline)
                .forEach(doc -> System.out.println(doc.toJson()));
    }
}

