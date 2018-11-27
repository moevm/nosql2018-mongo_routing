package ru.zmaps.db;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import ru.zmaps.parser.entity.Node;
import ru.zmaps.parser.entity.Tag;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;


@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DbUtils {

    MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017));

    MongoDatabase db = mongoClient.getDatabase("test");

    static DbUtils instance;

    private DbUtils() {
    }

    public static DbUtils getInstance() {
        if (instance == null) {
            instance = new DbUtils();
        }

        return instance;
    }

    public void addNode(Node node) {
        MongoCollection<Document> nodes = db.getCollection("nodes");

        Document doc = new Document();

        doc.append("_id", node.getId());
        doc.append("geometry", new com.mongodb.client.model.geojson.Point(new Position(node.getLat(), node.getLon())));
        doc.append("tags", convertTags(node.getTags()));

        nodes.insertOne(doc);
    }

    public Node getNearest(double x, double y) {
        MongoCollection<Document> nodes = db.getCollection("nodes");

        Point refPoint = new Point(new Position(x, y));
        FindIterable<Document> coordinates = nodes.find(Filters.near("geometry", refPoint, null, null)).limit(1);

        return deConvertPoint(Objects.requireNonNull(coordinates.first()));
    }

    private Node deConvertPoint(Document p) {
        List<Double> coordinate = (List<Double>) ((Document) p.get("geometry")).get("coordinates");
        Node node = new Node(coordinate.get(0), coordinate.get(1), p.getLong("_id"));
        node.setTags(deConvertTags((List<Document>) p.get("tags")));
        return node;
    }

    public static List<Document> convertTags(List<Tag> tags) {
        ArrayList<Document> documents = new ArrayList<>();
        tags.forEach(t -> documents.add(new Document(t.getKey(), t.getValue())));

        return documents;
    }

    public static List<Tag> deConvertTags(List<Document> docs) {
        List<Tag> tags = new ArrayList<>();

        docs.forEach(d -> d.forEach((key, value) -> tags.add(new Tag(key, (String) value))));

        return tags;
    }

}
