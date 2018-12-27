package ru.zmaps.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.zmaps.parser.entity.Relation;

import java.util.List;

@Repository
public class RelationDAO {
    @Autowired
    private MongoOperations mongo;

    public void save(Relation node) {
        mongo.save(node);
    }

    public void save(List<Relation> node) {
        mongo.insertAll(node);
    }

    public List<Relation> getByCriteria(Criteria cr, int limit, int skip) {
        Query query = new Query();
        query.addCriteria(cr).limit(limit).skip(skip);
        return mongo.find(query, Relation.class);
    }


}
