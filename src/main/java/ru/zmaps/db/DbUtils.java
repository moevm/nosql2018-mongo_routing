package ru.zmaps.db;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zmaps.parser.entity.Node;
import ru.zmaps.parser.entity.Tag;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DbUtils {

    MongoClient mongoClient = new MongoClient(new ServerAddress("212.8.247.116", 27017));

    MongoDatabase db = mongoClient.getDatabase("test");

    static DbUtils instance;

    @PostConstruct
    public void init() {
    }

    private DbUtils() {
    }

    public static DbUtils getInstance() {
        if (instance == null) {
            instance = new DbUtils();
        }

        return instance;
    }

    public void addNode(Node node) {
        MongoCollection<Document> ways = db.getCollection("nodes");

        Document doc = new Document();

        Document point = new Document("type", "Point");

        point.append("coordinates", Arrays.asList(node.getLat(), node.getLon()));

        doc.append("_id", node.getId());
        doc.append("geometry", point);
        doc.append("tags", convertTags(node.getTags()));

        ways.insertOne(doc);
    }

    public Point getNearest(double x, double y) {
        return null;//TODO:
    }

    public static List<Document> convertTags(List<Tag> tags) {
        ArrayList<Document> documents = new ArrayList<>();
        tags.forEach(t -> documents.add(new Document(t.getKey(), t.getValue())));

        return documents;
    }

}
