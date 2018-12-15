package ru.zmaps.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.zmaps.parser.entity.RouteNode;
import ru.zmaps.parser.entity.Way;

import javax.annotation.PostConstruct;

@Repository
public class RouteNodeDAO {


    @Autowired
    private MongoOperations mongo;

    @PostConstruct
    public void initIndexes() {
        //mongo.indexOps(Way.class).ensureIndex(new GeospatialIndex("point"));
    }

    public void save(RouteNode way) {
        mongo.save(way);
    }

}
