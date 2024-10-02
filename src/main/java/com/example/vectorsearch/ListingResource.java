package com.example.vectorsearch;


import com.example.vectorsearch.service.ListingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.Document;

import java.util.List;


@Path("/api/listings")
public class ListingResource {

    @Inject
    ListingService listingService;

    @POST
    @Path("/generate-embeddings")
    public Response generateEmbeddings() {
        listingService.generateAndStoreEmbeddings();
        return Response.ok("Embeddings generated and stored").build();
    }

    @GET
    @Path("/perform-vector-search")
    @Produces(MediaType.APPLICATION_JSON)  // Ensure JSON response format
    public Response performVectorSearch(@QueryParam("query") String query) {
        List<Document> results = listingService.performVectorSearch(query);
        return Response.ok(results).build();
    }
}

