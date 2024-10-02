# Building Quarkus Application to Perform MongoDB Vector Search

This repository demonstrates how to build a Quarkus application that performs vector search using MongoDB. Traditional keyword-based search methods often fall short of delivering relevant results in today's data-driven world. Vector search is a more advanced technique that understands the context and meaning of data, enabling smarter and more intuitive searches.

In this guide, you'll learn how to harness the power of Quarkus and MongoDB to build a modern search engine that supports vector search. This is particularly useful for applications like recommendation systems, content discovery, and AI-driven search experiences.

## Key Features
- **Vector Search**: Leverage MongoDB's vector search to build advanced, semantic search systems.
- **Quarkus**: A lightweight, high-performance Java framework that integrates seamlessly with MongoDB.
- **Gemini AI**: Generate vector embeddings for documents using the Gemini AI API.

## Prerequisites
Before running the project, ensure you have the following:
- A **MongoDB Atlas** free-tier cluster. [Create your cluster here](https://www.mongodb.com/cloud/atlas/register).
- Sample data loaded into the MongoDB collection.
- **Java 17+** installed.
- A **Gemini AI Key**. [Generate your free Gemini API key](https://gemini-api-provider-link.com).

## Project Setup

### 1. Understanding Vector Search
Vector search uses vector embeddings to represent data points in a multi-dimensional space. Similar items have embeddings close to each other, making it easier to perform semantic searches. MongoDB supports vector search with various similarity measures like Euclidean distance, Cosine similarity, and Dot product calculations.

### 2. Generating Vector Embeddings
Embeddings are generated using the Gemini AI API for the `description` field of each document. The embeddings capture semantic relationships and allow MongoDB to perform efficient searches.

To generate embeddings, use the following REST API endpoint:

```bash
curl -X POST http://localhost:8080/api/listings/generate-embeddings
```

### 3. Creating the Vector Search Index
Once embeddings are generated, create a vector search index in MongoDB Atlas:

1. Navigate to your Atlas cluster.
2. Select **Atlas Search** and create an index with the following JSON configuration:

```json
{
 "fields": [
   {
     "numDimensions": 768,
     "path": "embeddings",
     "similarity": "euclidean",
     "type": "vector"
   }
 ]
}
```

### 4. Performing Vector Search
After the embeddings and index are set up, you can perform a vector search using the following endpoint:

```bash
curl -X GET "http://localhost:8080/api/listings/perform-vector-search?query=Hotels%20that%20are%20recommended%20for%20romantic%20stay" | jq
```

This will return a list of recommended hotels based on the semantic similarity of the query.

### Example Response
```json
[
  {
    "listing_url": "https://www.airbnb.com/rooms/15266254",
    "name": "The Manhattan Club in the heart of midtown!!!!",
    "description": "My place is good for couples, solo adventurers, and business travelers.",
    "price": 305.00
  }
]
```

## Running the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/mongodb-developer/mongodb-vector-search-with-quarkus.git
   ```
2. Navigate to the project directory:
   ```bash
   cd mongodb-vector-search-with-quarkus
   ```
3. Set up your environment variables for the MongoDB connection and Gemini API key.
4. Build and run the application:
   ```bash
   ./mvnw compile quarkus:dev
   ```

Now, you can interact with the APIs for generating embeddings and performing vector search.

## Conclusion
This Quarkus and MongoDB-based application demonstrates how vector search can improve search relevance and user experience. Whether you’re building recommendation systems, enhancing content discovery, or enabling smarter searches, vector search brings a new level of contextual understanding to your applications.

