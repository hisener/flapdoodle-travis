package com.github.hisener;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FlapdoodleTest extends MongoTestCase {

    @Test
    void insertAndFind() {
        ConnectionString connectionString = new ConnectionString(getUri());
        MongoCollection<Document> movies =
                MongoClients.create(connectionString)
                        .getDatabase(connectionString.getDatabase())
                        .getCollection("movies");

        Document movie = new Document("title", "The Shawshank Redemption").append("year", 1994);

        StepVerifier.create(Mono.from(movies.insertOne(movie)))
                .expectNextCount(1)
                .expectComplete()
                .verify();

        StepVerifier.create(Flux.from(movies.find()))
                .assertNext(document -> assertEquals(movie, document))
                .expectComplete()
                .verify();
    }
}
