package ru.zmaps.db;


import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.junit.Assert;
import org.junit.Test;

public class TestConnection {

    @Test
    public void test() {
        MongoClient mongoClient = new MongoClient(new ServerAddress("212.8.247.116", 27017));

        Assert.assertFalse(mongoClient.isLocked());
    }
}
