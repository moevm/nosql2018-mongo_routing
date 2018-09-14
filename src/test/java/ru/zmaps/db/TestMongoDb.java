package ru.zmaps.db;


import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Test;

public class TestMongoDb {

    @Test
    public void testDbLocked() {
        MongoClient mongoClient = new MongoClient(new ServerAddress("212.8.247.116", 27017));

        Assert.assertFalse(mongoClient.isLocked());
    }

    @Test
    public void testWriteRead() {
        MongoClient mongoClient = new MongoClient(new ServerAddress("212.8.247.116", 27017));

        MongoDatabase db = mongoClient.getDatabase("test");

        MongoCollection<Document> collection = db.getCollection("testCollection");

        Document doc = new Document("key", "testValue");

        collection.insertOne(doc);

        collection = db.getCollection("testCollection");

        FindIterable<Document> documents = collection.find();

        for (Document document : documents) {
            if (document.containsKey("key")) {
                Assert.assertEquals(document.get("key"), "testValue");
            }
        }
    }

}
