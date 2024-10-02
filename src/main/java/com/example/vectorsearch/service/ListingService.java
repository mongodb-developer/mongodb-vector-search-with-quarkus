package com.example.vectorsearch.service;

import com.example.vectorsearch.gateway.GeminiAIGatewayImpl;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.Updates;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class ListingService {

    private static final int BATCH_SIZE = 500;
    @Inject
    MongoClient mongoClient;

    @Inject
    GeminiAIGatewayImpl geminiClient;

    public void generateAndStoreEmbeddings() {
        MongoCollection<Document> listingsCollection = mongoClient.getDatabase("sample_airbnb").getCollection("listingsAndReviews");
        int processedDocuments = 0;
        long totalDocuments = listingsCollection.countDocuments();
        while (processedDocuments < totalDocuments) {
            List<Document> documents = listingsCollection.find()
                    .skip(processedDocuments)
                    .limit(BATCH_SIZE)
                    .into(new ArrayList<>());
            List<UpdateOneModel<Document>> bulkUpdates = new ArrayList<>();

            for (Document document : documents) {
                String description = document.getString("description");
                List<Double> embeddings = geminiClient.getEmbedding(description);
                if (embeddings != null) {
                    UpdateOneModel<Document> updateModel = new UpdateOneModel<>(
                            Filters.eq("_id", document.getString("_id")),
                            Updates.set("embeddings", embeddings)
                    );
                    bulkUpdates.add(updateModel);
                }
            }
            if (!bulkUpdates.isEmpty()) {
                listingsCollection.bulkWrite(bulkUpdates, new BulkWriteOptions().ordered(false));
            }
            processedDocuments += documents.size();
            System.out.println("Processed " + processedDocuments + " out of " + totalDocuments + " documents.");
        }
        System.out.println("Embedding generation complete for all documents.");
    }



    public List<Document> performVectorSearch(String query){
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
        List<Document> results = new ArrayList<>();
        collection.aggregate(pipeline).forEach(results::add);
        return results;
    }
}

