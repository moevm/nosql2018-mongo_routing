package ru.zmaps.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.zmaps.parser.entity.Node;
import ru.zmaps.parser.entity.Way;

import javax.annotation.PostConstruct;

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

    public Way getNearest(Point position) {

        Query query = new Query();
        query.addCriteria(Criteria.where("point").near(position)).limit(1);

        return mongo.find(query, Way.class).get(0);
    }

}
