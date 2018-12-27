package ru.zmaps.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.zmaps.parser.entity.Node;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class NodeDAO {
    @Autowired
    private MongoOperations mongo;

    @PostConstruct
    public void initIndexes() {
        mongo.indexOps(Node.class).ensureIndex(new GeospatialIndex("point"));
    }

    public void save(Node node) {
        mongo.save(node);
    }

    public void save(List<Node> node) {
        mongo.insertAll(node);
    }

    public Node getNodeById(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id)).limit(1);

        List<Node> nodes = mongo.find(query, Node.class);
        if (nodes.size() == 0) return null;

        return nodes.get(0);
    }

    public Node getNearest(Point position) {
        Query query = new Query();
        query.addCriteria(Criteria.where("point").near(position)).limit(1);

        return mongo.find(query, Node.class).get(0);
    }

    public List<Node> getByLimit(int limit, int skip) {
        Query query = new Query();
        query.limit(limit).skip(skip);
        return mongo.find(query, Node.class);
    }

}
