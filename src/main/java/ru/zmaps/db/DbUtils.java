package ru.zmaps.db;

import com.mongodb.BasicDBObject;
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
import ru.zmaps.parser.entity.Node;
import ru.zmaps.parser.entity.Tag;
import ru.zmaps.parser.entity.Way;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DbUtils {

    MongoClient mongoClient = new MongoClient(new ServerAddress("212.8.247.116", 27017));

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

        nodes.insertOne(convertNode(node));
    }

    public Node getNodeById(Long id) {
        MongoCollection<Document> nodes = db.getCollection("nodes");

        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);

        return deConvertNode(nodes.find(query).first());
    }

    public void addWay(Way way) {
        MongoCollection<Document> nodes = db.getCollection("ways");

        nodes.insertOne(convertWay(way));
    }

    public Node getNearestPoint(double x, double y) {
        MongoCollection<Document> nodes = db.getCollection("nodes");

        Point refPoint = new Point(new Position(x, y));
        FindIterable<Document> coordinates = nodes.find(Filters.near("geometry", refPoint, null, null)).limit(1);

        return deConvertNode(Objects.requireNonNull(coordinates.first()));
    }


    public Way getNearestWay(double x, double y) {
        MongoCollection<Document> nodes = db.getCollection("ways");

        Point refPoint = new Point(new Position(x, y));
        FindIterable<Document> coordinates = nodes.find(Filters.near("points.geometry", refPoint, null, null)).limit(1);

        return deConvertWay(Objects.requireNonNull(coordinates.first()));
    }

    private Node deConvertNode(Document p) {
        List<Double> coordinate = (List<Double>) ((Document) p.get("geometry")).get("coordinates");
        Node node = new Node(coordinate.get(0), coordinate.get(1), p.getLong("_id"));
        node.setTags(deConvertTags((List<Document>) p.get("tags")));
        return node;
    }


    private Document convertWay(Way way) {
        Document doc = new Document();

        doc.append("_id", way.getId());
        List<Document> points = new ArrayList<>();

        way.getNodes().forEach(n -> {
            Document document = new Document();
            document.append("_id", n.getId());
            document.append("geometry", new Point(new Position(n.getLat(), n.getLon())));

            points.add(document);
        });

        doc.append("tags", convertTags(way.getTags()));
        doc.append("points", points);
        return doc;
    }


    private Way deConvertWay(Document doc) {
        List<Document> points = (List<Document>) doc.get("points");

        Way way = new Way(doc.getLong("_id"));

        points.forEach(p -> {
            List<Double> coordinate = (List<Double>) ((Document) p.get("geometry")).get("coordinates");

            Node node = new Node(coordinate.get(0), coordinate.get(1), p.getLong("_id"));
            node.setTags(null);
            way.addNode(node);
        });

        return way;
    }


    private Document convertNode(Node node) {
        Document doc = new Document();

        doc.append("_id", node.getId());
        doc.append("geometry", new Point(new Position(node.getLat(), node.getLon())));
        doc.append("tags", convertTags(node.getTags()));

        return doc;
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
