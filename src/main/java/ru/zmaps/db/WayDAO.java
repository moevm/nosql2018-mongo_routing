package ru.zmaps.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.zmaps.parser.entity.Way;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class WayDAO {
    @Autowired
    private MongoOperations mongo;

    @PostConstruct
    public void initIndexes() {
        //mongo.indexOps(Way.class).ensureIndex(new GeospatialIndex("point"));
    }

    public void save(Way way) {
        mongo.save(way);
    }

    public void save(List<Way> way) {
        mongo.insertAll(way);
    }

    public List<Way> get(Criteria criteria,int limit,int skip) {

        Query query = new Query();
        query.addCriteria(criteria);

        return mongo.find(query.limit(limit).skip(skip), Way.class);
    }

    public List<Way> get() {
        return mongo.findAll(Way.class);
    }

    public Way getNearest(Point position) {

        Query query = new Query();
        query.addCriteria(Criteria.where("point").near(position)).limit(1);

        return mongo.find(query, Way.class).get(0);
    }

}
